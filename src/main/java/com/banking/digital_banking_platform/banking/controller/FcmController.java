package com.banking.digital_banking_platform.banking.controller;

import com.banking.digital_banking_platform.banking.serviceImpl.FcmNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fcm")
@RequiredArgsConstructor
public class FcmController {


    private final FcmNotificationService notificationService;

    @PostMapping("/send")
    public String send(
            @RequestParam String token){
        return notificationService.sendNotification(
                token,
                "Wallet Alert",
                "Transaction successful"
        );
    }

}
