package com.pactera

import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component

/**
  * 项目启动时执行
  */
@Component
class CommonStart extends CommandLineRunner {


  override def run(args: String*): Unit = {

    println("start-----------------------")


  }
}
