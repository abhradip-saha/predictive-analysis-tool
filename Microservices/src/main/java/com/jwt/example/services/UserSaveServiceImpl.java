package com.jwt.example.services;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jwt.example.models.User;
import com.jwt.example.repositories.UserSaveRepo;

@Service
public class UserSaveServiceImpl implements UserSaveService {

    @Autowired
    private UserSaveRepo dataRepo;

    @Override
    public boolean saveData(User data){
        return dataRepo.saveData(data);
    }
}
