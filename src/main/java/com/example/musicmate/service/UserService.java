package com.example.musicmate.service;

import com.example.musicmate.entity.User;

public interface UserService {
    User registerUser(String email, String password, String name, String gender, int age);

    User authenticateUser(String email, String password);

    void resetPassword(String email, String newPassword);

}
