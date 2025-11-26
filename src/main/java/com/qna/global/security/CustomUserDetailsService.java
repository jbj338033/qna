package com.qna.global.security;

import com.qna.domain.user.domain.entity.User;
import com.qna.domain.user.domain.error.UserError;
import com.qna.domain.user.domain.repository.UserRepository;
import com.qna.global.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException(UserError.USER_NOT_FOUND));

        return new CustomUserDetails(user.getId(), user.getEmail(), user.getPassword(), Collections.emptyList());
    }
}
