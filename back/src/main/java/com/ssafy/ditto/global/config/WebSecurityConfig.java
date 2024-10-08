package com.ssafy.ditto.global.config;

import com.ssafy.ditto.global.jwt.JwtFilter;
import com.ssafy.ditto.global.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsUtils;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    private final JwtProvider jwtProvider;

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(corsConfig ->
                        corsConfig.disable())
                .csrf(csrfConfig ->
                        csrfConfig.disable())
                .formLogin(formLoginConfig ->
                        formLoginConfig.disable())
                .httpBasic(httpBasicConfig ->
                        httpBasicConfig.disable())
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                                .requestMatchers(CorsUtils::isCorsRequest).permitAll()
                                .requestMatchers("/v3/api-docs/**", "/swagger-ui/**").permitAll()
//                                .requestMatchers(HttpMethod.GET
//                                        , "/classes/**", "/posts/**", "/comments/**"
//                                        , "/categories/**", "/files/**", "/profiles/**", "tags/**").permitAll()
//                                .requestMatchers("/users/**").permitAll()
//                                .anyRequest().authenticated()
                                .anyRequest().permitAll()
                )
                .addFilterBefore(new JwtFilter(jwtProvider), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}