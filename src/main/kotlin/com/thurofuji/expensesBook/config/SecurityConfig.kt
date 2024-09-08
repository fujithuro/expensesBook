package com.thurofuji.expensesBook.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.oauth2.core.authorization.OAuth2AuthorizationManagers.hasScope
import org.springframework.security.web.SecurityFilterChain

@Configuration(proxyBeanMethods = false)
class SecurityConfig {

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        return http.authorizeHttpRequests { authZ -> authZ
            .requestMatchers(HttpMethod.GET).access(hasScope("expense:read"))
            .requestMatchers(HttpMethod.POST).access(hasScope("expense:write"))
            .requestMatchers(HttpMethod.PUT).access(hasScope("expense:write"))
            .requestMatchers(HttpMethod.DELETE).access(hasScope("expense:write"))
            .anyRequest().permitAll()
        }.oauth2ResourceServer { oauth -> oauth.jwt{} }
        .csrf { it.disable() }
        .build()
    }
}
