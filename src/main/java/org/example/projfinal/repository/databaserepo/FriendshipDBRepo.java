package org.example.projfinal.repository.databaserepo;


import org.example.projfinal.domain.FriendRequest;
import org.example.projfinal.domain.Friendship;
import org.example.projfinal.domain.validator.Validator;

import java.sql.*;

public class FriendshipDBRepo extends AbstractDatabaseRepo<Long, Friendship> {

    public FriendshipDBRepo(Validator<Friendship> validator, String tableName) {
        super(validator, tableName);
    }

    @Override
    protected Friendship extractEntity(ResultSet resultSet) throws SQLException {
        Long id = resultSet.getLong("id");
        Long user1 = resultSet.getLong("user1");
        Long user2 = resultSet.getLong("user2");
        Timestamp time = resultSet.getTimestamp("datefriendship");
        String statusString = resultSet.getString("state");
        Long fromrequest = resultSet.getLong("requestfrom");

        FriendRequest status;
        if (statusString.equals("ACCEPTED")) {
            status = FriendRequest.ACCEPTED;
        } else if (statusString.equals("PENDING"))
            status = FriendRequest.PENDING;
        else
            status = FriendRequest.DENIED;

        Friendship friendship = new Friendship(user1, user2, time.toLocalDateTime(), status, fromrequest);
        friendship.setId(id);

        return friendship;
    }

    protected String createInsertStatement(Friendship entity) {
        Timestamp sqlDate = Timestamp.valueOf(entity.getDate());
        String formattedDate = "'" + sqlDate + "'";

        return "INSERT INTO " + super.tableName + " (user1, user2, datefriendship, fromrequest) VALUES (" +
                entity.getUser1() + ", " +
                entity.getUser2() + ", " +
                formattedDate + ", "+
                entity.getFromrequest() + ")";
    }

    @Override
    protected PreparedStatement createInsertPrepared(Friendship entity, Connection connection) throws SQLException {
        String sqlQuery = "INSERT INTO " + super.tableName + " (user1, user2, datefriendship, state, requestfrom) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
        preparedStatement.setLong(1, entity.getUser1());
        preparedStatement.setLong(2, entity.getUser2());
        preparedStatement.setTimestamp(3, Timestamp.valueOf(entity.getDate()));
        preparedStatement.setString(4, entity.getStatus().toString());
        preparedStatement.setLong(5, entity.getFromrequest());
        return preparedStatement;
    }

    @Override
    protected String createUpdateStatement(Friendship entity) {
        Timestamp sqlDate = Timestamp.valueOf(entity.getDate());
        String formattedDate = "'" + sqlDate + "'";

        return "UPDATE " + super.tableName + " SET user1 = " + entity.getUser1() +
                ", user2 = " + entity.getUser2() +
                ", datefriendship = " + formattedDate +
                ", state = '" + entity.getStatus() + "'" +
                ", requestfrom = " + entity.getFromrequest() +
                " WHERE id = " + entity.getId() + ";";
    }

    @Override
    protected PreparedStatement createUpdatePrepared(Friendship entity, Connection connection) throws SQLException {
        String updateQuery = createUpdateStatement(entity);

        return connection.prepareStatement(updateQuery);
    }
}
