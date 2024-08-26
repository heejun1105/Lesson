 package com.hkit.lessons;

import java.util.Collection;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
	
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests((authorizeHttpRequests) -> authorizeHttpRequests
            	.requestMatchers(new AntPathRequestMatcher("/admins/**")).hasRole("ADMIN")
            	.requestMatchers(new AntPathRequestMatcher("/**")).permitAll())
            	.headers((headers)->headers
            		.addHeaderWriter(new XFrameOptionsHeaderWriter(
            				XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN)))
            //로그인 성공시 루트 페이지인 / 로 이동하도록 한다.
            	.formLogin((formLogin) -> formLogin
            		    .loginPage("/member/login")
            		    .successHandler((request, response, authentication) -> {
            		        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            		        boolean isAdmin = authorities.stream()
            		                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
            		        if (isAdmin) {
            		            response.sendRedirect("/admins"); // 관리자 전용 페이지로 이동
            		        } else {
            		            response.sendRedirect("/"); // 일반 사용자 페이지로 이동
            		        }
            		    })
            		    .failureUrl("/member/login?error"))

            //로그아웃 성공시 루트 페이지인 / 로 이동하도록 한다.
            .logout((logout)->logout
            		.logoutRequestMatcher(new AntPathRequestMatcher("/member/logout"))
            		.logoutSuccessUrl("/")
            		.invalidateHttpSession(true))
            ;
        return http.build();
    }
    
    @Bean
    PasswordEncoder passwordEncoder() {
    	return new BCryptPasswordEncoder();
    }
    @Bean
    AuthenticationManager authenticationManager (AuthenticationConfiguration 
authenticationConfiguration) throws Exception{
    	return authenticationConfiguration.getAuthenticationManager();
 
 }   
} 
    