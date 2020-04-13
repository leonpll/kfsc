package com.pactera

import java.io.FileInputStream
import java.util.Properties


/**
  * 获取环境变量
  */
object EnvironmentAwareUtil {


  var properties: Properties = null

  // 获取环境变量
  def loadProperties(): Unit = {

    properties = new Properties()
    val path = Thread.currentThread().getContextClassLoader.getResource("application-dev.properties").getPath
    properties.load(new FileInputStream(path))

  }

  def getPro(str: String): String = {

    properties.getProperty(str)
  }
}
