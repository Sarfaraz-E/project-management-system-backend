//package com.example.ProjectManagementSystem.controller;
//
//import com.example.ProjectManagementSystem.model.PlanType;
//import com.example.ProjectManagementSystem.model.Subscription;
//import com.example.ProjectManagementSystem.model.User;
//import com.example.ProjectManagementSystem.service.SubscriptionService;
//import com.example.ProjectManagementSystem.service.UserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/api/subscriptions")
//public class SubscriptionController {
//
//    @Autowired
//    private SubscriptionService subscriptionService;
//
//    @Autowired
//    private UserService userService;
//
//    @GetMapping("/user")
//    public ResponseEntity<Subscription> getrUserSubscription
//            (@RequestHeader ("Authorization")String jwt) throws Exception{
//        User user = userService.findUserProfileByJwt(jwt);
//
//        Subscription subscription = subscriptionService.getUserSubscription(user.getId());
//
//        return new ResponseEntity<>(subscription, HttpStatus.OK);
//    }
//
//    @PatchMapping("/upgrade")
//    public ResponseEntity<Subscription> upgradeSubscription
//            (@RequestHeader ("Authorization")String jwt,
//             @RequestParam PlanType planType) throws Exception{
//        User user = userService.findUserProfileByJwt(jwt);
//
//        Subscription subscription = subscriptionService.updateSubscription(user.getId(), planType);
//
//        return new ResponseEntity<>(subscription, HttpStatus.OK);
//    }
//
//
//}
