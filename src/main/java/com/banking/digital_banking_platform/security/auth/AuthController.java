package com.banking.digital_banking_platform.security.auth;


import com.banking.digital_banking_platform.banking.dto.RefreshTokenRequest;
import com.banking.digital_banking_platform.banking.service.RefreshTokenService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
            @RequestBody LoginRequest request,
            HttpServletRequest httpServletRequest
    ){
        String ip=getClientIp(httpServletRequest);
        return ResponseEntity.ok(authService.login(request,ip));
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

    @PostMapping("/logout")
    public ResponseEntity<?>logout(
            @RequestBody RefreshTokenRequest request
    ) {
        refreshTokenService.logout(request.getRefreshToken());
        return ResponseEntity.ok("Logout successfully");
    }

    @PostMapping("/logout-all")
    public ResponseEntity<?>logoutAllDevices(
            @RequestParam Long userId
    ){
        return ResponseEntity.ok("Logged out from all devices");
    }

    private String getClientIp(HttpServletRequest request){
        String xf= request.getHeader("X-Forwarded-For");
        if(xf!=null && !xf.isEmpty()){
            return xf.split(",")[0];
        }
        return request.getRemoteAddr();
    }

}
