package com.pactera.tools

import java.io.IOException

import org.apache.flink.api.common.serialization.AbstractDeserializationSchema

/**
  * 消息
  * @tparam T
  */
class ByteArrayDeserializationSchema[T] extends  AbstractDeserializationSchema[Array[Byte]]{

  @throws[IOException]
  override def deserialize(message: Array[Byte]): Array[Byte] = message
}
