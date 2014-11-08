package com.baidu.harry.second.mocknio;

import java.io.*;
import java.net.Socket;

/**
 * Created with IntelliJ IDEA.
 * User: chenyehui
 * Date: 14-11-5
 * Time: 上午11:15
 * To change this template use File | Settings | File Templates.
 */
public class TimeClient {

    private static final Integer port = 8082;

    public static void main(String[] args) throws Exception {
        Socket socket = new Socket("127.0.0.1", port);

        System.out.println("client start");

        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);

        writer.println("QUERYTIME1");
        String res = reader.readLine();
        System.out.println("client receive:" + res);

        reader.close();
        writer.close();
        socket.close();
    }
}
