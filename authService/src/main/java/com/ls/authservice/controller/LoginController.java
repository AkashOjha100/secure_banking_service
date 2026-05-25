package com.ls.authservice.controller;

import com.ls.authservice.domain.User;
import com.ls.authservice.repository.UserRepository;
import com.ls.authservice.service.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/auth")
public class LoginController {

    @Autowired
    private AuditService auditService;

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private OtpService otpService;
    @Autowired
    private SmsService smsService;
    @Autowired
    private EmailService emailService;

    @PostMapping("/login-success")
    public void loginSuccess(@RequestParam String username,HttpServletRequest request) {
        auditService.logSuccess(username, request.getRemoteAddr());
    }

    @PostMapping("/login-failure")
    public void loginFailure(@RequestParam String username,
                             @RequestParam String reason ,
                             HttpServletRequest request) {
        auditService.logFailure(username, reason, request.getRemoteAddr());
    }

//    @PostMapping("/login")
//    public ResponseEntity<?> login(@RequestParam String userName,
//                                   @RequestParam String password,
//                                   HttpServletRequest request) {
//        User user = userService.findByUserName(userName);
//        if (!user.isAccountNonLocked()) {
//
//            if (user.getLockTime() != null &&
//                    user.getLockTime().plusMinutes(15).isBefore(LocalDateTime.now())) {
//
//                user.setAccountNonLocked(true);
//                user.setFailedAttempts(0);
//                user.setLockTime(null); // 🔥 important reset
//                userRepo.save(user);
//
//            } else {
//                throw new RuntimeException("Account is locked");
//            }
//        }
////        if(!user.isAccountNonLocked()){
////            if(user.getLockTime().plusMinutes(15).isBefore(LocalDateTime.now())){
////                user.setAccountNonLocked(true);
////                user.setFailedAttempts(0);
////                userRepo.save(user);
////            }
////            else{
////                throw new RuntimeException("Account is locked");
////            }
////        }
//        if(!encoder.matches(password,user.getPassword())){
//            user.setFailedAttempts(user.getFailedAttempts() + 1);
//            if(user.getFailedAttempts() >= 6){
//                user.setAccountNonLocked(false);
//                user.setLockTime(LocalDateTime.now());
//            }
//            userRepo.save(user);
//            throw new RuntimeException("Invalid Credentials");
//        }
//        user.setFailedAttempts(0);
//        userRepo.save(user);
//        String ip = request.getRemoteAddr();
//        String deviceId = request.getHeader("X-Device-Id");
//        String otp = otpService.generateOtpCode(userName,ip,deviceId);
//        smsService.sendOtp(user.getUsername(),otp);
//        emailService.sendOtp(user.getUsername(),otp);
//        return ResponseEntity.ok("OTP sent");
//    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String userName,
                               @RequestParam String password,
                               HttpServletRequest request) {

        User user = userService.findByUserName(userName);

        if (user == null) {
            return ResponseEntity.badRequest().body("User not found");
        }

        //  Check lock
        if (!user.isAccountNonLocked()) {

            if (user.getLockTime() != null) {

                LocalDateTime unlockTime = user.getLockTime().plusMinutes(15);

                if (LocalDateTime.now().isAfter(unlockTime)) {
                    user.setAccountNonLocked(true);
                    user.setFailedAttempts(0);
                    user.setLockTime(null);
                    userRepo.save(user);
                } else {
                    long minutesLeft = Duration.between(
                            LocalDateTime.now(),
                            unlockTime
                    ).toMinutes();

                    return ResponseEntity.badRequest()
                        .body("Account locked. Try again in " + minutesLeft + " minutes");
                }
            }
        }

        // Wrong password
        if (!encoder.matches(password, user.getPassword())) {

            user.setFailedAttempts(user.getFailedAttempts() + 1);

            if (user.getFailedAttempts() >= 5) {
                user.setAccountNonLocked(false);
                user.setLockTime(LocalDateTime.now());
            }
            userRepo.save(user);

            return ResponseEntity.badRequest().body("Invalid credentials");
        }

        user.setFailedAttempts(0);
        userRepo.save(user);

        String ip = request.getRemoteAddr();
        String deviceId = request.getHeader("X-Device-Id");

        String otp = otpService.generateOtpCode(userName, ip, deviceId);

        smsService.sendOtp(user.getPhoneNumber(), otp);
        emailService.sendOtp(user.getEmail(), otp);

        return ResponseEntity.ok("OTP sent");
    }
}
