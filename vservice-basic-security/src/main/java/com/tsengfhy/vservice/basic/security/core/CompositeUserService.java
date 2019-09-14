package com.tsengfhy.vservice.basic.security.core;

import com.tsengfhy.vservice.basic.utils.MessageSourceUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;


public class CompositeUserService implements UserDetailsService {

    private final List<UserDetailsService> delegateServices;

    public CompositeUserService(List<UserDetailsService> delegateServices) {

        this.delegateServices = Optional.ofNullable(delegateServices)
                .orElseThrow(() -> new IllegalArgumentException("User service list must not be null!"))
                .stream()
                .peek(userDetailsService -> Assert.notNull(userDetailsService, "User service list cannot contain null entries. Got " + delegateServices))
                .collect(Collectors.toList());
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        return this.delegateServices
                .stream()
                .map(userDetailsService -> {
                    try {
                        return userDetailsService.loadUserByUsername(username);
                    } catch (UsernameNotFoundException e) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .findFirst()
                .orElseThrow(() -> new UsernameNotFoundException(MessageSourceUtils.getMessage("Security.noUser", new Object[]{username}, "No user with account: " + username)));
    }
}
