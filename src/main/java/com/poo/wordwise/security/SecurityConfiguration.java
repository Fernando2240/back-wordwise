package com.poo.wordwise.security;

import com.poo.wordwise.exception.CustomAccessDeniedHandler;
import com.poo.wordwise.exception.CustomAuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    private SecurityFilter securityFilter;
    private final CustomAccessDeniedHandler accessDeniedHandler;
    private final CustomAuthenticationEntryPoint authEntryPoint;

    public SecurityConfiguration(CustomAccessDeniedHandler accessDeniedHandler, CustomAuthenticationEntryPoint authEntryPoint) {
        this.accessDeniedHandler = accessDeniedHandler;
        this.authEntryPoint = authEntryPoint;
    }

    @Bean
    public SecurityFilterChain getSecurityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .cors(withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/usuario/register").permitAll()
                        .anyRequest().hasRole("USER")
                ).exceptionHandling(ex -> ex
                        .authenticationEntryPoint(authEntryPoint) // 401
                        .accessDeniedHandler(accessDeniedHandler) // 403
                )
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Autowired
    public void setSecurityFilter(SecurityFilter securityFilter) {
        this.securityFilter = securityFilter;
    }
}
