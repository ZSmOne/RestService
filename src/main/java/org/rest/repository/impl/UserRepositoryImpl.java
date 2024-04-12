package org.rest.repository.impl;

import org.rest.db.ConnectionManager;
import org.rest.db.ConnectionManagerImpl;
import org.rest.exception.RepositoryException;
import org.rest.model.User;
import org.rest.repository.CityRepository;
import org.rest.repository.UserRepository;
import org.rest.repository.UserToBankRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserRepositoryImpl implements UserRepository {

    private static final String SAVE_SQL = """
            INSERT INTO users (user_name, city_id)
            VALUES (?, ?) ;
            """;
    private static final String FIND_BY_ID_SQL = """
            SELECT * FROM users
            WHERE user_id = ?
            LIMIT 1;
            """;
    private static final String UPDATE_SQL = """
            UPDATE users
            SET user_name = ?,
                city_id =?
            WHERE user_id = ?  ;
            """;
    private static final String DELETE_SQL = """
            DELETE FROM users
            WHERE user_id = ? ;
            """;
    private static final String FIND_ALL_SQL = """
            SELECT * FROM users;
            """;
    private static final String EXIST_BY_ID_SQL = """
                SELECT exists (
                SELECT 1
                    FROM users
                        WHERE user_id = ?
                        LIMIT 1);
            """;
    private ConnectionManager connectionManager = ConnectionManagerImpl.getInstance();
    private static UserRepository instance;
    private final CityRepository cityRepository = CityRepositoryImpl.getInstance();
    private final UserToBankRepository userToBankRepository = UserToBankRepositoryImpl.getInstance();

    public UserRepositoryImpl() {
    }
    public UserRepositoryImpl(String url,String username, String password){
        connectionManager = new ConnectionManagerImpl(url, username, password);
    }
    public static synchronized UserRepository getInstance() {
        if (instance == null) {
            instance = new UserRepositoryImpl();
        }
        return instance;
    }

    @Override
    public User save(User user) {
        try(Connection connection = connectionManager.getConnection();
            PreparedStatement preparedStatement =connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, user.getName());
            preparedStatement.setLong(2, user.getCity().getId());
            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                user = new User(resultSet.getLong("user_id"), user.getName(), user.getCity(), null);
            }
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
        return user;
    }

    @Override
    public User findById(Long id) {
        User user = null;
        try(Connection connection = connectionManager.getConnection();
            PreparedStatement preparedStatement =connection.prepareStatement(FIND_BY_ID_SQL)){
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next())
                user = new User(resultSet.getLong("user_id"), resultSet.getString("user_name"),
                        cityRepository.findById(resultSet.getLong("city_id")),
                        userToBankRepository.findBanksByUserId(resultSet.getLong("user_id")));
        }catch (SQLException e){
            throw new RepositoryException(e);
        }
        return user;
    }

    @Override
    public List<User> findAll() {
        List<User> userList = new ArrayList<>();
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_SQL)) {

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                userList.add(new User(resultSet.getLong("user_id"), resultSet.getString("user_name"),
                        cityRepository.findById(resultSet.getLong("city_id")), null));
            }
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
        return userList;
    }

    @Override
    public void update(User user) {
        try(Connection connection = connectionManager.getConnection();
            PreparedStatement preparedStatement =connection.prepareStatement(UPDATE_SQL)){
            preparedStatement.setString(1, user.getName());
            preparedStatement.setLong(2, user.getCity().getId());
            preparedStatement.setLong(3, user.getId());
            preparedStatement.executeUpdate();
        }catch (SQLException e){
            throw new RepositoryException(e);
        }
    }

    @Override
    public boolean deleteById(Long id) {
        boolean deleteResult;
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_SQL);) {
            userToBankRepository.deleteByUserId(id);
            preparedStatement.setLong(1, id);
            deleteResult = preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
        return deleteResult;
    }

    @Override
    public boolean existById(Long id) {
        boolean isExists = false;
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(EXIST_BY_ID_SQL)) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                isExists = resultSet.getBoolean(1);
            }
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
        return isExists;
    }
}
