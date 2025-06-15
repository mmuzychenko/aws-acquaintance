package com.aws.acquaintance.image_service.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // configure access control

/*
    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        // configure http security
        http
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/home").permitAll()
                        .anyRequest().authenticated()) // allow only authenticated requests
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> {
                }));

        return http.build();
    }

 */


    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        // configure http security

        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/test").permitAll() //TODO: remove this after tests
                .antMatchers("/image").permitAll() //TODO: remove this after tests
                .antMatchers("/image/upload").permitAll() //TODO: remove this after tests
                .antMatchers("/file-rest/delete/**").permitAll() //TODO: remove this after tests
                .anyRequest().authenticated();

        http.oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> {
        }));

        return http.build();
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        // Use a symmetric key (e.g., HS256) or public key (RS256)
        String jwkSetUri = "https://your-auth-domain/.well-known/jwks.json";
        return NimbusJwtDecoder.withJwkSetUri(jwkSetUri).build();
    }

//    @Override
//    protected void configure(HttpSecurity security) throws Exception
//    {
//        security.httpBasic().disable();
//    }

//    protected void configure(HttpSecurity http) throws Exception {
//        http
//                .csrf()
//                .disable()
//                .sessionManagement()
//                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                .and()
//                .authorizeRequests()
//                .antMatchers(authWhiteList).permitAll()
//                .and()
//                .authorizeRequests()
//                .antMatchers(TOKEN_AUTH_ENTRY_POINT).authenticated()
//                .and()
//                .addFilterBefore(loginAuthenticationProcessingFilter(), UsernamePasswordAuthenticationFilter.class)
//                .addFilterBefore(jwtAuthenticationProcessingFilter(), UsernamePasswordAuthenticationFilter.class);
//    }

}
