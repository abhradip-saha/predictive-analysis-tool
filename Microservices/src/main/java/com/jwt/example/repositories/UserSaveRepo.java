package com.jwt.example.repositories;
import com.jwt.example.models.User;

import org.springframework.stereotype.Repository;

@Repository
public interface UserSaveRepo {
    boolean saveData(User data);
}