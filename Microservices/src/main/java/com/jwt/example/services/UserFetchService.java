package com.jwt.example.services;
import com.jwt.example.models.User;
import java.util.List;
public interface UserFetchService {
    List<User> fetchAllUser();
}