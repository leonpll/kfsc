package com.pactera.tools

import org.apache.flink.contrib.streaming.state.RocksDBStateBackend
import org.apache.flink.streaming.api.environment.CheckpointConfig
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.slf4j.LoggerFactory


/**
  * 获取Flink执行环境
  */
object EnvUtil {

  private val LOG = LoggerFactory.getLogger(EnvUtil.getClass)

  def getStreamExecutionEnvironment(mode: String, checkpointUri: String,hadoopUserName:String): StreamExecutionEnvironment = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    env.enableCheckpointing(5000)
    env.getCheckpointConfig.setCheckpointInterval(5000)
    if ("yarn".equalsIgnoreCase(mode)) {
      System.setProperty("user.name", hadoopUserName)
      System.setProperty("HADOOP_USER_NAME", hadoopUserName)
      try { // hdfs backend
        // env.setStateBackend(new FsStateBackend(checkpointUri,true))
        // rocksdb backend
        env.setStateBackend(new RocksDBStateBackend(checkpointUri,true))
        // 取消作业时保留检查点文件
        env.getCheckpointConfig.enableExternalizedCheckpoints(CheckpointConfig.ExternalizedCheckpointCleanup.RETAIN_ON_CANCELLATION)
      } catch {
        case e: Exception =>
          LOG.error("set checkpoint error:{}" + e)
      }
    }
    env
  }
}