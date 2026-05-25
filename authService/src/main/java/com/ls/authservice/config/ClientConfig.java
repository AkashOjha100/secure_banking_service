package com.ls.authservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;

import java.util.UUID;

@Configuration
public class ClientConfig {

    @Bean
    public RegisteredClientRepository registeredClientRepository(PasswordEncoder passwordEncoder) {

        RegisteredClient bankingClient = RegisteredClient.withId(UUID.randomUUID().toString())
                .clientId("api-gateway")
                .clientSecret(passwordEncoder.encode("secret123"))
                .authorizationGrantType(new AuthorizationGrantType("otp"))
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                .scope("account.read")
                .tokenSettings(TokenSettings
                        .builder()
                        .accessTokenTimeToLive(java.time.Duration.ofMinutes(20))
                        .refreshTokenTimeToLive(java.time.Duration.ofDays(7))
                        .reuseRefreshTokens(false)
                        .build())
                .build();
        return new InMemoryRegisteredClientRepository(bankingClient);
    }
}
