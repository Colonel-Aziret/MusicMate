package com.example.musicmate.config;


import com.example.musicmate.entity.User;
import com.example.musicmate.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AndRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserRepository userRepository;
    private final CustomizeAuthenticationSuccessHandler customizeAuthenticationSuccessHandler;



    public SecurityConfig(UserRepository userRepository, CustomizeAuthenticationSuccessHandler customizeAuthenticationSuccessHandler) {
        this.userRepository = userRepository;
        this.customizeAuthenticationSuccessHandler = customizeAuthenticationSuccessHandler;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }




    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/", "/register", "/login", "/reset-password", "/password-recovery-email", "/new-password-user")
                .permitAll()
                .anyRequest()
                .authenticated()
                .and().csrf().disable()
                .formLogin().successHandler(customizeAuthenticationSuccessHandler)
                .loginPage("/login").failureUrl("/login?error=true")
                .usernameParameter("login")
                .passwordParameter("password")
                .and().logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/").and().exceptionHandling()
                .accessDeniedPage("/access-denied")
                .and()
                .csrf()
                .csrfTokenRepository(new HttpSessionCsrfTokenRepository());
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web
                .ignoring()
                .antMatchers("/resources/**", "/templates/**", "/static/**", "registration");
    }


}