package com.pactera.tools

/**
  * 消费kaka数据，不做任何业务处理，测试kafka消费数据数据能力
  */
object RawKafkaUtil {

//  val LOG = LoggerFactory.getLogger(RawKafkaUtil.getClass)
  var received = 0
  val logfreq = 50000
  var lastLog: Long = -1
  var bsize = 0
  var lastElements = 0
  def calcSpeed(msg:String): Array[Int] = {
    received += 1
    bsize = msg.getBytes().length +bsize
    if (received % logfreq == 0) { // throughput over entire time
      var now = System.currentTimeMillis
      // throughput for the last "logfreq" elements
      if (lastLog == -1) { // init (the first)
        lastLog = now
//        lastElements = received
      } else {
        var timeDiff = now - lastLog
//        var elementDiff = received - lastElements
          var elementDiff = received
//        var ex =  elementDiff/(timeDiff/1000)
          var ex = logfreq/(timeDiff/1000)
//        LOG.info("During the last {} ms, we received {} elements. That's {} elements/second/core", timeDiff.toString, elementDiff.toString, (elementDiff * ex).toString)
        printf("During the last %s ms, we received %s elements. That's %s elements/second/core KB received %s \n",timeDiff.toString, elementDiff.toString, ex.toString,(bsize/1024).toString)
        // reinit
        lastLog = now
//        lastElements = received
        bsize = 0
        received = 0

      }
    }
    Array(1)
  }
}
