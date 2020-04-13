package com.pactera

import java.util.Properties

import com.pactera.tools.{EnvUtil, ParamsUtil, RawKafkaUtil}
import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer
import org.slf4j.LoggerFactory


/**
  * 执行Flink主类
  */
object ExcuteObject  {

  val LOG = LoggerFactory.getLogger(ExcuteObject.getClass)

  def main(args: Array[String]): Unit = {
    LOG.info("params:{}", args.mkString)

    val mode = "yarn"
//    val mode = "dev"
    val params = ParamsUtil.transParams(args, mode)


    val env = EnvUtil.getStreamExecutionEnvironment(mode, params.checkpointUri, params.hadoopUserName)

    import de.javakaffee.kryoserializers.UnmodifiableCollectionsSerializer
    val unmodColl = Class.forName("java.util.Collections$UnmodifiableCollection")
    env.getConfig.addDefaultKryoSerializer(unmodColl, classOf[UnmodifiableCo llectionsSerializer])

    // 设置kafka配置信息
    val properties = new Properties
    properties.setProperty("max.partition.fetch.bytes", "90485760")
    properties.setProperty("fetch.max.wait.ms","2000")
//    properties.setProperty("max.poll.records","5000")
//    properties.setProperty("fetch.max.bytes","52428800")
    properties.setProperty("fetch.min.bytes","3200000")
    properties.setProperty("bootstrap.servers", params.brokers)
    properties.setProperty("group.id", params.groupid)
    properties.setProperty("flink.partition-discovery.interval-millis", "10000")

    // flink消费kafka数据
    val consumer = new FlinkKafkaConsumer[String](params.sourceTopic, new SimpleStringSchema, properties)
    params.offsetreset.toLowerCase match {
      case "largest" => consumer.setStartFromLatest()
      case "smallest" => consumer.setStartFromEarliest()
      case "groupoffsets" => consumer.setStartFromGroupOffsets()
      case _ => consumer.setStartFromLatest()
    }
    import org.apache.flink.streaming.api.scala._
    val stream = env.addSource(consumer).setParallelism(params.pp.toInt)

    import org.apache.flink.streaming.api.scala._
    // 处理数据格式
    if ("test".equalsIgnoreCase(params.runMode)){
      stream.filter(rowMap => rowMap != null).flatMap(row => RawKafkaUtil.calcSpeed(row))
    }else{
      val message = stream.filter(rowMap => rowMap != null).setParallelism(params.ep.toInt).map(row => ParserMap.parserStrGet(row)).setParallelism(params.ep.toInt)

      //    LOG.info(message.print().toString)

      // 创建clickhouseSink连接
      val clickhouseSink = new ClickHouseJDBCSinkScala(params.url,params.schemaName,params.tableName,params.user,params.password,Integer.valueOf(params.batchSize),Integer.valueOf(params.batchInterval))

      message.addSink(clickhouseSink).setParallelism(params.ep.toInt)
    }
    env.execute("log-to-ch")


  }
}
