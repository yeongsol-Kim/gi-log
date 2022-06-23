package com.gilog.entity;

import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Getter
public class AuthAccount extends org.springframework.security.core.userdetails.User {
    private final User account;

    public AuthAccount(User account) {
        super(account.getUsername(), account.getPassword(), List.of(new SimpleGrantedAuthority(account.getRole())));
        this.account = account;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        AuthAccount that = (AuthAccount) o;
        return Objects.equals(account, that.account);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), account);
    }
}