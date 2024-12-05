package org.example.projfinal.domain;

import java.time.LocalDateTime;
import java.util.Objects;

public class Message extends Entity<Long> {

    private Long userId;
    private String message;
    private LocalDateTime date;
    private MessageType typeMsg;
    private Long groupId = null;

    public Message(Long userId, String message) {
        this.userId = userId;
        this.message = message;
    }

    public Message(Long userId, String message, Long groupId) {
        this.userId = userId;
        this.message = message;
        this.groupId = groupId;
        date = LocalDateTime.now();
        typeMsg = MessageType.NOTSEEN;
    }

    public Message(Long userId, String message, LocalDateTime date, MessageType typeMsg, Long groupId) {
        this.userId = userId;
        this.message = message;
        this.date = date;
        this.typeMsg = typeMsg;
        this.groupId = groupId;
    }

    public Message(Long userId, String message, LocalDateTime date) {
        this.userId = userId;
        this.message = message;
        this.date = date;
    }

    public MessageType getTypeMsg() {
        return typeMsg;
    }

    public void setTypeMsg(MessageType typeMsg) {
        this.typeMsg = typeMsg;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Message{" +
                "userId=" + userId +
                ", message='" + message + '\'' +
                ", date=" + date +
                ", id=" + id +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Message message1 = (Message) o;
        return Objects.equals(userId, message1.userId) && Objects.equals(message, message1.message) && Objects.equals(date, message1.date) && typeMsg == message1.typeMsg && Objects.equals(groupId, message1.groupId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), userId, message, date, typeMsg, groupId);
    }
}
