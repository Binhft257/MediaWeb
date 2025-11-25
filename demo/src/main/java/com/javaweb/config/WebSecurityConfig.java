//package com.javaweb.config;
//
//import com.javaweb.filters.JwtTokenFilter;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.Customizer;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
//@Configuration
//public class WebSecurityConfig {
//
//    @Autowired
//    private JwtTokenFilter jwtFilter;
//
//
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//
//        http.csrf(csrf -> csrf.disable());
//
//        http.authorizeHttpRequests(auth -> auth
//                .requestMatchers("/api/auth/login", "/api/users/register", "/api/auth/refresh").permitAll()
//                .requestMatchers("/api/medias/**").permitAll()// bỏ qua token
//                .anyRequest().authenticated()  // các API còn lại phải có JWT
//        );
//
//        // Thêm JWT filter trước UsernamePasswordAuthenticationFilter
//        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
//
//        return http.build();
//    }
//
//}