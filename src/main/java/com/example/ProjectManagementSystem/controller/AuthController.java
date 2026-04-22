package com.example.ProjectManagementSystem.controller;

import com.example.ProjectManagementSystem.config.JwtProvider;
import com.example.ProjectManagementSystem.config.request.LoginRequest;
import com.example.ProjectManagementSystem.repository.UserRepository;
import com.example.ProjectManagementSystem.response.AuthResponse;
import com.example.ProjectManagementSystem.service.CustomUserDetailsImpl;
//import com.example.ProjectManagementSystem.service.SubscriptionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.example.ProjectManagementSystem.model.User;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "01 -Auth", description = "Authentication APIs")
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CustomUserDetailsImpl customUserDetails;

//    @Autowired
//    private SubscriptionService subscriptionService;
    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> createUserHandler(@RequestBody User user) throws Exception {
       User isUserExist = userRepository.findByEmail(user.getEmail());

       if (isUserExist != null){
           throw new Exception("email already exist with another account");
       }
       User createdUser = new User();
       createdUser.setPassword(passwordEncoder.encode(user.getPassword()));
       createdUser.setEmail(user.getEmail());
       createdUser.setFullName(user.getFullName());
       User saveUser = userRepository.save(createdUser);

//       subscriptionService.createSubscription(saveUser);

        Authentication authentication = new UsernamePasswordAuthenticationToken(user.getEmail(),user.getPassword());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt= JwtProvider.generateToken(authentication);
        AuthResponse authResponse = new AuthResponse();
        //Comming from AuthResponse message & jwt
        authResponse.setMessage("signup success");
        authResponse.setJwt(jwt);

        return new ResponseEntity<>(authResponse, HttpStatus.CREATED);
    }

    @PostMapping("/signing")
    public ResponseEntity<AuthResponse> signing (@RequestBody LoginRequest loginRequest){

        String username = loginRequest.getEmail();
        String password = loginRequest.getPassword();

        Authentication authentication = authenticate(username,password);
                
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = JwtProvider.generateToken(authentication);

        AuthResponse response = new AuthResponse();
        response.setMessage("Signin Successfully");
        response.setJwt(jwt);

        return new ResponseEntity<>(response,HttpStatus.CREATED);

    }

    private Authentication authenticate(String username, String password) {

        UserDetails userDetails = customUserDetails.loadUserByUsername(username);
        if(userDetails==null){
            throw new BadCredentialsException("invalid username");
        }
        if (!passwordEncoder.matches(password,userDetails.getPassword())){
            throw new BadCredentialsException("invalid password");
        }
        return  new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
    }
}
