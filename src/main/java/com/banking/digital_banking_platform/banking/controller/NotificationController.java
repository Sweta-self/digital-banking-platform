package com.banking.digital_banking_platform.banking.controller;

import com.banking.digital_banking_platform.banking.dto.NotificationRequestDto;
import com.banking.digital_banking_platform.banking.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notifications")
public class NotificationController {
    private final NotificationService notificationService;

    //----For testing purpose----
    @PostMapping("/send")
    public ResponseEntity<String>send(
            @RequestBody NotificationRequestDto request
            ){
        notificationService.send(request);
        return ResponseEntity.ok("Notification sent");
    }

}
