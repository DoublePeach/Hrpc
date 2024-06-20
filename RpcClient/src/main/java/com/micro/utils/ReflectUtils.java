package com.micro.utils;

import java.lang.reflect.Method;

public class ReflectUtils {
	public static Class getClass(String className){
		try {
			return Class.forName(className);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}
	
	public static Method getMethod(Class clazz,String methodName,Class<?>[] types){
		Method method;
		try {
			method = clazz.getClass().getMethod(methodName,types);
			return method;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}
	
	public static Object invoke(Object obj,Method method,Object... args){
		try {
			return method.invoke(obj, args);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}
}
