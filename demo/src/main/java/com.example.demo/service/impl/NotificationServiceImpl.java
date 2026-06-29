package com.example.demo.service.impl;

import com.example.demo.domain.Order;
import com.example.demo.domain.User;
import com.example.demo.enums.UserType;
import com.example.demo.repository.UserRepository;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class NotificationServiceImpl {

    private final UserRepository userRepository;

    public void sendToUser(Long userId, String title, String body, Order order){

        User user = userRepository.findById(userId)
                        .orElseThrow();


        if(user.getFcmToken()!=null){
            sendFcmMessage(user.getFcmToken(), title, body, order);
        }
    }

    public void sendToUserType(
            UserType userType, String title, String body, Order order
    ){
        List<String> tokens = userRepository.findFcmTokensByUserType(userType);

        tokens.forEach(token ->
                sendFcmMessage(token, title, body, order));
    }

    private void sendFcmMessage(String token, String title, String body, Order order
    ){

        Message message = Message.builder()
                        .setToken(token)
                        .setNotification(
                                Notification.builder()
                                        .setTitle(title)
                                        .setBody(body)
                                        .build())


                        .putData("orderId", String.valueOf(order.getId()))
                        .putData("customerName", order.getCustomer().getFirstName())
                        .putData("customerPhone", order.getCustomer().getPhoneNumber())
                        .putData("address", order.getCustomer().getAddress())
                        .putData("itemName", order.getItem().getName())
                        .putData("price",String.valueOf(order.getItem().getPrice()))
                        .putData("imageUrl", order.getItem().getImageUrl())
                        .build();

        try {
            FirebaseMessaging
                    .getInstance()
                    .send(message);

            System.out.println("FCM sent successfully : " + token);

        } catch(FirebaseMessagingException e){
            System.err.println("FCM failed : " + e.getMessage());
        }
    }
}