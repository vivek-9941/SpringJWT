package com.security.security.userservice;
import com.security.security.entity.user;
import com.security.security.repo.repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import java.util.Objects;

@Component
public class customuserdetailservice implements UserDetailsService {
    @Autowired
    private repository repo;
    //to load user from db need repo so it will take user from db and check if the request details are same
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        user  userobj = repo.findByuserName(username);
        if(Objects.isNull(userobj)){
            System.out.println("user not found");
            throw new UsernameNotFoundException("user not found");
        }
        else{
            return new userdetails(userobj);
        }
    }
}
