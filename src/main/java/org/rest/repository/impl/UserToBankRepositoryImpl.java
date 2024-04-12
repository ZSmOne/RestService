package org.rest.repository.impl;

import org.rest.db.ConnectionManager;
import org.rest.db.ConnectionManagerImpl;
import org.rest.exception.RepositoryException;
import org.rest.model.Bank;
import org.rest.model.User;
import org.rest.model.UserToBank;
import org.rest.repository.BankRepository;
import org.rest.repository.UserRepository;
import org.rest.repository.UserToBankRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class UserToBankRepositoryImpl implements UserToBankRepository {
    private static final String SAVE_SQL = """
            INSERT INTO users_banks (user_id, bank_id)
            VALUES (?, ?);
            """;
    private static final String UPDATE_SQL = """
            UPDATE users_banks
            SET user_id = ?,
                bank_id = ?
            WHERE users_banks_id = ?;
            """;
    private static final String DELETE_SQL = """
            DELETE FROM users_banks
            WHERE users_banks_id = ? ;
            """;
    private static final String FIND_BY_ID_SQL = """
            SELECT * FROM users_banks
            WHERE users_banks_id = ?
            LIMIT 1;
            """;
    private static final String FIND_ALL_SQL = """
            SELECT * FROM users_banks;
            """;
    private static final String FIND_ALL_BY_USER_ID_SQL = """
            SELECT * FROM users_banks
            WHERE user_id = ?;
            """;
    private static final String FIND_ALL_BY_BANK_ID_SQL = """
            SELECT * FROM users_banks
            WHERE bank_id = ?;
            """;
    private static final String FIND_BY_USERID_AND_BANK_ID_SQL = """
            SELECT * FROM users_banks
            WHERE user_id = ? AND bank_id = ?
            LIMIT 1;
            """;
    private static final String DELETE_BY_USER_ID_SQL = """
            DELETE FROM users_banks
            WHERE user_id = ?;
            """;
    private static final String DELETE_BY_BANK_ID_SQL = """
            DELETE FROM users_banks
            WHERE bank_id = ?;
            """;
    private static final String EXIST_BY_ID_SQL = """
                SELECT exists (
                SELECT 1
                    FROM users_banks
                        WHERE users_banks_id = ?
                        LIMIT 1);
            """;
    private ConnectionManager connectionManager = ConnectionManagerImpl.getInstance();
    private static final UserRepository userRepository = UserRepositoryImpl.getInstance();
    private static final BankRepository bankRepository = BankRepositoryImpl.getInstance();

    private static UserToBankRepository instance;

    public UserToBankRepositoryImpl() {
    }
    public UserToBankRepositoryImpl(String url,String username, String password){
        connectionManager = new ConnectionManagerImpl(url, username, password);
    }

    public static synchronized UserToBankRepository getInstance() {
        if (instance == null) {
            instance = new UserToBankRepositoryImpl();
        }
        return instance;
    }

    @Override
    public UserToBank save(UserToBank userToBank) {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setLong(1, userToBank.getUserId());
            preparedStatement.setLong(2, userToBank.getBankId());
            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                userToBank = new UserToBank(resultSet.getLong("users_banks_id"), userToBank.getUserId(), userToBank.getBankId());
            }
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
        return userToBank;
    }

    @Override
    public UserToBank findById(Long id) {
        UserToBank userToBank = null;
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ID_SQL)) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                userToBank = new UserToBank(resultSet.getLong("users_banks_id"), resultSet.getLong("user_id"),
                        resultSet.getLong("bank_id"));
            }
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
        return userToBank;
    }

    @Override
    public List<UserToBank> findAll() {
        List<UserToBank> userToBankList = new ArrayList<>();
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_SQL)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                userToBankList.add(new UserToBank(resultSet.getLong("users_banks_id"),
                        resultSet.getLong("user_id"),
                        resultSet.getLong("bank_id")));
            }
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
        return userToBankList;
    }

    @Override
    public List<UserToBank> findAllByBankId(Long bankId) {
        List<UserToBank> userToBankList = new ArrayList<>();
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_BY_BANK_ID_SQL)) {
            preparedStatement.setLong(1, bankId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                userToBankList.add(new UserToBank(resultSet.getLong("users_banks_id"),
                        resultSet.getLong("user_id"),
                        resultSet.getLong("bank_id")));

            }
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
        return userToBankList;
    }

    @Override
    public List<Bank> findBanksByUserId(Long userId) {
        List<Bank> bankList = new ArrayList<>();
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_BY_USER_ID_SQL)) {
            preparedStatement.setLong(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                bankList.add(bankRepository.findById(resultSet.getLong("bank_id")));
            }
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
        return bankList;
    }

    @Override
    public List<User> findUsersByBankId(Long bankId) {
        List<User> userList = new ArrayList<>();
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_BY_BANK_ID_SQL)) {

            preparedStatement.setLong(1, bankId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                User user = userRepository.findById(resultSet.getLong("user_id"));
                if (user != null) {

                userList.add(user);
                }
            }
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
        return userList;
    }

    @Override
    public void update(UserToBank userToBank) {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_SQL)) {
            preparedStatement.setLong(1, userToBank.getUserId());
            preparedStatement.setLong(2, userToBank.getBankId());
            preparedStatement.setLong(3, userToBank.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
    }

    @Override
    public boolean deleteById(Long id) {
        boolean deleteResult;
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_SQL)) {
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
            if (resultSet.next())
                isExists = resultSet.getBoolean(1);
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
        return isExists;
    }

    @Override
    public boolean deleteByUserId(Long userId) {
        boolean deleteResult;
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_BY_USER_ID_SQL)) {
            preparedStatement.setLong(1, userId);
            deleteResult = preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
        return deleteResult;
    }

    @Override
    public boolean deleteByBankId(Long bankId) {
        boolean deleteResult;
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_BY_USER_ID_SQL)) {
            preparedStatement.setLong(1, bankId);
            deleteResult = preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
        return deleteResult;
    }

    @Override
    public List<UserToBank> findAllByUserId(Long userId) {
        List<UserToBank> userToBankList = new ArrayList<>();
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_BY_USER_ID_SQL)) {
            preparedStatement.setLong(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next())
                userToBankList.add(new UserToBank(resultSet.getLong("users_banks_id"),
                        userId,
                        resultSet.getLong("bank_id")));
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
        return userToBankList;
    }

    @Override
    public UserToBank findByUserIdAndBankId(Long userId, Long bankId) {
        UserToBank userToBank = null;
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_USERID_AND_BANK_ID_SQL)) {
            preparedStatement.setLong(1, userId);
            preparedStatement.setLong(2, bankId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                userToBank = new UserToBank(resultSet.getLong("users_banks_id"), userId, bankId);
            }
        }catch (SQLException e) {
            throw new RepositoryException(e);
        }
        return userToBank;
    }
}
