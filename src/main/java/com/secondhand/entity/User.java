package com.secondhand.entity;

import java.time.LocalDateTime;

public class User {
    private int userId;           // 用户ID (主键)
    private String username;      // 登录账号（学号）
    private String password;      // 登录密码
    private String nickname;      // 用户昵称
    private String phone;         // 联系电话
    private String address;       // 默认交易/收货地址
    private int role;             // 角色 (0:普通学生, 1:管理员)
    private LocalDateTime createTime; // 注册时间

    public User() {
    }

    public User(int userId, String username, String password, String nickname, String phone, String address, int role, LocalDateTime createTime) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.phone = phone;
        this.address = address;
        this.role = role;
        this.createTime = createTime;
    }

    public int getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getNickname() {
        return nickname;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public int getRole() {
        return role;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", nickname='" + nickname + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", role=" + role +
                ", createTime=" + createTime +
                '}';
    }
}
