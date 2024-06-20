package com.micro.rpc;

import java.io.Serializable;

import lombok.Data;

@Data
public class RpcResponse implements Serializable{
	private int code;//0成功，1失败
	private String msg;
	private Object data;
	
	@Override
	public String toString() {
		return "RpcResponse [code=" + code + ", msg=" + msg + ", data=" + data + "]";
	}
}
