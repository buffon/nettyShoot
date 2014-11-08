package com.baidu.harry.Third;

import com.baidu.harry.core.Util;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.nio.ByteBuffer;

/**
 * Created with IntelliJ IDEA.
 * User: chenyehui
 * Date: 14-11-5
 * Time: 下午3:59
 * To change this template use File | Settings | File Templates.
 */
public class TimeClientHandler extends ChannelHandlerAdapter {

    private final ByteBuf fm;

    public TimeClientHandler() {
        byte[] req = "QUERTTIME".getBytes();
        fm = Unpooled.buffer(req.length);
        fm.writeBytes(req);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("send to server ...");
        ctx.writeAndFlush(fm);
    }



    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("channelRead");
        System.out.println("Receive " + Util.parse((ByteBuf) msg));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println(cause.getMessage());
        ctx.close();
    }
}
