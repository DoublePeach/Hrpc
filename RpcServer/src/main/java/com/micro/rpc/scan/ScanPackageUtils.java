package com.micro.rpc.scan;

import java.io.File;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.micro.rpc.anno.Rpc;

//过滤注解
public class ScanPackageUtils {
	static Map<String,String> classNames=new HashMap<String, String>();
	
	public static Map<String,String> scannerClass(String packageName){
		try{			
			String name=packageName.replaceAll("\\.","/");
			URL url=ScanPackageUtils.class.getClassLoader().getResource(name);    
			File file=new File(url.getFile());
			File[] files=file.listFiles();
			for(File f:files){
				if(f.isDirectory()){
					scannerClass(packageName+"."+f.getName());
				}else{
					//反射获取注解及接口类
					String classname=packageName+"."+f.getName().replace(".class", "");
					Class clazz=Class.forName(classname);
					Annotation anno=clazz.getAnnotation(Rpc.class);
					if(anno!=null){
						Rpc rpcAnno=(Rpc) anno;
						Class interfaceClass=rpcAnno.interfaceClass();
						classNames.put(classname, interfaceClass.getName());
						
						//String newclassname=packageName+"."+f.getName().replace(".class","");
						//classNames.add(newclassname);
					}
				}
			}
			return classNames;
		}catch(Exception e){
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}
}
