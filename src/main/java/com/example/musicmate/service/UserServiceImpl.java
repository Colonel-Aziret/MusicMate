package com.example.musicmate.service;

import com.example.musicmate.entity.User;
import com.example.musicmate.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public User registerUser(String email, String password, String name, String gender, int age) {
        // Проверка, что пользователь с таким email не существует
        if (userRepository.findByEmail(email) != null) {
            throw new IllegalArgumentException("Пользователь с таким email уже существует");
        }

        // Валидация почты с использованием регулярного выражения (regexp)
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        if (!email.matches(emailRegex)) {
            throw new IllegalArgumentException("Некорректный формат email");
        }

        // Хеширование пароля перед сохранением в базу данных
        String hashedPassword = passwordEncoder.encode(password);

        // Создание и сохранение нового пользователя
        User user = new User();
        user.setEmail(email);
        user.setPassword(hashedPassword);
        user.setName(name);
        user.setGender(gender);
        user.setAge(age);

        return userRepository.save(user);
    }

    public void update(User user) {
        this.userRepository.save(user);
    }

    @Override
    public User authenticateUser(String email, String password) {
        User user = userRepository.findByEmail(email);

        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            return user;
        }

        return null;
    }


    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = this.userRepository.findFirstByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(user.getRole().getName()));
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), authorities);
    }
}
