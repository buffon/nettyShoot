package com.baidu.harry.second.nio.handler;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: chenyehui
 * Date: 14-11-5
 * Time: 下午1:38
 * To change this template use File | Settings | File Templates.
 */
public class NioClientHandler implements Runnable {

    private String host;
    private int port;
    private Selector selector;
    private SocketChannel socketChannel;
    private volatile boolean stop = false;

    public NioClientHandler(String host, int port) {
        this.host = host;
        this.port = port;
        try {
            selector = Selector.open();
            socketChannel = SocketChannel.open();
            socketChannel.configureBlocking(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void run() {
        System.out.println("client start ...");
        try {
            doConnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
            SocketChannel socketChannel = (SocketChannel) key.channel();
            if (key.isAcceptable()) {
                if (socketChannel.finishConnect()) {
                    System.out.println("register read ...");
                    socketChannel.register(selector, SelectionKey.OP_READ);
                } else {

                }
            }
            if (key.isReadable()) {
                ByteBuffer readBuffer = ByteBuffer.allocate(1024);
                int count = socketChannel.read(readBuffer);
                if (count > 0) {
                    readBuffer.flip();
                    byte[] bytes = new byte[readBuffer.remaining()];
                    readBuffer.get(bytes);
                    String body = new String(bytes, "UTF-8");
                    System.out.println("Client receive " + body);
                    this.stop = true;
                } else if (count < 0) {
                    key.cancel();
                    socketChannel.close();
                }
            }
        }
    }

    private void doConnect() throws Exception {
        if (socketChannel.connect(new InetSocketAddress(host, port))) {
            System.out.println("read...");
            socketChannel.register(selector, SelectionKey.OP_READ);
            write(socketChannel);
        } else {
            System.out.println("connect...");
            socketChannel.register(selector, SelectionKey.OP_CONNECT);
        }
    }

    private void write(SocketChannel sc) throws Exception {
        byte[] req = "QUERYTIME".getBytes();
        ByteBuffer buffer = ByteBuffer.allocate(req.length);
        buffer.put(req);
        buffer.flip();
        sc.write(buffer);
        if (!buffer.hasRemaining()) {
            System.out.println("send success");
        }
    }
}
