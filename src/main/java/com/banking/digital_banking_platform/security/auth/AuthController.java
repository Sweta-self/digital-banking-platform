package com.banking.digital_banking_platform.security.auth;


import com.banking.digital_banking_platform.banking.dto.RefreshTokenRequest;
import com.banking.digital_banking_platform.banking.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/register")
    public ResponseEntity<String>register(
            @RequestBody RegisterRequest request
    ){
        authService.register(request);
        return ResponseEntity.ok("Customer Registered Successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse>login(
            @RequestBody LoginRequest request
    ){
        return ResponseEntity.ok(authService.login(request));
    }
    @PostMapping("/refresh")
    public ResponseEntity<LoginResponse>refreshToken(
            @RequestBody RefreshTokenRequest request
            ){
        String newRefreshToken=refreshTokenService.rotateRefreshToken(request.getRefreshToken());
        String newAccessToken= refreshTokenService.refreshAccessToken(newRefreshToken);
        return ResponseEntity.ok(
                new LoginResponse(
                        newAccessToken,newRefreshToken
                )
        );
    }
}
