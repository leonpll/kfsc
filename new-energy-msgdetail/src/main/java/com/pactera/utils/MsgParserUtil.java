package com.pactera.utils;

import com.alibaba.fastjson.JSONObject;
import com.pactera.KafkaMessage;
import org.apache.log4j.Logger;

/**
 * @program: server-code
 * @description: 消息解析工具类
 * @author: LL
 * @create: 2019-10-12 10:50
 **/
public class MsgParserUtil {
    private static final Logger log = Logger.getRootLogger();

    public  static KafkaMessage pasMsg(String msg){

        return JSONObject.parseObject(msg, KafkaMessage.class);
    }


}
