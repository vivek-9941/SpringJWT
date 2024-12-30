package com.security.security;

import com.security.security.entity.user;
import com.security.security.userservice.userservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Usercontroller {
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    private userservice service;




    @PostMapping("/register")
    public ResponseEntity<user> register(@RequestBody user u) {
        return new ResponseEntity<>(service.saveusr(u), HttpStatus.OK);
    }

    @PostMapping("/login")
    public String login(@RequestBody user u) {
//        user u1 = service.getuser(u.getUserName());
//        if (u1 != null && (passwordEncoder.matches(u.getPassword() ,u1.getPassword()) )) {
//            return "Successfully logged in";
//        } else {
//            return "Failure";
//        }
        return service.validateusr(u);
    }

}
