package com.micro.rpc.discovery;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.micro.rpc.blance.LoadBlance;
import com.micro.utils.SpringContentUtils;

@Component(value="zookeeper")
public class ZookeeperServiceDiscovery implements IServiceDiscovery{
	private List<String> lists=new ArrayList<>();
    private CuratorFramework cf;
    
    @Value("${host}")
    private String host;
    
    @Value("${blance}")
    private String blance;
    
    @Autowired
    private SpringContentUtils springContentUtils;
    
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
    public String discover(String serviceName){
    	try{    		
    		String path="/myrpc/"+serviceName;
    		lists=cf.getChildren().forPath(path);
    		
    		//监听节点
    		watch(path);
    		
    		//通过负载算法选择一个地址返回
    		Object obj=springContentUtils.getBean(blance);
    		if(obj!=null){        	
    			LoadBlance lb=(LoadBlance) obj;
    			return lb.select(lists);
    		}else{
    			return null;
    		}
    	}catch(Exception e){
    		throw new RuntimeException(e.getMessage());
    	}
    }
    
    private void watch(String path) throws Exception{
        PathChildrenCache pcc=new PathChildrenCache(cf,path,true);
        //定义一个监听器
        PathChildrenCacheListener listener=new PathChildrenCacheListener(){

			@Override
			public void childEvent(CuratorFramework curator, PathChildrenCacheEvent arg1) throws Exception {
				//底层已经帮自动再监听了（递归）
                //再一次获得所有节点
                lists=curator.getChildren().forPath(path);
			}
        };
        
        //注册监听器
        pcc.getListenable().addListener(listener);
        //开始
        pcc.start();
    }

}
