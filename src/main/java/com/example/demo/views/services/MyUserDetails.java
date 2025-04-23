package com.example.demo.views.services;

import com.example.demo.views.repositories.database_entities.AuthorizationData;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class MyUserDetails implements UserDetails {
    private AuthorizationData authorizationData;
    public MyUserDetails(AuthorizationData authorizationData) {
        this.authorizationData=authorizationData;
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String role = authorizationData.getRole();
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()));
    }
    @Override
    public String getPassword() {
        return authorizationData.getPassword();
    }

    @Override
    public String getUsername() {
        return authorizationData.getName();
    }

    public String getRole(){
        return authorizationData.getRole();
    }

    public UUID getId() {
        return authorizationData.getId();
    }
}
