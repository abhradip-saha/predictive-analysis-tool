package com.jwt.example.repositories;
import org.springframework.data.jpa.repository.JpaRepository;

import com.jwt.example.models.JwtUser;

import java.util.Optional;

public interface JwtRepository extends JpaRepository<JwtUser,String>{

    public Optional<JwtUser> findByEmail(String email);
       
}
