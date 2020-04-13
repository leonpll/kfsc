package com.pactera.utils;

/**
 * @program: new-energy-parent
 * @description: 原始数据转换
 * @author: LL
 * @create: 2019-11-01 15:18
 **/
public class MsgParser {

    public static String bytes2HexStr(byte[] array) {
        StringBuffer sb = new StringBuffer(array.length);

        for (int i = 0; i < array.length; ++i) {
            String sTemp = Integer.toHexString(255 & array[i]);
            if (sTemp.length() < 2) {
                sb.append(0);
            }

            sb.append(sTemp.toUpperCase());
        }

        return sb.toString();
    }
}
