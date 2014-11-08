package com.baidu.harry.core;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * Created with IntelliJ IDEA.
 * User: chenyehui
 * Date: 14-11-4
 * Time: 下午9:37
 * To change this template use File | Settings | File Templates.
 */
public class HttpFileServer {

    public void run(final int port, final String url) throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class).childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel sc) throws Exception {
                    sc.pipeline().addLast("http-decoder", new HttpRequestDecoder());
                    sc.pipeline().addLast("http-aggregator", new HttpObjectAggregator(65536));
                    sc.pipeline().addLast("http-encoder", new HttpResponseEncoder());
                    sc.pipeline().addLast("http-chunked", new ChunkedWriteHandler());
                    sc.pipeline().addLast("fileServerHandler", new HttpFileServerHandler(url));
                }
            });

            ChannelFuture cf = b.bind("", port).sync();
            System.out.println("start server ...");
            cf.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
