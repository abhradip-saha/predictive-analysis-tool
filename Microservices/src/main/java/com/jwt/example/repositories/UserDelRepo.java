package com.jwt.example.repositories;

import org.springframework.stereotype.Repository;

@Repository
public interface UserDelRepo {
    void deleteAllUser();
}