package com.pactera.tools

import org.apache.flink.api.java.utils.ParameterTool

/**
  * 获取命令行参数
  */
object ParamsUtil {

  case class Params(runMode:String,pp:String,ep:String,brokers:String,groupid:String,sourceTopic:String,offsetreset:String,checkpointUri:String,schemaName:String,tableName:String,url:String,user:String,password:String,batchSize:String,batchInterval:String,hadoopUserName:String)

  def transParams(args:Array[String],mode:String): Params = {
    if("yarn".equalsIgnoreCase(mode)){
      val parameters=ParameterTool.fromArgs(args)
      val runMode=parameters.get("runMode")
      //kafka分区并行度
      val pp=parameters.get("pp")
      //执行并行度
      val ep=parameters.get("ep")
      // kafka broker 地址
      val brokers=parameters.get("brokers")
      // 消费者组
      val groupid=parameters.get("groupid")
      // 消费topic
      val sourceTopic=parameters.get("sourceTopic")
      // 消费的offset   1. smallest 2. largest 3. groupoffsets
      val offsetreset=parameters.get("offsetreset")
      // 检查点位置
      val checkpointUri=parameters.get("checkpointUri")
      // clickhouse 数据库名称
      val schemaName = parameters.get("schemaName")
      // clichouse 表名
      val tableName = parameters.get("tableName")
      // jdbc url
      val  url = parameters.get("url")
      val  user = parameters.get("user")
      val password = parameters.get("password")
      // 单分区每批次数据条数
      val batchSize = parameters.get("batchSize")
      // 每次写入的间隔时间
      val batchInterval = parameters.get("batchInterval")
      // hdfs的用户名
      val hadoopUserName = parameters.get("hadoopUserName")
      Params(runMode,pp,ep,brokers,groupid,sourceTopic,offsetreset,checkpointUri,schemaName,tableName,url,user,password,batchSize,batchInterval,hadoopUserName)
    }else{
      val runMode = "pro"
      //kafka分区并行度
      val pp = "4"
      //执行并行度
      val ep = "4"
      val brokers = "wecloud.nekafka1.testd1.bj3.autoai.com:9092,wecloud.nekafka2.testd1.bj3.autoai.com:9092,wecloud.nekafka3.testd1.bj3.autoai.com:9092"
      val groupid = "test_ecpt_002"
      val sourceTopic = "eye_cloud_platform_topic"
      val offsetreset = "smallest"
      val checkpointUri = ""
      val schemaName = "eye"
      val tableName = "raw_msg"
      val url = "jdbc:clickhouse://192.168.147.63:9090"
      val user = "insert"
      val password = ""
      val batchSize = "100"
      val batchInterval = "1000"
      val hadoopUserName = "hdfs"
      Params(runMode,pp,ep,brokers,groupid,sourceTopic,offsetreset,checkpointUri,schemaName,tableName,url,user,password,batchSize,batchInterval,hadoopUserName)
    }
  }


}
