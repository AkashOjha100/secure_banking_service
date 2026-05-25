package com.ls.authservice.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class LoginFailureEventListener {

    @EventListener
    public void loginFailure(AuthenticationFailureBadCredentialsEvent event) {
        log.warn("LoginFailed user{}", event.getAuthentication().getName());
    }
}
