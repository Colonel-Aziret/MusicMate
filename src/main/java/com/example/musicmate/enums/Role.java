package com.example.musicmate.enums;

public enum Role {
    ADMIN("Админ"),
    USER("Пользователь");

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    Role(String name) {
        this.name = name;
    }
}
