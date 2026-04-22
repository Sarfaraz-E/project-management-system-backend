//package com.example.ProjectManagementSystem.service;
//
//import com.example.ProjectManagementSystem.model.PlanType;
//import com.example.ProjectManagementSystem.model.Subscription;
//import com.example.ProjectManagementSystem.model.User;
//import com.example.ProjectManagementSystem.repository.SubscriptionRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import java.time.LocalDate;
//
//public class SubscriptionServiceImpl implements SubscriptionService{
//
//    @Autowired
//    private UserService userService;
//
//    @Autowired
//    private SubscriptionRepository subscriptionRepository;
//
//    @Override
//    public Subscription createSubscription(User user) {
//        Subscription subscription = new Subscription();
//        subscription.setUser(user);
//        subscription.setSubscriptionStartDate(LocalDate.now());
//        subscription.setGetSubscriptionEndDate(LocalDate.now().plusMonths(12));
//        subscription.isValid(true);
//        subscription.setPlanType(PlanType.FREE);
//
//         return subscriptionRepository.save(subscription);
//    }
//
//    @Override
//    public Subscription getUserSubscription(Long userId) throws Exception {
//        Subscription subscription= subscriptionRepository.findByUserId(userId);
//
//        if (isValid(subscription)){
//            subscription.setPlanType(PlanType.FREE);
//            subscription.setGetSubscriptionEndDate(LocalDate.now().plusMonths(12));
//            subscription.setSubscriptionStartDate(LocalDate.now());
//        }
//        return subscriptionRepository.save(subscription);
//    }
//
//    @Override
//    public Subscription updateSubscription(Long userId, PlanType planType) {
//        Subscription subscription = subscriptionRepository.findByUserId(userId);
//        subscription.setPlanType(planType);
//        subscription.setSubscriptionStartDate(LocalDate.now());
//        if (planType.equals(PlanType.ANNUALLY)){
//            subscription.setGetSubscriptionEndDate(LocalDate.now().plusMonths(12));
//
//        }else{
//            subscription.setGetSubscriptionEndDate(LocalDate.now().plusMonths(1));
//        }
//        return subscriptionRepository.save(subscription);
//    }
//
//    @Override
//    public boolean isValid(Subscription subscription) {
//        if(subscription.getPlanType().equals(PlanType.FREE)){
//            return true;
//        }
//        LocalDate endDate = subscription.getGetSubscriptionEndDate();
//        LocalDate currentDate = LocalDate.now();
//
//        return endDate.isAfter(currentDate) || endDate.isEqual(currentDate);
//    }
//}
