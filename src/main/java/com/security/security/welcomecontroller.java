package com.security.security;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.http.HttpRequest;

@RestController
public class welcomecontroller {
    @GetMapping("")
    public String  greet(){
        return "welcome to our secured webpage";
    }

    @GetMapping("/token")
    public CsrfToken gettoken(HttpServletRequest request){
        return (CsrfToken) request.getAttribute("_csrf");
    }
    //before going going to any orther api requst first goes to dispatch controller which decides which api to forward


}
