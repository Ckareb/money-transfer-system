package com.course.money_transfer_system.auth.model;

import lombok.Getter;

@Getter
public class UserDetails {
    private final String username;
    private final Long userId;

    public UserDetails(String username, Long userId) {
        this.username = username;
        this.userId = userId;
    }
}
