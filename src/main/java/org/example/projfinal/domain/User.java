package org.example.projfinal.domain;

import java.util.Objects;

public class User extends Entity<Long>{

    private String username;
    private String realName;
    private RoleEnum role;
    private String password;

    public User(String username, String realName) {
        this.username = username;
        this.realName = realName;
        role = RoleEnum.NORMAL;
        this.password = "pass";
    }

    public User(String username, String realName, String password) {
        this.username = username;
        this.realName = realName;
        role = RoleEnum.NORMAL;
        this.password = password;
    }

    public User(String username, String realName, RoleEnum role, String password){
        this.username = username;
        this.realName = realName;
        this.role = role;
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public RoleEnum getRole() {
        return role;
    }

    public void setRole(RoleEnum role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        User user = (User) o;
        return Objects.equals(username, user.username) && Objects.equals(realName, user.realName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), username, realName);
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", realName='" + realName + '\'' +
                ", id=" + id +
                ", role=" + role +
                ", pass=" + password +
                '}';
    }
}
