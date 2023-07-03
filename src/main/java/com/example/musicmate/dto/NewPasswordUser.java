package com.example.musicmate.dto;

import lombok.Data;

@Data
public class NewPasswordUser {
    private String login;
    private String email;
    private String password;
    private String repeatPassword;
    private Integer token;
}
