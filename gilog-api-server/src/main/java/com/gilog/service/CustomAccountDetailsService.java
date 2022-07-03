package com.gilog.service;

import com.gilog.entity.AuthAccount;
import com.gilog.entity.User;
import com.gilog.exception.BadRequestException;
import com.gilog.repository.UserRepositoryInt;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomAccountDetailsService implements UserDetailsService {
    private final UserRepositoryInt accountRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User account = accountRepository.findByUsername(username)
                .orElseThrow(() -> new BadRequestException("토큰을 확인하세요."));
        return new AuthAccount(account);
    }
}
