package com.jwt.example.services;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jwt.example.models.User;
import com.jwt.example.repositories.UserFetchRepo;

@Service
public class UserFetchServiceImpl implements UserFetchService {

    @Autowired
    private UserFetchRepo dataRepo;

    @Override
    public List<User> fetchAllUser() {
        return dataRepo.fetchAllUser();
    }
}
