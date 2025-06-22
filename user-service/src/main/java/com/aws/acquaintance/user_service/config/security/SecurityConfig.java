package com.aws.acquaintance.user_service.config.security;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {


    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        // configure http security

        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/home").permitAll()
                .antMatchers("/image-to-user").permitAll() // IMPORTANT: MUST permit this endpoint
                .antMatchers("/image-to-user/**").permitAll() // IMPORTANT: MUST permit this endpoint
                .anyRequest().authenticated();
        http.oauth2Login(Customizer.withDefaults()); // incoming requests should contain jwt token

        http.oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> {
        }));

        return http.build();
    }
}