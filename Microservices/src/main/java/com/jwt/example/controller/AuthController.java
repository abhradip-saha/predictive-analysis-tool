package com.jwt.example.controller;

import com.jwt.example.models.JwtRequest;
import com.jwt.example.models.JwtResponse;
import com.jwt.example.models.JwtUser;
import com.jwt.example.models.User;
import com.jwt.example.security.JwtHelper;
import com.jwt.example.services.UserSaveService;
import com.jwt.example.services.UserFetchService;
import com.jwt.example.services.UserDelService;
import com.jwt.example.services.JwtService;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private UserSaveService dataSaveService;

    @Autowired
    private UserFetchService dataFetchService;

    @Autowired
    private UserDelService dataDelService;

    @Autowired
    private JwtHelper helper;

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest request) {

        this.doAuthenticate(request.getEmail(), request.getPassword());


        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
        String token = this.helper.generateToken(userDetails);

        JwtResponse response = JwtResponse.builder()
                .jwtToken(token)
                .username(userDetails.getUsername()).build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private void doAuthenticate(String email, String password) {
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(email, password);
        try {
            manager.authenticate(authentication);
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException(" Invalid Username or Password  !!");
        }
    }

    @ExceptionHandler(BadCredentialsException.class)
    public String exceptionHandler() {
        return "Credentials Invalid !!";
    }


    @PostMapping("/create-user")
    public JwtUser createUser (@RequestBody JwtUser user){
        return jwtService.createUser(user);
    }

    

    @PostMapping("/push-redis")
    public ResponseEntity<String> saveData(@RequestBody User data){
        boolean result = dataSaveService.saveData(data);
        if(result)
            return ResponseEntity.ok("Data Pushed To Redis");
        else
            return ResponseEntity.status(HttpStatus.valueOf(0)).build();
    }

    @GetMapping("/fetch-redis")
    public ResponseEntity<List<User>> fetchAllUser(HttpServletResponse response) throws IOException {
        List<User> data = dataFetchService.fetchAllUser();
       
        dataDelService.deleteAllUser();
       
        return ResponseEntity.ok(data);
    }

}
