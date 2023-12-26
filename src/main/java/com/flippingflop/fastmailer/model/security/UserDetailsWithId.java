package com.flippingflop.fastmailer.model.security;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class UserDetailsWithId extends User {

    @Getter
    private final Long userId;

    public UserDetailsWithId(String username,
                             String password,
                             Collection<? extends GrantedAuthority> authorities,
                             Long userId) {
        super(username, password, authorities);
        this.userId = userId;
    }
}
