package com.micro.rpc.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import org.springframework.stereotype.Component;

import com.micro.rpc.RpcResponse;
import com.micro.utils.JsonUtils;

@Component(value="bio")
public class BioServer implements IServer{

	@Override
	public RpcResponse send(String host,int port,Object args) {
		OutputStream os=null;
		InputStream in=null;
		Socket client=null;
		try{
			client=new Socket(host,port);
			//发送
			os=client.getOutputStream();
			os.write(JsonUtils.objectToJson(args).getBytes());
			os.flush();
			
			//响应
			RpcResponse res=new RpcResponse();
			in=client.getInputStream();
			byte[] bytes=new byte[1024];
			int len=0;
			while (true) {
				len=in.read(bytes);
				if(len>0){					
					System.out.println("长度："+len);
					String json=new String(bytes, 0, len);
					res=JsonUtils.jsonToPojo(json, RpcResponse.class);
					break;
				}
            }
			
			System.out.println("客户端接受消息："+res.toString());
			return res;
		}catch(Exception e){
			throw new RuntimeException(e.getMessage());
		}finally{
			if(in!=null){				
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(os!=null){				
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(client!=null){				
				try {
					client.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
