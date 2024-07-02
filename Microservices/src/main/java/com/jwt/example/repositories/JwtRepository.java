package com.jwt.example.repositories;
import com.jwt.example.entities.JwtUser;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface JwtRepository extends JpaRepository<JwtUser,String>{

    public Optional<JwtUser> findByEmail(String email);
       
}
