package com.security.security.userservice;

import com.security.security.JWT.Jwtservice;
import com.security.security.entity.user;
import com.security.security.repo.repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class userservice {
    @Autowired
    private repository repo;
//save password in encryptted form
    private final AuthenticationManager authenticationManager;//this is manual inject of object
    @Autowired
    private PasswordEncoder passwordEncoder;//this is autowired injection of object
    private final Jwtservice jwtservice;
    public userservice(AuthenticationManager authenticationManager, Jwtservice jwtservice) {
        this.authenticationManager = authenticationManager;
        this.jwtservice = jwtservice;
    }

    public user saveusr(user u) {
        u.setPassword(passwordEncoder.encode(u.getPassword()));
        return repo.save(u);
    }

    public  user getuser(String userName){
      return  repo.findByuserName(userName);
    }


    public String validateusr(user u) {
        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        u.getUserName(), u.getPassword()
                )
        );
        if(authenticate.isAuthenticated()){
        //after login send the token
            return jwtservice.generatetoken(u);
        }
        return "Fail";

    }
}
