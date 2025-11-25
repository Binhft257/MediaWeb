package com.javaweb.security;

import com.javaweb.entity.UserEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;

public class MyUserDetails implements UserDetails {

    private Integer id;
    private String fullName;
    private String email;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;

    private boolean enabled;
    private boolean accountNonExpired = true;
    private boolean credentialsNonExpired = true;
    private boolean accountNonLocked = true;

    public MyUserDetails(UserEntity user, Collection<? extends GrantedAuthority> authorities) {
        this.id = user.getId();
        this.fullName = user.getName();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.authorities = authorities;
        this.enabled = !"inactive".equalsIgnoreCase(user.getStatus());
    }

    public Integer getId() { return id; }
    public String getFullName() { return fullName; }

    @Override public Collection<? extends GrantedAuthority> getAuthorities() { return authorities; }
    @Override public String getPassword() { return password; }
    @Override public String getUsername() { return email; }
    @Override public boolean isAccountNonExpired() { return accountNonExpired; }
    @Override public boolean isAccountNonLocked() { return accountNonLocked; }
    @Override public boolean isCredentialsNonExpired() { return credentialsNonExpired; }
    @Override public boolean isEnabled() { return enabled; }
}
