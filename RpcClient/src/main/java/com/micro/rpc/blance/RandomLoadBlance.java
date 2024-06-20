package com.micro.rpc.blance;

import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Component;

@Component(value="random")
public class RandomLoadBlance implements LoadBlance{
	@Override
    public String select (List<String> lists){
        int len=lists.size();
        Random random=new Random();
        return lists.get(random.nextInt(len));
    }
}
