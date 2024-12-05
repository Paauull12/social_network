package org.example.projfinal.repository.databaserepo;

import org.example.projfinal.domain.Entity;
import org.example.projfinal.domain.validator.Validator;
import org.example.projfinal.repository.Repository;
import org.example.projfinal.repository.filters.Filter;
import org.example.projfinal.utils.paging.Page;
import org.example.projfinal.utils.paging.Pageable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class AbstractDatabaseRepo<ID, E extends Entity<ID>> implements Repository<ID, E> {

    protected final String tableName;
    protected final Validator<E> validator;

    public AbstractDatabaseRepo(Validator<E> validator, String tableName) {
        this.tableName = tableName;
        this.validator = validator;
    }

    protected abstract E extractEntity(ResultSet resultSet) throws SQLException;

    protected abstract PreparedStatement createInsertPrepared(E entity, Connection connection) throws SQLException;

    protected abstract String createUpdateStatement(E entity);

    protected abstract PreparedStatement createUpdatePrepared(E entity, Connection connection) throws SQLException;

    @Override
    public Optional<E> findOne(ID id) {
        String query = "select * from " + this.tableName + " where id = ?";

        Connection connection = DatabaseConnection.INSTANCE.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setObject(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                E entity = extractEntity(resultSet);
                return Optional.of(entity);
            } else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error loading data from database");
        }
    }

    @Override
    public Iterable<E> findAll() {
        String query = "select * from " + this.tableName;

        Connection connection = DatabaseConnection.INSTANCE.getConnection();

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();

            List<E> rez = new ArrayList<>();

            while (resultSet.next()) {
                rez.add(extractEntity(resultSet));
            }

            return rez;

        } catch (SQLException e) {
            throw new RuntimeException("Error loading data from database");
        }
    }

    @Override
    public Optional<E> save(E entity) {
        this.validator.validate(entity);

        Connection connection = DatabaseConnection.INSTANCE.getConnection();

        try (PreparedStatement statement = createInsertPrepared(entity, connection)) {
            statement.executeUpdate();

            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Error saving entity to the database" +  e);
        }
    }

    @Override
    public Optional<E> delete(ID id) {
        Optional<E> entity = findOne(id);
        if (entity.isEmpty()) {
            return Optional.empty();
        }

        String query = "DELETE FROM " + this.tableName + " WHERE id = ?";

        Connection connection = DatabaseConnection.INSTANCE.getConnection();

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setObject(1, id);
            statement.executeUpdate();

            return entity;
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting data from database" + e);
        }
    }

    @Override
    public Optional<E> update(E entity) {
        this.validator.validate(entity);

        Optional<E> currentEntity = findOne(entity.getId());
        if (currentEntity.isEmpty()) {
            return Optional.empty();
        }

        Connection connection = DatabaseConnection.INSTANCE.getConnection();

        try {
            PreparedStatement statement = createUpdatePrepared(entity, connection);
            statement.executeUpdate();

            return currentEntity;
        } catch (SQLException e) {
            throw new RuntimeException("Error updating data from database", e);
        }
    }

    public Page<E> findAllOnPage(Pageable pageable, Filter filter) {
        String query = "SELECT * FROM " + this.tableName + filter.getFiltersSql() + " LIMIT ? OFFSET ?";

        Connection connection = DatabaseConnection.INSTANCE.getConnection();

        //System.out.println(query);

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, pageable.getPageSize());
            statement.setInt(2, pageable.getPageSize() * pageable.getPageNumber());
            ResultSet resultSet = statement.executeQuery();

            List<E> entities = new ArrayList<>();
            while (resultSet.next()) {
                entities.add(extractEntity(resultSet));
            }

            int totalElements = countTotalElements(filter);

            return new Page<>(entities, totalElements);
        } catch (SQLException e) {
            throw new RuntimeException("Error loading data from database", e);
        }
    }

    private int countTotalElements(Filter filter) {
        String query = "SELECT COUNT(*) FROM " + this.tableName + filter.getFiltersSql();

        Connection connection = DatabaseConnection.INSTANCE.getConnection();

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            } else {
                throw new RuntimeException("Error counting total elements in database");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error counting total elements in database", e);
        }
    }
}