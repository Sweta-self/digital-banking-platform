package com.banking.digital_banking_platform.security.config;

import jakarta.servlet.http.HttpServletRequest;

public class IpUtil {
    public static String getClientIp(HttpServletRequest request){
        String xf= request.getHeader("X-Forwarded-For");
        if(xf!=null && !xf.isEmpty()){
            return xf.split(",")[0];
        }
        return request.getRemoteAddr();
    }
}
