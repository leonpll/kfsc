package com.pactera

import java.text.SimpleDateFormat
import java.util
import java.util.Date

import com.navinfo.wecloud.ausiaev.forward.common.iot.common.CommonPacket
import com.navinfo.wecloud.ausiaev.forward.common.iot.util.{ByteArrayUtil, Packet2ObjUtil}
import com.pactera.utils.MsgParser
import org.apache.flink.shaded.netty4.io.netty.buffer.Unpooled

object ParserMap {


  /**
    * @Description: 解析消息封装map格式
    *               * @param String
    * @Author: LL
    * @Date: 2019/10/16  10:05
    */
  def parserStr(msgStr: Array[Byte]): java.util.Map[String, Any] = {


    val returnMap = new util.HashMap[String, Any](300)

    val comPacket: CommonPacket = Packet2ObjUtil.bytes2commonPacket(msgStr);


    // 车辆唯一标识
    val deviceId: String = comPacket.getDeviceId
    returnMap.put("device_id", deviceId)

    // 车辆上报时间
    val byteBuf = Unpooled.wrappedBuffer(comPacket.getDataUnit)
    val collectTime = new Array[Byte](6)
    byteBuf.readBytes(collectTime)


    val timeStr = ByteArrayUtil.bytes2timeStr(collectTime) // yyyyMMddHHmmss
    val timeC  = new SimpleDateFormat("yyyyMMddHHmmss").parse(timeStr)

    val colTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(timeC)
    val colDate = new SimpleDateFormat("yyyy-MM-dd").format(timeC)
    val createTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())

    returnMap.put("collect_time", colTime)
    returnMap.put("collect_date", colDate)
    returnMap.put("create_time", createTime)

    // 放入元数据
    returnMap.put("msg", MsgParser.bytes2HexStr(msgStr))

    returnMap
  }


}
