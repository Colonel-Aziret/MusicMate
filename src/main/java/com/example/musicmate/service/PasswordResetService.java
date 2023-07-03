package com.example.musicmate.service;

import com.example.musicmate.entity.PasswordResetToken;
import com.example.musicmate.entity.User;
import com.example.musicmate.repository.PasswordResetTokenRepository;
import com.example.musicmate.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@Service
public class PasswordResetService {
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public PasswordResetService(PasswordResetTokenRepository passwordResetTokenRepository, UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean isTokenUsed(String token) {
        PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByToken(token);
        return passwordResetToken != null && passwordResetToken.isUsed();
    }

    public void markTokenAsUsed(String token) {
        PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByToken(token);
        if (passwordResetToken != null) {
            passwordResetToken.setUsed(true);
            passwordResetTokenRepository.save(passwordResetToken);
        }
    }

    public void resetPassword(String token, String newPassword) {
        PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByToken(token);
        if (passwordResetToken == null || passwordResetToken.isUsed()) {
            throw new IllegalArgumentException("Неверный токен сброса пароля или токен уже использован.");
        }

        User user = passwordResetToken.getUser();
        if (user == null) {
            throw new IllegalArgumentException("Пользователь не найден.");
        }

        String encodedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encodedPassword);

        userRepository.save(user);
        passwordResetToken.setUsed(true);
        passwordResetTokenRepository.save(passwordResetToken);
    }
}

