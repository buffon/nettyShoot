package com.baidu.harry.rpc.core;

import com.baidu.harry.rpc.core.catalogy.RandomAccessServerFind;
import com.baidu.harry.rpc.core.service.HelloService;
import com.baidu.harry.zoo.demo.AppClient;
import org.apache.commons.lang3.tuple.MutablePair;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.Socket;

/**
 * RpcFramework
 *
 * @author harry.chen
 */
public class RpcClientCore {

    public static void main(String[] args) throws Exception {
        HelloService service = refer(HelloService.class);
        for (int i = 0; i < 10; i++) {
            String hello = service.hello("World" + i);
            System.out.println(hello);
            Thread.sleep(1000);
        }
    }

    private static AppClient appClient;

    static {
        appClient = new AppClient();
        try {
            appClient.init();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static <T> T refer(final Class<T> interfaceClass) throws Exception {
        RandomAccessServerFind serverFind = new RandomAccessServerFind();
        //根据不同策略去获取对应的server值
        MutablePair<String, Integer> pair = serverFind.getServerHost(appClient.getServerList());
        return refer(interfaceClass, pair.getLeft(), pair.getRight());
    }

    /**
     * 引用服务
     *
     * @param <T>            接口泛型
     * @param interfaceClass 接口类型
     * @param host           服务器主机名
     * @param port           服务器端口
     * @return 远程服务
     * @throws Exception
     *///原理是通过代理，获得服务器端接口的一个“代理”的对象。对这个对象的所有操作都会调用invoke函数，在invoke函数中，是将被调用的函数名，参数列表和参数发送到服务器，并接收服务器处理的结果
    @SuppressWarnings("unchecked")
    public static <T> T refer(final Class<T> interfaceClass, final String host, final int port) throws Exception {
        if (interfaceClass == null) {
            throw new IllegalArgumentException("Interface class == null");
        }
        if (!interfaceClass.isInterface()) {
            throw new IllegalArgumentException("The " + interfaceClass.getName() + " must be interface class!");
        }
        if (host == null || host.length() == 0) {
            throw new IllegalArgumentException("Host == null!");
        }
        if (port <= 0 || port > 65535) {
            throw new IllegalArgumentException("Invalid port " + port);
        }
        System.out.println("Get remote service " + interfaceClass.getName() + " from server " + host + ":" + port);
        return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class<?>[]{interfaceClass},
                new InvocationHandler() {
                    public Object invoke(Object proxy, Method method, Object[] arguments)
                            throws Throwable {
                        Socket socket = new Socket(host, port);
                        ObjectOutputStream output = null;
                        ObjectInputStream input = null;
                        try {
                            output = new ObjectOutputStream(socket.getOutputStream());
                            input = new ObjectInputStream(socket.getInputStream());
                            output.writeUTF(interfaceClass.getName());
                            output.writeUTF(method.getName());
                            output.writeObject(method.getParameterTypes());
                            output.writeObject(arguments);
                            return input.readObject();
                        } finally {
                            output.close();
                            input.close();
                            socket.close();
                        }
                    }
                });
    }

}
