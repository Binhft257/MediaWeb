package com.javaweb.security;

import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {

    public static MyUserDetails getPrincipal() {
        return (MyUserDetails) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
    }
}
