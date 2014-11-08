package com.baidu.harry.second.mocknio;

import java.io.*;
import java.net.Socket;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: chenyehui
 * Date: 14-11-5
 * Time: 上午10:35
 * To change this template use File | Settings | File Templates.
 */
public class TimeServerHandler implements Runnable {

    private Socket socket;

    public TimeServerHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        BufferedReader br = null;
        PrintWriter pw = null;

        try {
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);

            String word = null;
            String currentTime = null;
            while ((word = br.readLine()) != null) {
                System.out.println("Server receive " + word);
                currentTime = word.equals("QUERYTIME") ? new Date().toString() : "BAD TIME";
                pw.println(currentTime);
            }
        } catch (Exception e) {
            try {
                br.close();
                pw.close();
                socket.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }

    }
}
