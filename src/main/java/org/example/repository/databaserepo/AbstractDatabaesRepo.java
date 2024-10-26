package org.example.repository.databaserepo;

import org.example.domain.Entity;
import org.example.domain.validator.Validator;
import org.example.repository.InMemoryRepo;
import org.example.repository.Repository;

import java.sql.*;
import java.util.Optional;

public abstract class AbstractDatabaesRepo<ID, E extends Entity<ID>> extends InMemoryRepo<ID, E> {

    private final String tableName;

    public AbstractDatabaesRepo(Validator<E> validator, String tableName) {
        super(validator);
        this.tableName = tableName;
    }

    public static Connection getConnection() throws SQLException {
        String USER = "postgres";
        String URL = "jdbc:postgresql://localhost:5432/socialnetwork";
        String PASSWORD = "postgres";
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    private void loadData() {
        String query = "select * from " + this.tableName;

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                E entity = extractEntity(resultSet);
                super.save(entity);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error loading data from database");
        }
    }

    protected abstract E extractEntity(ResultSet resultSet) throws SQLException;

    protected abstract String createInsertStatement(E entity);


}
