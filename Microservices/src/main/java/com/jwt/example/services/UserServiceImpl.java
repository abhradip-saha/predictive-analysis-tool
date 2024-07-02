package com.jwt.example.services;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jwt.example.models.User;
import com.jwt.example.repositories.UserRepo;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepo dataRepo;

    @Override
    public boolean saveData(User data){
        return dataRepo.saveData(data);
    }

    @Override
    public List<User> fetchAllUser() {
        return dataRepo.fetchAllUser();
    }

    @Override
    public void deleteAllUser() {
        dataRepo.deleteAllUser();
    }
}
