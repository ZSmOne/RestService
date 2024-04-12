package org.rest.servlet.mapper.impl;

import org.rest.model.Bank;
import org.rest.repository.UserToBankRepository;
import org.rest.repository.impl.UserToBankRepositoryImpl;
import org.rest.servlet.dto.BankIncomingDto;
import org.rest.servlet.dto.BankOutGoingDto;
import org.rest.servlet.dto.BankUpdateDto;
import org.rest.servlet.dto.UserSimpleOutGoingDto;
import org.rest.servlet.mapper.BankDtoMapper;

import java.util.List;

public class BankDtoMapperImpl implements BankDtoMapper {
    private static BankDtoMapper instance;
    static UserToBankRepository userToBankRepository = UserToBankRepositoryImpl.getInstance();

    private BankDtoMapperImpl() {
    }

    public static BankDtoMapper getInstance() {
        if (instance == null) {
            instance = new BankDtoMapperImpl();
        }
        return instance;
    }

@Override
public Bank map(BankIncomingDto dto) {
    return new Bank(null, dto.getName(), null);
}

    @Override
    public BankOutGoingDto map(Bank bank) {
        List<UserSimpleOutGoingDto> userList = userToBankRepository.findUsersByBankId(bank.getId())
                .stream().map(user -> new UserSimpleOutGoingDto(
                        user.getId(),
                        user.getName()

                )).toList();

        return new BankOutGoingDto(bank.getId(), bank.getName(), userList);
    }

    @Override
    public Bank map(BankUpdateDto updateDto) {
        return new Bank(
                updateDto.getId(),
                updateDto.getName(),
                null
        );
    }

    @Override
    public List<BankOutGoingDto> map(List<Bank> bankList) {
        return bankList.stream().map(this::map).toList();
    }

    @Override
    public List<Bank> mapUpdateList(List<BankUpdateDto> bankList) {
        return bankList.stream().map(this::map).toList();
    }
}
