package com.ls.authservice.config;

import com.ls.authservice.domain.User;
import com.ls.authservice.repository.UserRepository;
import com.ls.authservice.security.OtpAuthenticationConverter;
import com.ls.authservice.security.OtpAuthenticationProvider;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.token.*;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class AuthorizationServerConfig {

    @Autowired
    private UserRepository userRepo;
    @Bean
    @Order(-1)
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   OtpAuthenticationConverter otpConverter,
                                                   OtpAuthenticationProvider otpProvider) throws Exception {
        OAuth2AuthorizationServerConfigurer configurer =new OAuth2AuthorizationServerConfigurer();
        http
                .securityMatcher(configurer.getEndpointsMatcher())
                .with(configurer,authServer->authServer
                        .tokenEndpoint(tokenEndpoint -> tokenEndpoint
                                .accessTokenRequestConverter(otpConverter)
                                .authenticationProvider(otpProvider)
                        )
                )
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                    .anyRequest().authenticated());
                return http.build();

    }

    @Bean
    public OAuth2TokenGenerator<? extends OAuth2Token>  tokenGenerator(JWKSource<SecurityContext> jwksSource,
                OAuth2TokenCustomizer<JwtEncodingContext> customizer ) {

        JwtGenerator jwtGenerator = new JwtGenerator(new NimbusJwtEncoder(jwksSource));
        jwtGenerator.setJwtCustomizer(customizer);
        return new DelegatingOAuth2TokenGenerator(jwtGenerator, new OAuth2AccessTokenGenerator());

    }

    @Bean
    public OAuth2TokenCustomizer<JwtEncodingContext> tokenCustomizer() {
        return (context) -> {
            if(OAuth2TokenType.ACCESS_TOKEN.equals(context.getTokenType())) {
                Authentication pricipal =context.getPrincipal();
                if(pricipal.getPrincipal() instanceof UserDetails user) {
                    User dbUser = userRepo.findByUsername(user.getUsername()).orElseThrow(()->
                            new RuntimeException("User not found"));
                    context.getClaims().claim("userId", dbUser.getId());
                }
            }
        };
    }
}
