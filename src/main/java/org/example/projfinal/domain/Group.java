package org.example.projfinal.domain;

import java.util.List;
import java.util.Objects;

public class Group extends Entity<Long> {

    private List<Long> users;
    private String groupName;


    public Group(String groupName) {
        this.groupName = groupName;
    }

    public Group(String groupName, List<Long> users) {
        this.users = users;
        this.groupName = groupName;
    }

    public List<Long> getUsers() {
        return users;
    }

    public void setUsers(List<Long> users) {
        this.users = users;
    }

    public void addToList(Long id) {
        if (!users.contains(id))
            users.add(id);
    }

    public void removeFromList(Long id) {
        users.remove(id);
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Group group = (Group) o;
        return Objects.equals(users, group.users) && Objects.equals(groupName, group.groupName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), users, groupName);
    }

    @Override
    public String toString() {
        return "Group{" +
                "users=" + users +
                ", groupName='" + groupName + '\'' +
                ", id=" + id +
                '}';
    }
}
