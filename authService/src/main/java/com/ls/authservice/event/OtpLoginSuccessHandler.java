package com.ls.authservice.event;

import com.ls.authservice.domain.User;
import com.ls.authservice.repository.UserRepository;
import com.ls.authservice.service.EmailService;
import com.ls.authservice.service.OtpService;
import com.ls.authservice.service.SmsService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class OtpLoginSuccessHandler implements AuthenticationSuccessHandler {
    @Autowired
    private SmsService smsService;
    @Autowired
    private OtpService otpService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private UserRepository userRepo;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication
    ) throws IOException, ServletException {
        String userName = authentication.getName();

        String ip = request.getRemoteAddr();
        if ("0:0:0:0:0:0:0:1".equals(ip)) {
            ip = "127.0.0.1";
        }

        String deviceId = request.getHeader("X-Device-Id");
        if (deviceId == null || deviceId.isBlank()) {
            deviceId = "UNKNOWN_DEVICE";
        }
        //String ip = request.getRemoteAddr();
        //String deviceId = request.getHeader("X-Device-Id");
        User user = userRepo.findByUsername(userName).orElseThrow(()->new RuntimeException("Username not found"));
        String otp =otpService.generateOtpCode(userName,ip,deviceId);
        if(user.getPassword()!=null){
            smsService.sendOtp(user.getPhoneNumber(),otp);
        }
        if(user.getEmail()!=null){
            emailService.sendOtp(user.getEmail(),otp);
        }

    }
}
