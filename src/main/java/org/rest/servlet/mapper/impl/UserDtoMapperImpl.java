package org.rest.servlet.mapper.impl;

import org.rest.model.User;
import org.rest.repository.UserToBankRepository;
import org.rest.repository.impl.UserToBankRepositoryImpl;
import org.rest.servlet.dto.BankSimpleOutGoingDto;
import org.rest.servlet.dto.UserIncomingDto;
import org.rest.servlet.dto.UserOutGoingDto;
import org.rest.servlet.dto.UserUpdateDto;
import org.rest.servlet.mapper.BankDtoMapper;
import org.rest.servlet.mapper.CityDtoMapper;
import org.rest.servlet.mapper.UserDtoMapper;

import java.util.List;

public class UserDtoMapperImpl implements UserDtoMapper {
    private static final CityDtoMapper CITY_DTO_MAPPER = CityDtoMapperImpl.getInstance();
    static final BankDtoMapper bankDtoMapper = BankDtoMapperImpl.getInstance();
    static UserToBankRepository userToBankRepository = UserToBankRepositoryImpl.getInstance();


    private static UserDtoMapper instance;


    public static synchronized UserDtoMapper getInstance() {
        if (instance == null) {
            instance = new UserDtoMapperImpl();
        }
        return instance;
    }

    @Override
    public User map(UserIncomingDto userDto) {
        return new User(
                null,
                userDto.getName(),
                userDto.getCity(),
                null
        );
    }

    @Override
    public User map(UserUpdateDto userDto) {
        return new User(
                userDto.getId(),
                userDto.getName(),
                CITY_DTO_MAPPER.map(userDto.getCity()),
                null
        );
    }

    @Override
    public UserOutGoingDto map(User user) {
        List<BankSimpleOutGoingDto> bankList = userToBankRepository.findBanksByUserId(user.getId())
                .stream().map(bank -> new BankSimpleOutGoingDto(
                        bank.getId(),
                        bank.getName()
                )).toList();
        return new UserOutGoingDto(
                user.getId(), user.getName(), CITY_DTO_MAPPER.map(user.getCity()), bankList);

    }

    @Override
    public List<UserOutGoingDto> map(List<User> user) {
        return user.stream().map(this::map).toList();
    }
}
