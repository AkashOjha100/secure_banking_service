package com.ls.authservice.service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SmsService {

    @Value("${twilio.sid}")
    private String accountId;

    @Value("${twilio.token}")
    private String authToken;

    @Value("${twilio.number}")
    private String fromNumber;

    @PostConstruct
    public void init() {
        Twilio.init(accountId, authToken);
    }
    public void sendOtp(String phoneNumber,String otp) {
        Message.creator(new PhoneNumber(phoneNumber),
                new PhoneNumber(fromNumber),"your Banking Otp: "+otp)
                .create();

    }

}
