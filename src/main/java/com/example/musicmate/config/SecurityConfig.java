package com.example.musicmate.config;


import com.example.musicmate.entity.User;
import com.example.musicmate.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .mvcMatchers("/", "/register", "/login", "/reset-password", "/password-recovery-email", "/new-password-user", "/music").permitAll()
                .anyRequest().authenticated()
                .and()
                .csrf().disable();
    }

//    @Bean
//    public PrincipalExtractor principalExtractor(UserRepository userRepository) {
//        return map -> {
//            Long id = (Long) map.get("sub");
//
//            User user = userRepository.findById(id).orElseGet(() -> {
//                User newUser = new User();
//
//                newUser.setId(id);
//                newUser.setEmail((String) map.get("email"));
//                newUser.setPassword((String) map.get("password"));
//                newUser.setName((String) map.get("name"));
//                newUser.setGender((String) map.get("gender"));
//                newUser.setAge((int) map.get("age"));
//
//                return newUser;
//            });
//
//            return userRepository.save(user);
//        };
//    }
}