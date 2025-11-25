package com.javaweb.entity;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.Date;

@Entity
@Table(name = "refresh_tokens")
public class RefreshTokenEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false, unique=true, length=500)
    private String token;

    @Column(nullable=false)
    private Date expiryDate;

    @Column(nullable=false)
    private boolean revoked = false;

    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    private UserEntity user;

    public Long getId() { return id; }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public Date getExpiryDate() { return expiryDate; }
    public void setExpiryDate(Date expiryDate) { this.expiryDate = expiryDate; }

    public boolean isRevoked() { return revoked; }
    public void setRevoked(boolean revoked) { this.revoked = revoked; }

    public UserEntity getUser() { return user; }
    public void setUser(UserEntity user) { this.user = user; }
}