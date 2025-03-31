package com.jwt.example.services;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jwt.example.models.User;
import com.jwt.example.repositories.UserDelRepo;

@Service
public class UserDelServiceImpl implements UserDelService {

    @Autowired
    private UserDelRepo dataRepo;

    @Override
    public void deleteAllUser() {
        dataRepo.deleteAllUser();
    }
}
