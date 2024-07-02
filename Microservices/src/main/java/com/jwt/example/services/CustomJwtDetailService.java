package com.jwt.example.services;

import com.jwt.example.entities.JwtUser;
import com.jwt.example.repositories.JwtRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomJwtDetailService implements UserDetailsService {

    @Autowired
    private JwtRepository jwtRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        JwtUser user=  jwtRepository.findByEmail(username).orElseThrow(()->  new RuntimeException("User not found!!"));
        return user;

    }
}
