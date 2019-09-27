package com.pactera

import java.util.Properties

import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.api.common.typeinfo.TypeInformation
import org.apache.flink.api.scala._
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer


object ExcuteObject  {

  implicit val typeInfo = TypeInformation.of(classOf[String])
  implicit val typeInfoM = TypeInformation.of(classOf[java.util.Map[String,Object]])


  def startWithScala() : Unit = {


    println("---------------start-----------------------")

    val env = StreamExecutionEnvironment.getExecutionEnvironment

    // 设置kafka配置信息
    val properties = new Properties()
    properties.setProperty("bootstrap.servers", "192.168.5.87:9092")
    properties.setProperty("group.id", "test")


    //
    val consumer = new FlinkKafkaConsumer[String]("dgp", new SimpleStringSchema, properties)
    val stream = env.addSource(consumer)


    // 处理数据格式
    val log_convert_message = stream.map(row => ParserMap.parserStr(row)).filter(rowMap => rowMap != null)

    println(log_convert_message)

    // 创建clickhouseSink连接
    val schemaName = "default"
    val tableName = "user"
    val url = "jdbc:clickhouse://111.231.193.90:8123"
    val user = "default"
    val password = "default"
    val batchSize = "5" //每5条提交一次
    val batchInterval = "4000" // 每隔4秒提交一次
    val clickhouseSink = new ClickHouseJDBCSinkScala(schemaName, tableName, url, user, password, Integer.valueOf(batchSize), Integer.valueOf(batchInterval))

    log_convert_message.addSink(clickhouseSink)
    env.execute("Kafka Window WordCount")
  }
}
