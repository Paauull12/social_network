package org.example.projfinal.repository.databaserepo;

import org.example.projfinal.domain.Message;
import org.example.projfinal.domain.MessageType;
import org.example.projfinal.domain.validator.Validator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class MessageDBRepo extends AbstractDatabaseRepo<Long, Message> {

    public MessageDBRepo(Validator<Message> validator, String tableName) {
        super(validator, tableName);
    }

    @Override
    protected Message extractEntity(ResultSet resultSet) throws SQLException {
        Long id = resultSet.getLong("id");
        Long userId = resultSet.getLong("userId");
        String message = resultSet.getString("message");
        Timestamp date = resultSet.getTimestamp("date");
        String typeMsg = resultSet.getString("typeMsg");
        Long groupId = resultSet.getLong("groupId");
        if (resultSet.wasNull()) {
            throw new SQLException("groupId is null");
        }
        MessageType messageType = MessageType.valueOf(typeMsg);
        Message msg = new Message(userId, message, date.toLocalDateTime(), messageType, groupId);
        msg.setId(id);
        return msg;
    }

    @Override
    protected PreparedStatement createInsertPrepared(Message entity, Connection connection) throws SQLException {
        String query = "INSERT INTO " + this.tableName + " (userId, message, date, typeMsg, groupId) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setLong(1, entity.getUserId());
        statement.setString(2, entity.getMessage());
        statement.setTimestamp(3, Timestamp.valueOf(entity.getDate()));
        statement.setString(4, entity.getTypeMsg().name());
        if (entity.getGroupId() != null) {
            statement.setLong(5, entity.getGroupId());
        } else {
            statement.setNull(5, java.sql.Types.BIGINT);
        }
        return statement;
    }

    @Override
    protected String createUpdateStatement(Message entity) {
        return "UPDATE " + this.tableName + " SET userId = ?, message = ?, date = ?, typeMsg = ?, groupId = ? WHERE id = ?";
    }

    @Override
    protected PreparedStatement createUpdatePrepared(Message entity, Connection connection) throws SQLException {
        String query = createUpdateStatement(entity);
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setLong(1, entity.getUserId());
        statement.setString(2, entity.getMessage());
        statement.setTimestamp(3, Timestamp.valueOf(entity.getDate()));
        statement.setString(4, entity.getTypeMsg().name());
        if (entity.getGroupId() != null) {
            statement.setLong(5, entity.getGroupId());
        } else {
            statement.setNull(5, java.sql.Types.BIGINT);
        }
        statement.setLong(6, entity.getId());
        return statement;
    }
}