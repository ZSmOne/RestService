package org.rest.repository.impl;

import org.rest.db.ConnectionManager;
import org.rest.db.ConnectionManagerImpl;
import org.rest.exception.RepositoryException;
import org.rest.model.Bank;
import org.rest.repository.BankRepository;
import org.rest.repository.UserToBankRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BankRepositoryImpl implements BankRepository {

    private static final String SAVE_SQL = """
            INSERT INTO banks (bank_name)
            VALUES (?);
            """;
    private static final String FIND_BY_ID_SQL = """
            SELECT * FROM banks
            WHERE bank_id = ?
            LIMIT 1;
            """;
    private static final String FIND_ALL_SQL = """
            SELECT * FROM banks;
            """;
    private static final String UPDATE_SQL = """
            UPDATE banks
            SET bank_name = ?
            WHERE bank_id = ?;
            """;
    private static final String DELETE_SQL = """
            DELETE FROM banks
            WHERE bank_id = ?;
            """;
    private static final String EXIST_BY_ID_SQL = """
                SELECT exists (
                SELECT 1
                    FROM banks
                        WHERE bank_id = ?
                        LIMIT 1);
            """;

    private ConnectionManager connectionManager = ConnectionManagerImpl.getInstance();
    private final UserToBankRepository userToBankRepository = UserToBankRepositoryImpl.getInstance();
    private static BankRepository instance;


    public BankRepositoryImpl() {
    }

    public BankRepositoryImpl(String url,String username, String password){
        connectionManager = new ConnectionManagerImpl(url, username, password);
    }

    public static synchronized BankRepository getInstance() {
        if (instance == null) {
            instance = new BankRepositoryImpl();
        }
        return instance;
    }
    @Override
    public Bank save(Bank bank) {
        try(Connection connection = connectionManager.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS)){
            preparedStatement.setString(1, bank.getName());
            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next())
                bank = new Bank(resultSet.getLong("bank_id"), bank.getName(), null);
        }catch (SQLException e){
            throw new RepositoryException(e);
        }
        return bank;
    }

    @Override
    public void update(Bank bank) {
        try(Connection connection = connectionManager.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_SQL)){
            preparedStatement.setString(1, bank.getName());
            preparedStatement.setLong(2, bank.getId());
            preparedStatement.executeUpdate();
        }catch (SQLException e){
            throw new RepositoryException(e);
        }
    }

    @Override
    public Bank findById(Long id) {
        Bank bank = null;
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ID_SQL)){
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                bank = new Bank(resultSet.getLong("bank_id"), resultSet.getString("bank_name"), null);

            }
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
        return bank;
    }

    @Override
    public boolean deleteById(Long id) {
        boolean deleteResult;
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_SQL)){
            userToBankRepository.deleteByUserId(id);
            preparedStatement.setLong(1, id);
            deleteResult = preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
        return deleteResult;
    }

    @Override
    public List<Bank> findAll() {
        List<Bank> bankList = new ArrayList<>();
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_SQL)){
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next())
                bankList.add(new Bank(resultSet.getLong("bank_id"), resultSet.getString("bank_name"), null));
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
        return bankList;
    }

    @Override
    public boolean existById(Long id) {
        boolean isExists = false;
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(EXIST_BY_ID_SQL)){
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

