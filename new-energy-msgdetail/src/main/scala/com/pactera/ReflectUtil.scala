package com.pactera

import java.lang.reflect.Field
import java.util

import com.google.protobuf.ByteString
import lombok.extern.slf4j.Slf4j
import org.slf4j.LoggerFactory


@Slf4j
object ReflectUtil {


  val LOG = LoggerFactory.getLogger(ReflectUtil.getClass)
  /**
    * 对象转map
    *
    * @param obj
    * @return
    */
  def objectToMap(obj: Any): util.Map[String, Any] = {

    var carryClassName : Boolean = false
    if (obj == null) return null
    // 获取属性数组
    val declaredFields = obj.getClass.getDeclaredFields
    fieldToMap(declaredFields, obj ,carryClassName)
  }

  /**
    * 对象转map
    *
    * @param obj
    * @param carryClassName 字段是否携带类名 true携带
    * @return
    */
  def objectToMap(obj: Any,carryClassName : Boolean): util.Map[String, Any] = {

    if (obj == null) return null
    // 获取属性数组
    val declaredFields = obj.getClass.getDeclaredFields
    fieldToMap(declaredFields, obj ,carryClassName)
  }


  private def fieldToMap(fields: Array[Field], obj: Any , carryClassName : Boolean) = {
    val map = new util.HashMap[String, Any]

    // 获取类名并转换
    var simpleName = obj.getClass.getSimpleName
    simpleName = toLowerCaseFirstOne(simpleName)
    for (field <- fields) {
      field.setAccessible(true)

      // 判断最feildName是否已“_”结尾
      var fieldName: String = field.getName
      val filelNameLastChar: String = field.getName.charAt(field.getName.length - 1).toString
      if (filelNameLastChar.equals("_")) {

        fieldName = fieldName.substring(0,fieldName.length-1)
        try {

          var value = field.get(obj)
          // 将byteString类型转成byte数组
          if (field.getType eq classOf[ByteString]) {

            // stalls,eWFlag,eCompanyWFlag 三种数据格式比较特殊，需要转成数组存储。其他转String字符串
            if (fieldName.equals("bProbeTemperature") || fieldName.equals("eWFlag") || fieldName.equals("eCompanyWFlag")) {

              value = value.asInstanceOf[ByteString].toByteArray;

            } else if (fieldName.equals("sProbeTemperature")) {
              value = value.asInstanceOf[ByteString].toByteArray.mkString
            } else value = value.asInstanceOf[ByteString].toByteArray.mkString //bytesToString(value.asInstanceOf[ByteString],"")

          }
          if (carryClassName)
            map.put((new StringBuffer).append(simpleName).append(".").append(simpleName).append("_").append(fieldName).toString, value)
          else
            map.put((new StringBuffer).append(simpleName).append("_").append(fieldName).toString, value)
        } catch {

          case e: IllegalAccessException =>
            LOG.error("字段解析Exception----{}-------{}" + fieldName + e.getMessage)
        }
      }
    }
    map
  }


  /**
    * @Description: 将集合中所有相同属性拼为数组
    *               * @param list集合
    * @return: Map<String, Object>
    * @Author: LL
    * @Date: 2019/10/12  19:24
    */
  def listToMap(list: util.List[_]): util.Map[String, util.LinkedList[Any]] = {
    val returnMap = new util.HashMap[String, util.LinkedList[Any]]

    import scala.collection.JavaConversions._
    for (o <- list) { // 对象转map
      val map = objectToMap(o,true)

      for (m <- map.entrySet) {

        val key = m.getKey
        val value = m.getValue
        // 相同属值性合并数组
        if (returnMap.get(key) != null) returnMap.get(key).add(value)
        else {
          val valueList = new util.LinkedList[Any]
          valueList.add(value)
          returnMap.put(key, valueList)
        }
      }
    }
    returnMap
  }


  /**
    * 首字母小写
    *
    * @param s
    * @return
    */
  def toLowerCaseFirstOne(s: String): String = {
    if (s == null) return s
    if (Character.isLowerCase(s.charAt(0))) s
    else (new StringBuilder).append(Character.toLowerCase(s.charAt(0))).append(s.substring(1)).toString
  }

}
