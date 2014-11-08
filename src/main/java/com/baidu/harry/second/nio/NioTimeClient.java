package com.baidu.harry.second.nio;

import com.baidu.harry.second.nio.handler.NioClientHandler;

/**
 * Created with IntelliJ IDEA.
 * User: chenyehui
 * Date: 14-11-5
 * Time: 下午2:31
 * To change this template use File | Settings | File Templates.
 */
public class NioTimeClient {

    public static void main(String[] args) {
        new Thread(new NioClientHandler("127.0.0.1", 8082), "TimeClient-001").start();
    }
}
