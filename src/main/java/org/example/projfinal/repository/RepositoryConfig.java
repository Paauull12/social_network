package org.example.projfinal.repository;


import org.example.projfinal.domain.Friendship;
import org.example.projfinal.domain.Group;
import org.example.projfinal.domain.Message;
import org.example.projfinal.domain.User;
import org.example.projfinal.domain.validator.FriendshipValidation;
import org.example.projfinal.domain.validator.GroupValidation;
import org.example.projfinal.domain.validator.MessageValidation;
import org.example.projfinal.domain.validator.UserValidation;
import org.example.projfinal.repository.databaserepo.FriendshipDBRepo;
import org.example.projfinal.repository.databaserepo.GroupDBRepo;
import org.example.projfinal.repository.databaserepo.MessageDBRepo;
import org.example.projfinal.repository.databaserepo.UserDBRepo;

public enum RepositoryConfig {

    DB_BASED(
            new UserDBRepo(new UserValidation(), "users"),
            new FriendshipDBRepo(new FriendshipValidation(), "friendship"),
            new GroupDBRepo(new GroupValidation(), "groups"),
            new MessageDBRepo(new MessageValidation(), "messages")
    );

    private final Repository<Long, User> userRepo;
    private final Repository<Long, Friendship> friendRepo;
    private final Repository<Long, Group> groupRepo;
    private final Repository<Long, Message> messages;

    RepositoryConfig(Repository<Long, User> userRepo, Repository<Long, Friendship> friendRepo, Repository<Long, Group> groupRepo, Repository<Long, Message> messages) {
        this.userRepo = userRepo;
        this.friendRepo = friendRepo;
        this.groupRepo = groupRepo;
        this.messages = messages;
    }

    public Repository<Long, User> getUserRepo() {
        return userRepo;
    }

    public Repository<Long, Friendship> getFriendRepo() {
        return friendRepo;
    }

    public Repository<Long, Group> getGroupRepo() {
        return groupRepo;
    }

    public Repository<Long, Message> getMessageRepo() {
        return messages;
    }
}
