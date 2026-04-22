package com.example.ProjectManagementSystem.service;

import com.example.ProjectManagementSystem.config.JwtConstant;
import com.example.ProjectManagementSystem.config.JwtProvider;
import com.example.ProjectManagementSystem.model.User;
import com.example.ProjectManagementSystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository userRepository;
    @Override
    public User findUserProfileByJwt(String jwt) throws Exception {
        // 1. Strip the "Bearer " prefix if it exists
        if (jwt != null && jwt.startsWith("Bearer ")) {
            jwt = jwt.substring(7);
        }

        // 2. Now pass the clean token to the provider
        String email = JwtProvider.getEmailFromToken(jwt);
        return findUserByEmail(email);
    }

    @Override
    public User findUserByEmail(String email) throws Exception {
        User user = userRepository.findByEmail(email);
        if(user==null){
            throw new Exception("user not found");
        }
        return user;
    }

    @Override
    public User findUserById(Long userId) throws Exception {

        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()){
            throw new Exception("user not found");
        }
        return optionalUser.get();
    }

    @Override
    public User updateUserProjectSize(User user, int number) {
        user.setProjectSize(user.getProjectSize()+number);

        return userRepository.save(user);
    }
}
