package org.example.domain;

import java.time.LocalDateTime;
import java.util.Objects;

import static java.lang.Math.max;
import static java.lang.Math.min;


public class Friendship extends Entity<Long>{

    private final LocalDateTime date;

    Tuple<Long, Long> users;

    public Friendship(Long user1, Long user2) {
        users = new Tuple<>(min(user1, user2), max(user1, user2));
        date = LocalDateTime.now();
    }

    public LocalDateTime getDate() {
        return date;
    }

    public Long getUser1() {
        return users.getLeft();
    }

    public Long getUser2() {
        return users.getRight();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Friendship that = (Friendship) o;
        return Objects.equals(date, that.date) && Objects.equals(users, that.users);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), date, users);
    }

    @Override
    public String toString() {
        return "Friendship{" +
                "date=" + date +
                ", users=" + users +
                ", id=" + id +
                '}';
    }
}
