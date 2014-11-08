package com.baidu.harry.core;

import io.netty.buffer.ByteBuf;

/**
 * Created with IntelliJ IDEA.
 * User: chenyehui
 * Date: 14-11-6
 * Time: 下午3:22
 * To change this template use File | Settings | File Templates.
 */
public class Util {

    public static String parse(ByteBuf byteBuf){
        byte[] bytes = new byte[byteBuf.writableBytes()];
        byteBuf.readBytes(bytes);
        try {
            return new String(bytes, "UTF-8");
        } catch (Exception e){

        }
        return "";
    }
}
