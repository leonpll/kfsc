import java.sql.{Connection, DatabaseMetaData, PreparedStatement, ResultSet, SQLException}
import java.util

import com.pactera.ParserMap
import com.pactera.tools.ParamsUtil
import ru.yandex.clickhouse.BalancedClickhouseDataSource
import ru.yandex.clickhouse.settings.ClickHouseProperties

object  TestSome {



  var batchCount:Int=0
  var batchSize:Int =5000
  var lastBatchTime = System.currentTimeMillis()
  // 峰值提交值信息


  def main(args: Array[String]): Unit = {
    val mode = "dev"
    //    val mode = "dev"
    val params = ParamsUtil.transParams(args, mode)


    val properties = new ClickHouseProperties()
    properties.setUser(params.user)
    properties.setPassword(params.password)
    properties.setDatabase(params.schemaName)
    properties.setSocketTimeout(50000)
    //    dataSource = new ClickHouseDataSource(url, properties)
    val dataSource = new BalancedClickhouseDataSource(params.url, properties)

    val connection = dataSource.getConnection()

    // 动态获取表字段:类型
    val columnNameAndType = getColumnNameAndType(params.tableName, params.schemaName, connection)
    val insertSql = generatePreparedSqlByColumnNameAndType(columnNameAndType, params.schemaName, params.tableName)
    var pst = connection.prepareStatement(insertSql)
    val start = System.currentTimeMillis()
    for(i <- 1 until 1000000){
//      println(columnNameAndType.size())
      pst = generatePreparedCloumns(pst, columnNameAndType, null)
      batchCount = batchCount + 1

//      pst.addBatch()

      // 当大于batchSize条数或者时间窗口为lastBatchTime时提交
      if (batchCount >= batchSize || lastBatchTime < System.currentTimeMillis - 10000) {
        printf("batch %s , time %s \n",batchCount.toString, ((System.currentTimeMillis - lastBatchTime)).toString)
        batchCount = 0
        lastBatchTime = System.currentTimeMillis
        //      pst.executeBatch()
      }

    }

//    val end = System.currentTimeMillis()


  }

  /**
    * 动态封装sql语句
    *
    * @param columnNames
    * @param schemaName 库名
    * @param tableName  表名
    * @return sql语句
    */
  def generatePreparedSqlByColumnNameAndType(columnNames: util.List[String], schemaName: String, tableName: String): String = {
    var insertColumns: String = ""
    var insertValues: String = ""
    if (columnNames != null && columnNames.size > 0) {
      insertColumns += columnNames.get(0).split(":")(0)
      insertValues += "?"
    }

    for (i <- 1 until columnNames.size()) {
      insertColumns += ", " + columnNames.get(i).split(":")(0)
      insertValues += ", " + "?"
    }


    val insertSql: String = "INSERT INTO " + schemaName + "." + tableName + " (" + insertColumns + ") values(" + insertValues + ")"
    insertSql
  }

  /**
    * 根据库/表明获取字段名称及类型
    *
    * @param tableName  表名
    * @param schemaName 库名
    * @param conn       连接
    * @return List<String> String=字段名:自动类型
    * @throws SQLException
    */
  @throws[SQLException]
  def getColumnNameAndType(tableName: String, schemaName: String, conn: Connection): util.List[String] = {
    val dd: DatabaseMetaData = conn.getMetaData
    val columnNameAndType: util.List[String] = new util.ArrayList[String]
    val colRet: ResultSet = dd.getColumns(null, "%", tableName, "%")
    while ( {
      colRet.next
    }) {
      val columnName: String = colRet.getString("COLUMN_NAME")
      val columnType: String = colRet.getString("TYPE_NAME")
      columnNameAndType.add(columnName + ":" + columnType)
    }


    columnNameAndType
  }


