package com.micro.rpc.proxy;

import java.lang.reflect.Proxy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.micro.rpc.discovery.IServiceDiscovery;
import com.micro.rpc.server.IServer;
import com.micro.utils.SpringContentUtils;

@Component
public class RpcProxy {
	@Value("${rpc}")
	private String rpc;
	
	@Value("${registercentertype}")
	private String registercentertype;
	
	@Autowired
	private SpringContentUtils springContentUtils;
	
	public <T> T create(Class<?> interfaceClass){
		IServiceDiscovery discovery=(IServiceDiscovery) springContentUtils.getBean(registercentertype);
		IServer server=(IServer) springContentUtils.getBean(rpc);
		
        RpcInvocationHandler proxy=new RpcInvocationHandler(discovery,server);
        
        T result=(T)Proxy.newProxyInstance(
            interfaceClass.getClassLoader(),
            new Class[]{interfaceClass},
            proxy);
        
        return result;
    }
}
