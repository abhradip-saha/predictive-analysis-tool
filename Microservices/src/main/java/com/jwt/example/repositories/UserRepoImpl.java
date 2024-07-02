package com.jwt.example.repositories;
import com.jwt.example.models.User;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepoImpl implements UserRepo{
    
    @Autowired
    private RedisTemplate redisTemplate;
   
    @Override
    public boolean saveData(User data){
        try{
            redisTemplate.opsForHash().put("KEY", data.getKey() , data);
            return true;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }
    @Override
    public List<User> fetchAllUser() {
        List<User> nics;
        nics=redisTemplate.opsForHash().values("KEY");
        return nics;
    }
    @Override
    public void deleteAllUser() {
        redisTemplate.getConnectionFactory().getConnection().flushDb();
    }
}
