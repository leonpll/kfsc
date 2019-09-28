package com.pactera

import java.util.Random
import collection.JavaConverters._

object ParserMap {


  def parserStr(a: String): java.util.Map[String, Any] = {

    var user: Map[String, Any] = Map()

    val id: Int = new Random().nextInt(1000000) + 1

    val z = Array("1970-12-11", "1972-12-11", "1971-12-11", "1973-12-11", "1974-12-11", "1975-12-11", "1976-12-11", "1977-12-11", "1978-12-11", "1979-12-11",
      "1980-12-11", "1982-12-11", "1981-12-11", "1983-12-11", "1984-12-11", "1985-12-11", "1986-12-11", "1987-12-11", "1988-12-11", "1989-12-11",
      "1990-12-11", "1992-12-11", "1991-12-11", "1993-12-11", "1994-12-11", "1995-12-11", "1996-12-11", "1997-12-11", "1998-12-11", "1999-12-11")

    user += ("id" -> id)
    user += ("username" -> "我是测试数据")
    user += ("realname" -> "我是测试数据1")
    user += ("birthday" -> z(new Random().nextInt(29) + 1))
    user.asJava;
  }
}
