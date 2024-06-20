package com.micro.service.impl;

import com.micro.rpc.anno.Rpc;
import com.micro.service.ITestService;

@Rpc(interfaceClass=ITestService.class)
public class TestServiceImpl implements ITestService{

	@Override
	public String sayHello(String name) {
		
		return "你好,"+name+"!!";
	}

}
