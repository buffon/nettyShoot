package com.baidu.harry.second.mocknio;

import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created with IntelliJ IDEA.
 * User: chenyehui
 * Date: 14-11-5
 * Time: 上午10:32
 * To change this template use File | Settings | File Templates.
 */
public class TimeServer {

    private static final Integer port = 8082;

    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = null;

        try {
            serverSocket = new ServerSocket(port);
            TimeServerHandlerExecutePool pool = new TimeServerHandlerExecutePool(50, 10000);
            Socket socket = null;
            while (true) {
                socket = serverSocket.accept();
                pool.execute(new TimeServerHandler(socket));
            }
        } finally {
            if (serverSocket != null) {
                serverSocket.close();
                serverSocket = null;
            }
        }
    }
}
