package com.ls.authservice.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationGrantAuthenticationToken;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class OtpAuthenticationToken extends OAuth2AuthorizationGrantAuthenticationToken {
    private final String username;
    private final String otp;
    private final String ip;
    private final String deviceId;
    public OtpAuthenticationToken(Authentication clientPrincipal,
                                  Set<String> scopes,
                                  Map<String, Object> additionalParameter) {
        super(new AuthorizationGrantType("otp"),clientPrincipal,additionalParameter);
        this.username =(String) additionalParameter.get("username");
        this.otp = (String) additionalParameter.get("otp");
        this.ip = (String) additionalParameter.get("ip");
        this.deviceId = (String) additionalParameter.get("deviceId");

    }
    public String getUsername() {
        return username;
    }
    public String getOtp() {
        return otp;
    }
    public String getIp() {
        return ip;
    }
    public String getDeviceId() {
        return deviceId;
    }
}
