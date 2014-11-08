package com.baidu.harry.rpc.core.service.impl;


import com.baidu.harry.rpc.core.EndPoint;
import com.baidu.harry.rpc.core.service.HelloService;

@EndPoint("helloService")
public class HelloServiceImpl implements HelloService {
	public String hello(String name) {
		return "Hello " + name;
	}

}
