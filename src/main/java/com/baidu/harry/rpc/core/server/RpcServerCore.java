package com.baidu.harry.rpc.core.server;

import com.baidu.harry.rpc.core.EndPoint;
import com.baidu.harry.rpc.core.service.impl.HelloServiceImpl;
import org.apache.commons.lang.StringUtils;

import java.io.*;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: chenyehui
 * Date: 14-11-7
 * Time: 下午12:50
 * To change this template use File | Settings | File Templates.
 */
public class RpcServerCore {

    private static Map<String, Object> services = new HashMap<String, Object>();

    public static void main(String[] args) throws Exception {
        scanClassPath(new File(System.getProperty("user.dir") + "/target/classes"));
        export(8087);
    }

    public static Map<String, Object> getServices() {
        return services;
    }

    private static void scanClassPath(File file) {
        try {
            if (file.isFile()) {
                if (file.getName().endsWith(".class")) {
                    String path = file.getPath();
                    MyClassLoader myClassLoader = new MyClassLoader();
                    Class<?> clazz = myClassLoader.load(path);
                    EndPoint endPoint = (EndPoint) clazz.getAnnotation(EndPoint.class);
                    if (endPoint != null) {
                        String uri = endPoint.value();
                        Object action = clazz.newInstance();
                        services.put(uri, action);
                    }
                }
            } else {
                File[] files = file.listFiles();
                for (File child : files) {
                    scanClassPath(child);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    static class MyClassLoader extends ClassLoader {
        public MyClassLoader() {
            super();
        }

        public Class<?> load(String path) {
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(path);
                byte[] buf = new byte[fis.available()];
                int len = 0;
                int total = 0;
                int fileLength = buf.length;
                while (total < fileLength) {
                    len = fis.read(buf, total, fileLength - total);
                    total = total + len;
                }
                return super.defineClass(null, buf, 0, fileLength);
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    fis = null;
                }
            }
        }
    }

    /**
     * 暴露服务
     *
     * @param port 服务端口
     * @throws Exception
     */
    public static void export(final int port) throws Exception {

        if (port <= 0 || port > 65535) {
            throw new IllegalArgumentException("Invalid port " + port);
        }

        ServerSocket server = new ServerSocket(port);
        for (; ; ) {
            new MsgHandler(server.accept()).start();
        }
    }


}
