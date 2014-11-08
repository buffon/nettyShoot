package com.baidu.harry.second.nio;

import com.baidu.harry.second.nio.handler.NioServerHandler;

/**
 * Created with IntelliJ IDEA.
 * User: chenyehui
 * Date: 14-11-5
 * Time: 上午11:34
 * To change this template use File | Settings | File Templates.
 */
public class NioTimeServer {

    public static void main(String[] args) {
        new Thread(new NioServerHandler(8082), "Thread-server").start();
    }
}
