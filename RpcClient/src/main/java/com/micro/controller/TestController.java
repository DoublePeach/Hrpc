package com.micro.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.micro.rpc.proxy.RpcProxy;
import com.micro.service.ITestService;

@RestController
@RequestMapping("/test")
public class TestController {
	@Autowired
	private RpcProxy rpcProxy;

	@RequestMapping("/sayHello")
	public String sayHello(String name){
		ITestService testService=rpcProxy.create(ITestService.class);
		return testService.sayHello(name);
	}
}
