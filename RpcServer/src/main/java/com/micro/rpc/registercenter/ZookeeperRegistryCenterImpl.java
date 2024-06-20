package com.micro.rpc.registercenter;

import javax.annotation.PostConstruct;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component(value="zookeeper")
public class ZookeeperRegistryCenterImpl implements IRegisterCenter{
	@Value("${host}")
	private String host;
	
	private CuratorFramework cf;
	
	@PostConstruct
    public void init(){
    	cf=CuratorFrameworkFactory.builder()
        .connectString(host)
        .sessionTimeoutMs(4000)
        .retryPolicy(new ExponentialBackoffRetry(1000,10))
        .build();
        
        cf.start();
    }
    
	@Override
	public void register(String serviceName, String serviceAddress) {
		try{			
			String path="/myrpc/"+serviceName;
			//1.创建根节点（持久化节点）
			if(cf.checkExists().forPath(path)==null){
				cf.create()
				.creatingParentsIfNeeded()
				.withMode(CreateMode.PERSISTENT) //持久化节点
				.forPath(path,"0".getBytes());
			}
			
			//2.创建子节点（临时节点）
			String childPath=path+"/"+serviceAddress;
			cf.create()
			.withMode(CreateMode.EPHEMERAL)
			.forPath(childPath,"0".getBytes());
			
		}catch(Exception e){
			throw new RuntimeException(e.getMessage());
		}
	}

}
