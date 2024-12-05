package org.example.projfinal.repository.databaserepo;

import org.example.projfinal.domain.Group;
import org.example.projfinal.domain.validator.Validator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class GroupDBRepo extends AbstractDatabaseRepo<Long, Group> {

    public GroupDBRepo(Validator<Group> validator, String tableName) {
        super(validator, tableName);
    }

    @Override
    protected Group extractEntity(ResultSet resultSet) throws SQLException {
        Long id = resultSet.getLong("group_id");
        String name = resultSet.getString("group_name");
        String members = resultSet.getString("group_members");
        List<Long> memberIds = Arrays.stream(members.split(";"))
                .map(Long::parseLong)
                .collect(Collectors.toList());
        Group group = new Group(name, memberIds);
        group.setId(id);
        return group;
    }

    @Override
    protected PreparedStatement createInsertPrepared(Group entity, Connection connection) throws SQLException {
        String query = "INSERT INTO " + this.tableName + " (group_name, group_members) VALUES (?, ?)";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, entity.getGroupName());
        String members = entity.getUsers().stream()
                .sorted()
                .map(String::valueOf)
                .collect(Collectors.joining(";"));
        System.out.println(members);
        statement.setString(2, members);
        return statement;
    }

    @Override
    protected String createUpdateStatement(Group entity) {
        return "UPDATE " + this.tableName + " SET group_name = ?, group_members = ? WHERE group_id = ?";
    }

    @Override
    protected PreparedStatement createUpdatePrepared(Group entity, Connection connection) throws SQLException {
        String query = createUpdateStatement(entity);
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, entity.getGroupName());
        String members = entity.getUsers().stream()
                .sorted()
                .map(String::valueOf)
                .collect(Collectors.joining(";"));
        statement.setString(2, members);
        statement.setLong(3, entity.getId());
        return statement;
    }
}