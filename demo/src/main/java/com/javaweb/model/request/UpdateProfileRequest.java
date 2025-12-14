package com.javaweb.model.request;

import java.util.Date;

public class UpdateProfileRequest {

    private String name;
    private String userGender;
    private Date userDob;
    private String avatar;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getUserGender() {
        return userGender;
    }

    public void setUserGender(String userGender) {
        this.userGender = userGender;
    }

    public Date getUserDob() { return userDob; }
    public void setUserDob(Date userDob) { this.userDob = userDob; }

    public String getAvatar() { return avatar; }
    public void setAvatar(String avatar) { this.avatar = avatar; }
}
