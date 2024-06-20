package com.micro.rpc.server;

import com.micro.rpc.RpcResponse;

public interface IServer {

	public RpcResponse send(String host,int port,Object args);
}
