package org.example.service;

import org.example.domain.Friendship;
import org.example.domain.User;
import org.example.domain.validator.ValidationException;
import org.example.repository.Repository;

import java.util.List;
import java.util.stream.StreamSupport;

public class SocialNetworkCrud {

    private final Repository<Long, User> userRepo;
    private final Repository<Long, Friendship> friendRepo;

    public SocialNetworkCrud(Repository<Long, User> userRepo, Repository<Long, Friendship> friendRepo) {
        this.userRepo = userRepo;
        this.friendRepo = friendRepo;
    }


    public Iterable<User> getUses() {
        return userRepo.findAll();
    }

    public User findUser(Long id) {
        return userRepo.findOne(id).orElseThrow(() -> new ValidationException("No user"));
    }

    public Long getNewUserId() {
        return 1 + StreamSupport.stream(userRepo.findAll().spliterator(), false)
                .map(User::getId)
                .reduce(Long::max)
                .orElse(0L);
    }

    public void addUser(User user) {
        user.setId(getNewUserId());
        userRepo.save(user);
    }

    public void deleteUser(User user) {
        try {

            User usr = userRepo.findOne(user.getId()).orElseThrow(
                    () -> new ValidationException("There is no user with this id"));

            List<Long> toDelete = StreamSupport.stream(friendRepo.findAll().spliterator(), false)
                    .filter(friendship -> friendship.getUser2().equals(user.getId()) ||
                            friendship.getUser1().equals(user.getId()))
                    .map(Friendship::getId)
                    .toList();

            toDelete.forEach(friendRepo::delete);

            userRepo.delete(user.getId()).orElseThrow(
                    () -> new ValidationException("There is no user to delete"));

        } catch (IllegalArgumentException e) {
            System.out.println("Invalid user! ");
        } catch (ValidationException v) {
            System.out.println();
        }
    }

    public Iterable<Friendship> getFriendships() {
        return friendRepo.findAll();
    }

    public Friendship findFriendship(Long id) {
        return friendRepo.findOne(id).orElseThrow(() -> new ValidationException("No friend"));
    }

    public Long getNewFriendshipId() {
        return 1 + StreamSupport.stream(friendRepo.findAll().spliterator(), false)
                .map(Friendship::getId)
                .reduce(Long::max)
                .orElse(0L);
    }

    public void addFriendship(Friendship friendship) {
        if (!validateFriendship(friendship.getUser1(), friendship.getUser2())) {
            System.out.println("You cannot add the same friendship twice!");
            return;
        }
        friendship.setId(getNewFriendshipId());
        friendRepo.save(friendship);
    }

    private boolean validateFriendship(Long user1, Long user2) {
        return StreamSupport.stream(friendRepo.findAll().spliterator(), false)
                .noneMatch(friendship -> (
                        friendship.getUser1().equals(user1) && friendship.getUser2().equals(user2) ||
                                friendship.getUser1().equals(user2) && friendship.getUser2().equals(user1)
                ));
    }

    public void deleteFriendship(Friendship friendship) {
        try {
            friendRepo.delete(friendship.getId()).orElseThrow(
                    () -> new ValidationException("There is no friendship to delete"));
        } catch (IllegalArgumentException e) {
            System.err.println("Invalid user! ");
        } catch (ValidationException v) {
            System.err.println(v.getMessage());
        }
    }

}
