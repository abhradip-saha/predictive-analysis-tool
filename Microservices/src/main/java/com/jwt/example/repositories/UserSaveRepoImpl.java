package com.jwt.example.repositories;
import com.jwt.example.models.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserSaveRepoImpl implements UserSaveRepo{
    
    @Autowired
    private RedisTemplate redisTemplate;
   
    @Override
    public boolean saveData(User data){
        try{
            redisTemplate.opsForHash().put("KEY", data.getKey() , data);
            // Map<String, String> redisData = new HashMap<>();
            // redisData.put("Demand_date", "2025-03-28");
            // redisData.put("Demand_qty", "28");
            // redisData.put("Key", "abc123");

            // redisTemplate.opsForHash().putAll("KEY", redisData);
            return true;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
