package com.thurofuji.expensesBook.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.oauth2.core.authorization.OAuth2AuthorizationManagers.hasScope
import org.springframework.security.web.SecurityFilterChain

/**
 * セキュリティに関する設定を管理するConfiguration
 */
@Configuration(proxyBeanMethods = false)
class SecurityConfig {

    /**
     * JWTを使用した認可のための設定を追加する
     *
     * 閲覧権限
     * * メソッド: GET
     * * トークンのscopeに`expense:read`が必要
     *
     * 編集権限
     * * メソッド: POST/PUT/DELETE
     * * トークンのscopeに`expense:write`が必要
     */
    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        return http.authorizeHttpRequests { authZ -> authZ
            .requestMatchers(HttpMethod.POST, "/api/login").permitAll()     // ログイン処理は認証不要
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
