package com.ls.authservice.security;

import com.ls.authservice.service.CustomUserDetailService;
import com.ls.authservice.service.OtpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.context.AuthorizationServerContextHolder;
import org.springframework.security.oauth2.server.authorization.token.DefaultOAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.stereotype.Component;

import static org.springframework.web.server.ServerWebExchangeExtensionsKt.principal;

@Component
public class OtpAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private OtpService otpService;
    @Autowired
    private CustomUserDetailService userDetailService;
    @Autowired
    private OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        OtpAuthenticationToken token = (OtpAuthenticationToken) authentication;
        String username = token.getUsername();
        String otp = token.getOtp();
        String ip = token.getIp();
        String deviceId = token.getDeviceId();
        if(!otpService.checkOtpCode(username, otp, ip, deviceId)){
            throw new BadCredentialsException("Invalid OTP code");
        }
        UserDetails user =userDetailService.loadUserByUsername(username);
        DefaultOAuth2TokenContext tokenContext = DefaultOAuth2TokenContext.builder()
                .registeredClient(((OAuth2ClientAuthenticationToken) token.getPrincipal()).getRegisteredClient())
                .principal(new UsernamePasswordAuthenticationToken(user ,null ,user.getAuthorities()))
                .authorizationServerContext(AuthorizationServerContextHolder.getContext())
                .tokenType(OAuth2TokenType.ACCESS_TOKEN)
                .authorizationGrantType(new AuthorizationGrantType("otp"))
                .authorizationGrant(token).build();
        OAuth2Token generateToken = this.tokenGenerator.generate(tokenContext);
        OAuth2AccessToken accessToken = new OAuth2AccessToken(
                OAuth2AccessToken.TokenType.BEARER,
                generateToken.getTokenValue(),
                generateToken.getIssuedAt(),
                generateToken.getExpiresAt(),
                tokenContext.getAuthorizedScopes()
        );
        return new OAuth2AccessTokenAuthenticationToken(((OAuth2ClientAuthenticationToken) token.getPrincipal()).getRegisteredClient(),
                (Authentication) token.getPrincipal(),accessToken);
        //return new UsernamePasswordAuthenticationToken(user,null,user.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return OtpAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
