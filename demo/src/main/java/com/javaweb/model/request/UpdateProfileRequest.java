package com.javaweb.model.request;

import java.util.Date;

public class UpdateProfileRequest {

    private String name;
    private String gender;
    private Date userDob;
    private String avatar;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public Date getUserDob() { return userDob; }
    public void setUserDob(Date userDob) { this.userDob = userDob; }

    public String getAvatar() { return avatar; }
    public void setAvatar(String avatar) { this.avatar = avatar; }
}
