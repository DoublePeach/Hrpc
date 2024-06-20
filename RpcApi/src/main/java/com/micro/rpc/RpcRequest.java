package com.micro.rpc;

import java.io.Serializable;

import lombok.Data;

//
@Data
public class RpcRequest implements Serializable{
	private String className;//类名称
    private String methodName;//方法名称
    private Class[] types;//参数类型
    private Object[] params;//参数
}
