package org.example.projfinal.repository.databaserepo;

import org.example.projfinal.domain.RoleEnum;
import org.example.projfinal.domain.User;
import org.example.projfinal.domain.validator.Validator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDBRepo extends AbstractDatabaseRepo<Long, User> {

    public UserDBRepo(Validator<User> validator, String tableName) {
        super(validator, tableName);
    }

    @Override
    protected User extractEntity(ResultSet resultSet) throws SQLException {
        Long id = resultSet.getLong("id");
        String username = resultSet.getString("username");
        String realName = resultSet.getString("realname");
        String roleString = resultSet.getString("role");
        String password = resultSet.getString("password");
        RoleEnum role = RoleEnum.valueOf(roleString);

        User user = new User(username, realName, role, password);
        user.setId(id);

        return user;
    }

    protected String createInsertStatement(User entity) {
        return "insert into " + super.tableName + " (username, realname, role, password) values (" +
                "'" + entity.getUsername() + "', '" +
                entity.getRealName() + "', '" +
                entity.getRole() + "', '" +
                entity.getPassword() + "')";
    }

    @Override
    protected PreparedStatement createInsertPrepared(User entity, Connection connection) throws SQLException {
        String insertSql = "insert into " + super.tableName + " (username, realname, role, password) values (" +
                "'" + entity.getUsername() + "', '" +
                entity.getRealName() + "', '" +
                entity.getRole() + "', '" +
                entity.getPassword() + "')";

        return connection.prepareStatement(insertSql);
    }


    @Override
    protected String createUpdateStatement(User entity) {
        String safeUsername = entity.getUsername().replace("'", "''");
        String safeRealName = entity.getRealName().replace("'", "''");
        String safeRole = entity.getRole().name().replace("'", "''");
        String safePassword = entity.getPassword().replace("'", "''");

        return "update " + super.tableName + " set username = '" + safeUsername +
                "', realname = '" + safeRealName +
                "', role = '" + safeRole +
                "', password = '" + safePassword +
                "' WHERE id = " + entity.getId() + ";";
    }

    @Override
    protected PreparedStatement createUpdatePrepared(User entity, Connection connection) throws SQLException {
        String updateQuery = createUpdateStatement(entity);

        return connection.prepareStatement(updateQuery);
    }

}
