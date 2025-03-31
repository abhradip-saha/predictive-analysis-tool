package com.jwt.example.repositories;
import com.jwt.example.models.User;

import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public interface UserFetchRepo {
    List<User> fetchAllUser();
}