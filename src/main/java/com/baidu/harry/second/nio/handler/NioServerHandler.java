package com.baidu.harry.second.nio.handler;

import org.apache.commons.lang.StringUtils;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: chenyehui
 * Date: 14-11-5
 * Time: 上午11:34
 * To change this template use File | Settings | File Templates.
 */
public class NioServerHandler implements Runnable {

    private Selector selector;

    private ServerSocketChannel channel;

    private volatile boolean stop;

    public NioServerHandler(int port) {
        try {
            selector = Selector.open();
            channel = ServerSocketChannel.open();
            channel.configureBlocking(false);
            channel.socket().bind(new InetSocketAddress(port), 1024);
            channel.register(selector, SelectionKey.OP_ACCEPT);

            System.out.println("server start...");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void stop() {
        this.stop = true;
    }


    @Override
    public void run() {
        while (!stop) {
            try {
                selector.select(1000);   // selector 休眠时间 1s，无论是否有读写事件发生，selector每隔1s被唤醒一次
                Set<SelectionKey> keys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = keys.iterator();
                SelectionKey key = null;
                while (iterator.hasNext()) {
                    key = iterator.next();
                    iterator.remove();
                    handleInput(key);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void handleInput(SelectionKey key) throws Exception {
        if (key.isValid()) {
            if (key.isAcceptable()) {
                ServerSocketChannel channel1 = (ServerSocketChannel) key.channel();
                SocketChannel socketChannel = channel1.accept();
                socketChannel.configureBlocking(false);
                socketChannel.register(selector, SelectionKey.OP_READ);
            }
            if (key.isReadable()) {
                SocketChannel socketChannel = (SocketChannel) key.channel();
                ByteBuffer readBuffer = ByteBuffer.allocate(1024);
                int count = socketChannel.read(readBuffer);
                if (count > 0) {
                    readBuffer.flip();
                    byte[] bytes = new byte[readBuffer.remaining()];
                    readBuffer.get(bytes);
                    String body = new String(bytes, "UTF-8");

                    System.out.println("Server Receive " + body);
                    String current = body.equals("QUERYTIME") ? new Date().toString() : "BAD REQUEST";
                    write(socketChannel, current);
                } else if (count < 0) {
                    key.cancel();
                    socketChannel.close();
                }
            }
        }
    }

    private void write(SocketChannel channel, String res) throws Exception {
        if (!StringUtils.isEmpty(res)) {
            System.out.println("write to client " + res);
            byte[] bytes = res.getBytes();
            ByteBuffer buffer = ByteBuffer.allocate(bytes.length);
            buffer.put(bytes);
            buffer.flip();          // 这个方法用来将缓冲区准备为数据传出状态,执行以上方法后,输出通道会从数据的开头而不是末尾开始.回绕保持缓冲区中的数据不变,只是准备写入而不是读取.

            channel.write(buffer);
        }
    }
}
