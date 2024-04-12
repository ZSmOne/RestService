package org.rest.repository.impl;

import org.rest.db.ConnectionManager;
import org.rest.db.ConnectionManagerImpl;
import org.rest.exception.RepositoryException;
import org.rest.model.City;
import org.rest.repository.CityRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CityRepositoryImpl implements CityRepository {
    private static final String SAVE_SQL = """
            INSERT INTO cities (city_name)
            VALUES (?);
            """;
    private static final String FIND_BY_ID_SQL = """
            SELECT * FROM cities
            WHERE city_id = ?
            LIMIT 1;
            """;
    private static final String FIND_ALL_SQL = """
            SELECT * FROM cities;
            """;
    private static final String UPDATE_SQL = """
            UPDATE cities
            SET city_name = ?
            WHERE city_id = ?;
            """;
    private static final String DELETE_SQL = """
            DELETE FROM cities
            WHERE city_id = ?;
            """;
    private static final String EXIST_BY_ID_SQL = """
                SELECT exists (
                SELECT 1
                    FROM cities
                        WHERE city_id = ?
                        LIMIT 1);
            """;
    private  ConnectionManager connectionManager = ConnectionManagerImpl.getInstance();
    private static CityRepository instance;

    public CityRepositoryImpl() {
    }

public CityRepositoryImpl(String url,String username, String password){
    connectionManager = new ConnectionManagerImpl(url, username, password);
}
    public static synchronized CityRepository getInstance() {
        if (instance == null) {
            instance = new CityRepositoryImpl();
        }
        return instance;
    }

    @Override
    public City save(City city) {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS)){
            preparedStatement.setString(1, city.getName());
            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()){
                city = new City(resultSet.getLong("city_id"), city.getName());
            }
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
        return city;
    }

    @Override
    public City findById(Long id) {
        City city =null;
        try(Connection connection = connectionManager.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ID_SQL)) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                city = new City(resultSet.getLong("city_id"), resultSet.getString("city_name"));
            }
        }catch (SQLException e){
            throw new RepositoryException(e);
        }
        return city;
    }

    @Override
    public List<City> findAll() {
        List<City> cityList = new ArrayList<>();
        try(Connection connection = connectionManager.getConnection();
            Statement statement = connection.createStatement()){
            ResultSet resultSet = statement.executeQuery(FIND_ALL_SQL);
            while (resultSet.next()){
                cityList.add(new City(resultSet.getLong("city_id"), resultSet.getString("city_name")));
            }

        }catch (SQLException e){
            throw new RepositoryException(e);
        }
        return cityList;
    }

    @Override
    public void update(City city) {
        try(Connection connection = connectionManager.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_SQL)){
            preparedStatement.setString(1, city.getName());
            preparedStatement.setLong(2, city.getId());
            preparedStatement.executeUpdate();
        }catch (SQLException e){
            throw new RepositoryException(e);
        }
    }

    @Override
    public boolean deleteById(Long id) {
        boolean deleteResult;
        try(Connection connection = connectionManager.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(DELETE_SQL)){
            preparedStatement.setLong(1, id);
            deleteResult = preparedStatement.executeUpdate() > 0;
        }catch (SQLException e){
            throw new RepositoryException(e);
        }
        return deleteResult;
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
