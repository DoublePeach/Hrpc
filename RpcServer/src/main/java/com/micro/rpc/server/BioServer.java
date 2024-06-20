package com.micro.rpc.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.micro.rpc.RpcRequest;
import com.micro.rpc.RpcResponse;
import com.micro.rpc.registercenter.IRegisterCenter;
import com.micro.rpc.scan.ScanPackageUtils;
import com.micro.utils.JsonUtils;
import com.micro.utils.ReflectUtils;
import com.micro.utils.SpringContentUtils;

@Component(value="bio")
public class BioServer implements IServer{
	@Value("${serverport}")
	private int serverport;
	
	@Value("${scanpackage}")
	private String scanpackage;
	
	@Value("${registercentertype}")
	private String registercentertype;
	
	@Autowired
	private SpringContentUtils springContentUtils;
	
	@Override
	public void start() {
		//扫描接口，注册到Zookeeper
		scan();
		
		//启动服务
		init();
	}
	//扫描
	private void scan(){
		try{			
			Object obj=springContentUtils.getBean(registercentertype);
			if(obj!=null){
				IRegisterCenter registerCenter=(IRegisterCenter) obj;
				//扫描
				Map<String,String> map=ScanPackageUtils.scannerClass(scanpackage);
				
				//注册
				if(!CollectionUtils.isEmpty(map)){
					InetAddress addr=InetAddress.getLocalHost();
					String serviceAddress=addr.getHostAddress()+":"+serverport;
					
					for(Map.Entry<String, String> entry : map.entrySet()){
					    String className = entry.getKey();
					    String interfaceName = entry.getValue();
					    String value=className+"&"+serviceAddress;
					    
					    registerCenter.register(interfaceName, value);
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	//启动
	private void init(){
		try{			
			ExecutorService pool = Executors.newCachedThreadPool();
			ServerSocket serverSocket = new ServerSocket(serverport);
			while (true) {
				final Socket socket = serverSocket.accept();//会堵塞
				pool.execute(new Runnable() {
					public void run() {
						handler(socket);
					}
				});
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	//处理客户端业务
	public static void handler(Socket socket) {
		InputStream input = null;
		OutputStream output=null;
		try {
			input=socket.getInputStream();
			byte[] bytes = new byte[1024];
			
			int len = input.read(bytes);//会堵塞
			if (len>0) {
				RpcResponse res=new RpcResponse();
				try{					
					//获取客户端传递过来的参数
					String json=new String(bytes, 0, len);
					RpcRequest bean=JsonUtils.jsonToPojo(json, RpcRequest.class);
					
					//反射执行目标方法
					String className=bean.getClassName();
					String methodName=bean.getMethodName();
					Class<?>[] types=bean.getTypes();
					Object[] args=bean.getParams();
					
					Class clazz=ReflectUtils.getClass(className);
					Method method=ReflectUtils.getMethod(clazz, methodName, types);
					
					Object instance=ReflectUtils.newInstance(clazz);
					Object obj=ReflectUtils.invoke(instance, method, args);
					
					res.setCode(0);
					res.setMsg("执行成功");
					res.setData(obj);
				}catch(Exception e){
					res.setCode(1);
					res.setMsg(e.getMessage());
					res.setData(null);
				}
				
				//响应执行结果
				output=socket.getOutputStream();
				String resJson=JsonUtils.objectToJson(res);
				System.out.println("响应结果："+resJson);
				output.write(resJson.getBytes());
				output.flush();
			}
            
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
