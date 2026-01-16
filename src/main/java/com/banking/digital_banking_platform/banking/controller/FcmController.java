package com.banking.digital_banking_platform.banking.controller;

import com.banking.digital_banking_platform.banking.dto.FcmTokenRequest;
import com.banking.digital_banking_platform.banking.serviceImpl.FcmNotificationService;
import com.banking.digital_banking_platform.banking.serviceImpl.UserFcmTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/fcm")
@RequiredArgsConstructor
public class FcmController {


    private final FcmNotificationService notificationService;
    private final UserFcmTokenService userFcmTokenService;

    @PostMapping("/send")
    public String send(
            @RequestParam String token){
        return notificationService.sendNotification(
                token,
                "Wallet Alert",
                "Transaction successful"
        );
    }
    //save token
    @PostMapping("/token")
    public ResponseEntity<String>saveToken(
            @RequestBody FcmTokenRequest request
            ){
        userFcmTokenService.saveToken(request);
        return ResponseEntity.ok("FCM token saved");
    }

    //Test send to user
    @PostMapping("/sendtokenuser")
    public ResponseEntity<String>sendToUser(
            @RequestParam Long userId,
            @RequestParam String title,
            @RequestParam String body
    ){
        userFcmTokenService.sendNotificationToUser(userId,title,body);
        return ResponseEntity.ok("Notification triggered");
    }
}
