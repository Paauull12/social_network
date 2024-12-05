package org.example.projfinal.service;

import org.example.projfinal.domain.*;
import org.example.projfinal.repository.Repository;
import org.example.projfinal.repository.filters.UserFilter;
import org.example.projfinal.utils.observer.Observable;
import org.example.projfinal.utils.observer.Observer;
import org.example.projfinal.utils.paging.Page;
import org.example.projfinal.utils.paging.Pageable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Network implements Observable {

    private final Repository<Long, User> userRepo;
    private final Repository<Long, Friendship> friendRepo;
    private final Repository<Long, Group> groupRepo;
    private final Repository<Long, Message> messages;
    private final List<Observer> observers = new ArrayList<>();

    public Network(Repository<Long, User> userRepo, Repository<Long, Friendship> friendRepo, Repository<Long, Group> groupRepo, Repository<Long, Message> messages) {
        this.userRepo = userRepo;
        this.friendRepo = friendRepo;
        this.groupRepo = groupRepo;
        this.messages = messages;
        this.checkIntegrity();
    }

    private void checkIntegrity() {}

    public Iterable<User> getUsers() {
        return userRepo.findAll();
    }

    public Iterable<Friendship> getFriendships() {
        return friendRepo.findAll();
    }

    public Iterable<Group> getGroups() {
        return groupRepo.findAll();
    }

    public List<Group> getGroupsList(Long id) {
        List<Group> groups = new ArrayList<>();
        for (Group group : groupRepo.findAll()) {
            if(group.getUsers().contains(id))
                groups.add(group);
        }
        return groups;
    }

    public Iterable<Message> getMessages() {
        return messages.findAll();
    }

    public List<Message> getMessagesList(Long id) {
        List<Message> messages = new ArrayList<>();
        for (Message message : this.messages.findAll()) {
            if (message.getGroupId().equals(id)) {
                messages.add(message);
            }
        }
        return messages.stream().sorted(Comparator.comparing(Message::getDate)).toList();
    }

    public Group getGroupByName(String name){
        for (Group group : groupRepo.findAll()) {
            if(group.getGroupName().equals(name))
                return group;
        }
        return null;
    }

    public List<User> getFriendRequests(Long id){
        List<User> users = new ArrayList<>();
        for (Friendship friendship : friendRepo.findAll()) {
            if(friendship.getStatus() == FriendRequest.PENDING && (friendship.getUser2().equals(id) || friendship.getUser1().equals(id)) && !friendship.getFromrequest().equals(id))
                users.add(userRepo.findOne(friendship.getUser1()).orElse(null));
        }
        System.out.println(users);
        return users;
    }

    public User getUser(Long id) {
        return userRepo.findOne(id).orElse(null);
    }

    public Friendship getFriendship(Long id) {
        return friendRepo.findOne(id).orElse(null);
    }

    public Group getGroup(Long id) {
        return groupRepo.findOne(id).orElse(null);
    }

    public Message getMessage(Long id) {
        return messages.findOne(id).orElse(null);
    }

    public void addUser(User user) throws Exception {

        try{
            userRepo.save(user);
        }catch (Exception e){
            throw new Exception("Username already exists");
        }
        notifyObservers();
    }

    public void addFriendship(Friendship friendship) {
        friendRepo.save(friendship);
        notifyObservers();
    }

    public void addGroup(Group group) {
        groupRepo.save(group);
        notifyObservers();
    }

    public void addMessage(Message message) {
        messages.save(message);
        notifyObservers();
    }

    public void removeUser(Long id) {
        userRepo.delete(id);
        notifyObservers();
    }

    public void removeFriendship(Long id) {
        friendRepo.delete(id);
        notifyObservers();
    }

    public void removeGroup(Long id) {
        groupRepo.delete(id);
        notifyObservers();
    }

    public void removeMessage(Long id) {
        messages.delete(id);
        notifyObservers();
    }

    public void updateUser(User user) {
        userRepo.update(user);
        notifyObservers();
    }

    public void updateFriendship(Friendship friendship) {
        friendRepo.update(friendship);
        notifyObservers();
    }

    public void updateGroup(Group group) {
        groupRepo.update(group);
        notifyObservers();
    }

    public void updateMessage(Message message) {
        messages.update(message);
        notifyObservers();
    }

    public List<User> getPotentialFriends(Long id){
        List<User> currentFriends = getFriends(id);

        List<User> potentialFriends = new ArrayList<>();

        for (User user : userRepo.findAll()) {
            if (!currentFriends.contains(user) && !user.getId().equals(id)) {
                potentialFriends.add(user);
            }
        }

        potentialFriends.forEach(System.out::println);
        return potentialFriends;
    }

    public List<User> getFriends(Long id) {
        List<User> friends = new ArrayList<>();
        for (Friendship friendship : friendRepo.findAll()) {
            if (friendship.getUser1().equals(id) && friendship.getStatus() == FriendRequest.ACCEPTED) {
                friends.add(userRepo.findOne(friendship.getUser2()).orElse(null));
            } else if (friendship.getUser2().equals(id) && friendship.getStatus() == FriendRequest.ACCEPTED) {
                friends.add(userRepo.findOne(friendship.getUser1()).orElse(null));
            }
        }
        friends.forEach(System.out::println);
        return friends;
    }

    public int countFriendRequests(Long id) {
        int count = 0;
        for (Friendship friendship : friendRepo.findAll()) {
            if ((friendship.getUser2().equals(id) || friendship.getUser1().equals(id)) && friendship.getStatus() == FriendRequest.PENDING && !friendship.getFromrequest().equals(id)) {
                count++;
            }
        }
        return count;
    }

    public Friendship getFriendship(Long user1, Long user2) {
        for (Friendship friendship : friendRepo.findAll()) {
            if ((friendship.getUser1().equals(user1) && friendship.getUser2().equals(user2)) || (friendship.getUser1().equals(user2) && friendship.getUser2().equals(user1))) {
                return friendship;
            }
        }
        return null;
    }

    public List<Group> getGroupsForUser(Long id){
        List<Group> groups = new ArrayList<>();
        for (Group group : groupRepo.findAll()) {
            if (group.getUsers().contains(id)) {
                groups.add(group);
            }
        }
        groups.forEach(System.out::println);
        return groups;
    }

    public List<Message> getMessageInAGroup(Long group_id) {
        List<Message> messages = new ArrayList<>();
        for (Message message : this.messages.findAll()) {
            if (message.getGroupId().equals(group_id)) {
                messages.add(message);
            }
        }
        messages.forEach(System.out::println);
        return messages;
    }

    public  User getUserByUsername(String username) {
        for (User user : userRepo.findAll()) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    public Page<User> getPage(int page, int pageSize, UserFilter filter) {
        return userRepo.findAllOnPage(new Pageable(page, pageSize), filter);
    }



    @Override
    public void addObserver(Observer e) {
        observers.add(e);
    }

    @Override
    public void removeObserver(Observer e) {
        observers.remove(e);
    }

    @Override
    public void notifyObservers() {
        observers.forEach(Observer::update);
    }
}
