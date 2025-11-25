package com.javaweb.entity;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "Users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name="role_id")
    private RoleEntity role;

    @Column(name="user_name")
    private String name;

    @Column(name="user_email", nullable=false, unique=true)
    private String email;

    @Column(name="password", nullable=false)
    private String password;

    @Column(name="status")
    private String status;

    @Column(name="user_gender")
    private String userGender;

    @Column(name="user_dob")
    private Date userDob;

    @Column(name="avatar")
    private String avatar;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public RoleEntity getRole() { return role; }
    public void setRole(RoleEntity role) { this.role = role; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getUserGender() { return userGender; }
    public void setUserGender(String userGender) { this.userGender = userGender; }

    public Date getUserDob() { return userDob; }
    public void setUserDob(Date userDob) { this.userDob = userDob; }

    public String getAvatar() { return avatar; }
    public void setAvatar(String avatar) { this.avatar = avatar; }
}
