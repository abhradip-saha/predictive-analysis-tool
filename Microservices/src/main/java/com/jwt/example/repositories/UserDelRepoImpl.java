package com.jwt.example.repositories;
import com.jwt.example.models.User;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserDelRepoImpl implements UserDelRepo{
    
    @Autowired
    private RedisTemplate redisTemplate;
    @Override
    public void deleteAllUser() {
        redisTemplate.getConnectionFactory().getConnection().flushDb();
    }
}
