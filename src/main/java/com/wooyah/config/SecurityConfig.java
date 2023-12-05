package com.wooyah.config;

import com.wooyah.jwt.JwtAuthenticationProcessingFilter;
import com.wooyah.jwt.JwtService;
import com.wooyah.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsUtils;

@EnableWebSecurity
@AllArgsConstructor
@Configuration
public class SecurityConfig {
    private final UserRepository userRepository;
    private final JwtService jwtService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .requestMatchers(CorsUtils::isPreFlightRequest).permitAll() //cors 허용
                .requestMatchers(new AntPathRequestMatcher("/api/auth/**")).permitAll() // 주소 허용 permitall() -> 나머지 다 열어준다
                //.requestMatchers(new AntPathRequestMatcher("/test")).permitAll() // 주소 허용 permitall() -> 나머지 다 열어준다
                //.requestMatchers(new AntPathRequestMatcher("/api/cart/**")).permitAll() // 주소 허용 permitall() -> 나머지 다 열어준다
                .anyRequest().permitAll() //잠금
                .and()
                .httpBasic().disable() //http 기본 인증 인가 사용 x
                .cors().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilterBefore(jwtAuthenticationProcessingFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public JwtAuthenticationProcessingFilter jwtAuthenticationProcessingFilter() {
        JwtAuthenticationProcessingFilter jwtAuthenticationFilter = new JwtAuthenticationProcessingFilter(jwtService, userRepository);
        return jwtAuthenticationFilter;
    }
}