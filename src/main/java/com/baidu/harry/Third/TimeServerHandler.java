package com.baidu.harry.Third;

import com.baidu.harry.core.Util;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: chenyehui
 * Date: 14-11-5
 * Time: 下午3:50
 * To change this template use File | Settings | File Templates.
 */
public class TimeServerHandler extends ChannelHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("receive " + Util.parse((ByteBuf) msg));
        System.out.println("send 2 client.");
        ctx.write(Unpooled.copiedBuffer(new Date().toString().getBytes()));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelReadComplete");
        ctx.flush();
    }
}
