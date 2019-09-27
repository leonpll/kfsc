package com.pactera

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.{CommandLineRunner, SpringApplication}

/**
  * 项目启动类
  */
@SpringBootApplication
class ScalaBootApp extends CommandLineRunner {


  // 启动时执行func
  override def run(args: String*): Unit = {

    ExcuteObject.startWithScala()

  }
}


object ScalaBootApp {

  def main(args: Array[String]): Unit = {
    SpringApplication.run(classOf[ScalaBootApp], args: _*)
  }

}