package org.example.projfinal.domain;

import java.time.LocalDateTime;
import java.util.Objects;

import static java.lang.Math.max;
import static java.lang.Math.min;


public class Friendship extends Entity<Long> {

    private final LocalDateTime date;
    private FriendRequest status;
    Tuple<Long, Long> users;
    private Long fromrequest;

    public Friendship(Long user1, Long user2) {
        users = new Tuple<>(min(user1, user2), max(user1, user2));
        date = LocalDateTime.now();
        status = FriendRequest.PENDING;
    }

    public Friendship(Long user1, Long user2, Long fromrequest) {
        users = new Tuple<>(min(user1, user2), max(user1, user2));
        date = LocalDateTime.now();
        status = FriendRequest.PENDING;
        this.fromrequest = fromrequest;
    }

    public Friendship(Long user1, Long user2, LocalDateTime time, FriendRequest status, Long fromrequest) {
        users = new Tuple<>(min(user1, user2), max(user1, user2));
        date = time;
        this.status = status;
        this.fromrequest = fromrequest;
    }

    public Long getFromrequest() {
        return fromrequest;
    }

    public void setFromrequest(Long fromrequest) {
        this.fromrequest = fromrequest;
    }

    public FriendRequest getStatus() {
        return status;
    }

    public void setStatus(FriendRequest status) {
        this.status = status;
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
                ", status=" + status +
                ", users=" + users +
                ", id=" + id +
                '}';
    }
}
