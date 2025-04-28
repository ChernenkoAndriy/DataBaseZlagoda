package com.example.demo.services;

import com.example.demo.repositories.database_entities.AuthorizationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
@Service
public class MyUserDetailsService implements UserDetailsService {
    @Autowired
    private AuthorizationService authorizationService;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AuthorizationData authorizationData = authorizationService.getUserByLogin(username);
        if(authorizationData == null){
            throw new UsernameNotFoundException(username);
        }
        return new MyUserDetails(authorizationData);
    }

    public static MyUserDetails getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof MyUserDetails) {
            return (MyUserDetails) auth.getPrincipal();
        }
        return null;
    }

}
