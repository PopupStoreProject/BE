package com.project.kpaas.global.security;

import com.project.kpaas.global.exception.ErrorCode;
import com.project.kpaas.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import com.project.kpaas.user.entity.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = (User) userRepository.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException(ErrorCode.NOT_FOUND_USER.getMsg()));
        return new UserDetailsImpl(user, user.getUsername());
    }
}
