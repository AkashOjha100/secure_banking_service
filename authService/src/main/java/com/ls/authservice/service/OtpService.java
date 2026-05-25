package com.ls.authservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class OtpService {
    @Autowired
    private StringRedisTemplate redisTemplate;
    private static final String OTP_KEY = "otp";
    private static final String META_KEY = "otp_meta";

    public String generateOtpCode(String username,String ip,String deviceId) {
        String otpCode = String.valueOf(ThreadLocalRandom.current().nextInt(100000, 999999));
        redisTemplate.opsForValue().set(OTP_KEY+ username,otpCode, Duration.ofMinutes(2));
        String meta =ip+" "+deviceId;
        redisTemplate.opsForValue().set(META_KEY+ username,meta, Duration.ofMinutes(2));
        return otpCode;
    }

    public boolean checkOtpCode(String username, String otp,String ip,String deviceId ) {
        String storeOtp = redisTemplate.opsForValue().get(OTP_KEY+ username);
        String meta = redisTemplate.opsForValue().get(META_KEY+ username);
        if (storeOtp == null || meta == null) {
            return false;
        }
        String [] parts = meta.trim().split("\\s+");
        if(parts.length < 2){
            throw new RuntimeException("Invalid OTP metadata format: " +meta);
        }
        String storeIp = parts[0];
        String storedDeviceId = parts[1];
        boolean valid = storeOtp.equals(otp)
                && storeIp.equals(ip)
                && storedDeviceId.equals(deviceId);

        redisTemplate.delete(OTP_KEY+ username);
        redisTemplate.delete(META_KEY+ username);
        System.out.println("INPUT OTP: " + otp);
        System.out.println("STORED OTP: " + storeOtp);

        System.out.println("INPUT IP: " + ip);
        System.out.println("STORED IP: " + storeIp);

        System.out.println("INPUT DEVICE: " + deviceId);
        System.out.println("STORED DEVICE: " + storedDeviceId);
        return valid;
    }
}
