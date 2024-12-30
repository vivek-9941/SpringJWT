package com.security.security.config;

import com.security.security.JWT.Jwtservice;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.hibernate.annotations.Filter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
@Component
public class Jwtfilter extends OncePerRequestFilter {
    @Autowired
    UserDetailsService userDetailsService;
    @Autowired
    Jwtservice jwtservice;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //extracting the token
        String token = extracttoken(request);
        if(token!= null){
            try{
                // Extract username from token and validate
                String username = jwtservice.extractusername(token);
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                if(username != null && authentication == null){//do athentication
                    UserDetails UserDetails = userDetailsService.loadUserByUsername(username);
                    if(jwtservice.isTokenValid(token, UserDetails)){
                        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(UserDetails,null,UserDetails.getAuthorities());
                       usernamePasswordAuthenticationToken.setDetails(
                               new WebAuthenticationDetailsSource().buildDetails(request)
                       );
                    }

                }
                else{//authenticated
                    filterChain.doFilter(request,response);
                }
            }
            catch (Exception e){

            }
        }
        else{
        filterChain.doFilter(request,response);
        }
    }

    private String extracttoken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if(header!= null && header.startsWith("Bearer ")){
            return header.substring(7);
        }
        return null;
    }
}
