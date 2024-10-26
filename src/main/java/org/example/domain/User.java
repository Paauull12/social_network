package org.example.domain;

import java.util.Objects;

public class User extends Entity<Long>{

    private String username;
    private String realName;

    public User(String username, String realName) {
        this.username = username;
        this.realName = realName;
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
                '}';
    }
}
