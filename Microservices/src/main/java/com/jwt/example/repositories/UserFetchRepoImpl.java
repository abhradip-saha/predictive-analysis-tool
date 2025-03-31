package com.jwt.example.repositories;
import com.jwt.example.models.User;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserFetchRepoImpl implements UserFetchRepo{
    
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public List<User> fetchAllUser() {
        List<User> nics;
        nics=redisTemplate.opsForHash().values("KEY");
        return nics;
    }
}
