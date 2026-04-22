package com.example.ProjectManagementSystem.service;

import com.example.ProjectManagementSystem.model.User;
import com.example.ProjectManagementSystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;

@Service
public class CustomUserDetailsImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        // username = email (as you decided)
        User user = userRepository.findByEmail(username);

        if (user == null) {
            throw new UsernameNotFoundException(
                    "User not found with email: " + username
            );
        }

        // Authorities / Roles
        List<GrantedAuthority> authorities = new ArrayList<>();
//        authorities.add(new SimpleGrantedAuthority("ROLE_USER")); still don't want roles

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),      // username
                user.getPassword(),   // password
                authorities
        );
    }
}
