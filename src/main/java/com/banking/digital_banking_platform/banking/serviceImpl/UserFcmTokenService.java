package com.banking.digital_banking_platform.banking.serviceImpl;

import com.banking.digital_banking_platform.banking.dto.FcmTokenRequest;
import com.banking.digital_banking_platform.banking.entity.UserFcmToken;
import com.banking.digital_banking_platform.banking.repository.UserFcmTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserFcmTokenService {
    private final UserFcmTokenRepository repo;
    private final FcmPushService pushService;

    //save token
    public void saveToken(FcmTokenRequest request){
        repo.findByUserIdAndFcmToken(request.getUserId(),request.getToken())
                .orElseGet(()->{
                    UserFcmToken token=new UserFcmToken();
                    token.setUserId(request.getUserId());
                    token.setFcmToken(request.getToken());
                    token.setPlatform(request.getPlatform());
                    return repo.save(token);
                });
    }

    //send to user
    public void sendNotificationToUser(Long userId,String title,String body){
        List<String> tokens=repo.findTokenByUserId(userId);
        for(String token:tokens){
            pushService.sendPush(token,title,body);
        }
    }
}
