package com.ironhack.security;

import com.ironhack.filters.CustomAuthenticationFilter;
import com.ironhack.filters.CustomAuthorizationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfiguration {
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private AuthenticationManagerBuilder authManagerBuilder;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(authManagerBuilder.getOrBuild());
        customAuthenticationFilter.setFilterProcessesUrl("/login");
        /* disable */
        http.csrf().disable();
        /* Stateless */
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        /* Filter routes */
        http.authorizeHttpRequests()
                /* Autentificated routas for Users */
                .requestMatchers(HttpMethod.GET, "/user/login/**").authenticated()
                .requestMatchers(HttpMethod.POST, "/user/login/**").authenticated()
                .requestMatchers(HttpMethod.GET, "User/accounts/thirdparty/{id}/**").authenticated()
                /* Admin routes protected by roles*/
                .requestMatchers(HttpMethod.GET, "/admin/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/admin/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PATCH, "/admin/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/admin/**").hasRole("ADMIN")
                /* Common Users routes protected */
                .requestMatchers(HttpMethod.GET, "User/accounts/{id}/transfer**").hasRole("USER")
                .requestMatchers(HttpMethod.GET, "user/accounts/account-holder/{id}").hasAnyRole("USER", "ADMIN")
                /* rest are secure */
                .anyRequest().permitAll();

        // Add the custom authorization filter before the standard authentication filter.
        http.addFilter(customAuthenticationFilter);
        // Build the security filter chain to be returned.
        http.addFilterBefore(new CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();

    }
}