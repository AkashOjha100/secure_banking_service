package com.ls.customer.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@EnableMethodSecurity(securedEnabled = true)
@Configuration
public class MethodSecurityConfig {


}
