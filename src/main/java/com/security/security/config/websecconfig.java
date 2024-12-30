package com.security.security.config;

import jakarta.servlet.Filter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class websecconfig {
    @Autowired
    private Jwtfilter jwtfilter;
    private final UserDetailsService userDetailsService;

    public websecconfig(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
          return  httpSecurity
//                  to bypass registration from security
                  .authorizeHttpRequests(request-> request
                                .requestMatchers("/register","/login").permitAll()
                                .anyRequest().authenticated()
                  )

//                  .authorizeHttpRequests( request -> request.anyRequest().authenticated())
                  .httpBasic(Customizer.withDefaults())
                  .csrf(csrf -> csrf.disable())
                  .sessionManagement(Session -> Session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                  .addFilterBefore(jwtfilter, UsernamePasswordAuthenticationFilter.class)
                  .build();



    }

//    @Bean
//    public UserDetailsService userDetailsService(){
//        UserDetails userone
//                = User.withUsername("vivek")
//                .password("{noop}pass")
//
//                .roles("USER")
//                .build();
////but as a producion grade application we should not use hardcoded values we should use registration and login and save it in DB
//        UserDetails nikhil
//                = User.withUsername("nikhil")
//                .password("{noop}pass1")//using noop i.e no encoding to be  done else it gives errror
//                .roles("USER")
//                .build();
//        //this InMemoryUserDetailsManager contains a constructor which accpts all the users
//
//        return new InMemoryUserDetailsManager(userone,nikhil);
//    }


    //this provides the object of user from db
    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

//authenticationManager handles the login i.e data coming from client and sends to authentication provider
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
        //this authenticationManager needs to be injected in  usercontroller

    }
}
