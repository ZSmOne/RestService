package org.rest.servlet.mapper;

import org.rest.model.Bank;
import org.rest.servlet.dto.BankIncomingDto;
import org.rest.servlet.dto.BankOutGoingDto;
import org.rest.servlet.dto.BankUpdateDto;

import java.util.List;

public interface BankDtoMapper {
    Bank map(BankIncomingDto bankIncomingDto);

    BankOutGoingDto map(Bank bank);

    Bank map(BankUpdateDto bankUpdateDto);

    List<BankOutGoingDto> map(List<Bank> bankList);

    List<Bank> mapUpdateList(List<BankUpdateDto> departmentList);
}
