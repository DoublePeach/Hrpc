package com.micro.rpc.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.micro.utils.SpringContentUtils;

@Component
public class Startup implements CommandLineRunner{
	@Value("${servertype}")
	private String servertype;
	
	@Autowired
	private SpringContentUtils springContentUtils;
	
	@Override
	public void run(String... args) throws Exception {
		Object obj=springContentUtils.getBean(servertype);
		if(obj!=null){
			IServer server=(IServer)obj;
			server.start();
		}
	}

}