  /**
    * 动态拼装sql值
    *
    * @param ps
    * @param columnNamesAndType 字段类型
    * @return
    */
  def generatePreparedCloumns(ps: PreparedStatement, columnNamesAndType: util.List[String], map: util.Map[String, Any]) = {
    try {
      var y: Int = 0
      for (i <- 0 until columnNamesAndType.size()) {

        // 设置sql值时的下标
        y = i + 1

        val columnName = columnNamesAndType.get(i).split(":")(0)
        val value = map.get(columnName)
        var clickhouseType = columnNamesAndType.get(i).split(":")(1)
        //                value=String.valueOf(value);
        var convert_value = String.valueOf(value)
        if (convert_value == "null" || convert_value == "") convert_value = "null"

        if (isNullable(clickhouseType)) clickhouseType = unwrapNullable(clickhouseType)

        if (clickhouseType.startsWith("Int") || clickhouseType.startsWith("UInt")) {
          if (convert_value == "null") convert_value = "0"
          if (clickhouseType.endsWith("64")) ps.setLong(y, convert_value.toLong)
          else ps.setInt(y, Integer.valueOf(convert_value))
        }
        else if ("String" == clickhouseType) {
          if (convert_value == "null") convert_value = "null"
          ps.setString(y, String.valueOf(convert_value))
        }
        else if (clickhouseType.startsWith("Float32")) {
          if (convert_value == "null") {
            convert_value = "0"
          }
          ps.setFloat(y, convert_value.toFloat)
        }
        else if (clickhouseType.startsWith("Float64")) {
          if (convert_value == "null") {
            convert_value = "0"
          }
          ps.setDouble(y, convert_value.toDouble)
        }
        else if ("Date" == clickhouseType) {
          if (convert_value == "null") convert_value = "0"
          ps.setString(y, String.valueOf(convert_value))
        }
        else if ("DateTime" == clickhouseType) {
          if (convert_value == "null") convert_value = "0"
          ps.setString(y, String.valueOf(convert_value))
        }
        else if ("FixedString" == clickhouseType) { // BLOB 暂不处理
          ps.setString(y, "ERROR")
        }
        else if (isArray(clickhouseType)) {

          val arrayType: String = getArrayType(clickhouseType)
          if (convert_value == "null") ps.setString(y, "[]")

          // 这三类由Byte[]存入
          //TODO 判断可能有特殊处理，暂时保留
          else if (columnName.equals("evBattery_bProbeTemperature") || columnName.equals("evWarning_eWFlag") || columnName.equals("evWarning_eCompanyWFlag")) {
            ps.setString(y, util.Arrays.toString(value.asInstanceOf[scala.Array[Byte]]))
          } else if (columnName.equals("evStorageTemperature.evStorageTemperature_sProbeTemperature")) {
            // 该字段string存入有问题，暂时存为Int64类型了，待解决
            // println(columnName + "---------------------------------------" + convert_value + "---" + value)
            ps.setString(y, convert_value)
            //ps.setObject(y, "['dsa','dsadsa']")
          }
          else if (arrayType.contains("Array")) {

            // val list = value.asInstanceOf[util.LinkedList[Object]]
            ps.setString(y, convert_value)
          } else { // 其他字段由LinkedList转换[]
            //            println(columnName + "-----------------------" )
            //            val list = value.asInstanceOf[util.LinkedList[Object]]
            //            val longArray = new scala.Array[Any](list.size())
            ps.setString(y, convert_value)
            //            if (arrayType.startsWith("Int") || arrayType.startsWith("UInt")) {
            //              if (arrayType.endsWith("64")) {
            //                val list = value.asInstanceOf[util.LinkedList[Long]]
            //                val longArray = new scala.Array[Long](list.size())
            //                ps.setString(y, util.Arrays.toString(longArray))
            //              } else {
            //                val list = value.asInstanceOf[util.LinkedList[Int]]
            //                val intArray = new scala.Array[Int](list.size())
            //                ps.setString(y, util.Arrays.toString(intArray))
            //              }

            //            } else if ("String" == arrayType) {
            //
            //              val list = value.asInstanceOf[util.LinkedList[String]]
            //              val strArray = new scala.Array[String](list.size())
            //              //ps.setString(y, util.Arrays.toString)
            //            }
            //var z = new scala.Array[Any](list.size())

          }

        }
        else ps.setString(y, "ERROR")
      }
    } catch {
      case e: Exception =>
      //      LOG.error("初始化clickhouse sinks生成prepared columns异常------{}", e.getMessage)
    }
    ps
  }

  def unwrapNullable(clickhouseType: String): String = {
    clickhouseType.substring("Nullable(".length, clickhouseType.length - 1)
  }

  /**
    * 判断是否为Nullable
    *
    * @param clickhouseType
    * @return
    */
  def isNullable(clickhouseType: String): Boolean = {
    clickhouseType.startsWith("Nullable(") && clickhouseType.endsWith(")")
  }

  /**
    * 判断是否为Array
    *
    * @param clickhouseType
    * @return
    */
  def isArray(clickhouseType: String): Boolean = {
    clickhouseType.startsWith("Array(") && clickhouseType.endsWith(")")
  }

  /**
    * 获取array类型
    *
    * @param clickhouseType
    * @return
    */
  def getArrayType(clickhouseType: String): String = {
    clickhouseType.substring(6, clickhouseType.length - 1)
  }

  def paramMap(msg: String): Unit = {
    ParserMap.parserStr(msg);
    ParserMap.parserStrGet(msg);

  }



}