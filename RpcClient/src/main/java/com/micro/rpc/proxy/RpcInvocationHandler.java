package com.micro.rpc.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import com.micro.rpc.RpcRequest;
import com.micro.rpc.RpcResponse;
import com.micro.rpc.discovery.IServiceDiscovery;
import com.micro.rpc.server.IServer;

public class RpcInvocationHandler implements InvocationHandler{
	IServiceDiscovery discovery=null;
	IServer server=null;
	
	RpcInvocationHandler(IServiceDiscovery discovery,IServer server){
		this.discovery=discovery;
		this.server=server;
	}
	
    @Override
    public Object invoke(Object proxy,Method method,Object[] args){
    	//1.去Zk获取服务对应的地址       
    	String url=discovery.discover(method.getDeclaringClass().getName());
    	String[] arrs=url.split("&");
    	String className=arrs[0];
    	String host=arrs[1].split(":")[0];
    	int port=Integer.parseInt(arrs[1].split(":")[1]);
    	
        //2.封装参数
        RpcRequest req=new RpcRequest();
        req.setClassName(className);
        req.setMethodName(method.getName());
        req.setTypes(method.getParameterTypes());
        req.setParams(args);
        
        
        //3.发起远程请求
        RpcResponse res=server.send(host, port, req);
        return res.getData();
    }
}
