package com.piotrdomagalski.planning.security;

import com.piotrdomagalski.planning.app_user.AppUserService;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.List;

import static org.springframework.http.HttpMethod.*;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    private final AppUserService userService;
    private final PasswordEncoder bCryptPasswordEncoder;

    public SecurityConfig(AppUserService userService, PasswordEncoder bCryptPasswordEncoder) {
        this.userService = userService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    private static final String[] SWAGGER = {
            "/v2/api-docs",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui.html",
            "/webjars/**",
            "/v3/api-docs/**",
            "/swagger-ui/**"
    };


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.cors(c -> {
            CorsConfigurationSource cs = request -> {
                CorsConfiguration cc = new CorsConfiguration();
                cc.setAllowedOrigins(List.of("*"));
                cc.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
                cc.setAllowedHeaders(List.of("Origin", "Content-Type", "X-Auth-Token", "Access-Control-Expose-Header",
                        "Authorization"));
                cc.addExposedHeader("Authorization");
                cc.addExposedHeader("User-Name");
                return cc;
            };
            c.configurationSource(cs);
        });
        http.headers().frameOptions().disable();
        http.sessionManagement().sessionCreationPolicy(STATELESS);
        http.authorizeRequests().antMatchers("/login/**").permitAll();
        http.authorizeRequests().antMatchers("/h2-console/**").permitAll();
        http.authorizeRequests().antMatchers(SWAGGER).permitAll();
        http.authorizeRequests().antMatchers("/users/password-reset-request").permitAll();
        http.authorizeRequests().antMatchers("/users/password-change").permitAll();
        http.authorizeRequests().antMatchers("/users/**").hasAnyAuthority("ADMIN");
        http.authorizeRequests().antMatchers(GET).hasAnyAuthority("USER");
        http.authorizeRequests().antMatchers(POST).hasAnyAuthority("USER");
        http.authorizeRequests().antMatchers(PUT).hasAnyAuthority("USER");
        http.authorizeRequests().antMatchers(DELETE).hasAnyAuthority("MODERATOR");
        http.authorizeRequests().anyRequest().authenticated();
        http.addFilter(new CustomAuthenticationFilter(authenticationManager()));
        http.addFilterBefore(new CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(bCryptPasswordEncoder);
    }

    @Override
    protected UserDetailsService userDetailsService() {
        return userService;
    }
}
