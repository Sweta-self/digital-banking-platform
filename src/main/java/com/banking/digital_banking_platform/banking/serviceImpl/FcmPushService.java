package com.banking.digital_banking_platform.banking.serviceImpl;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import org.springframework.stereotype.Service;

@Service
public class FcmPushService {
    public void sendPush(
            String token,
            String title,
            String body
    ){
        Notification notification=Notification.builder()
                .setTitle(title)
                .setBody(body)
                .build();
        Message message= Message.builder()
                .setToken(token)
                .setNotification(notification)
                .build();
        try{
            String response=
                    FirebaseMessaging.getInstance().send(message);
            System.out.println("FCM SENT:"+response);
        }
        catch(FirebaseMessagingException e){
            System.out.println("FCM ERROR:"+e.getMessage());
        }
    }
}
