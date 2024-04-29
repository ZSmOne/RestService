package org.rest.servlet.mapper;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;
import org.rest.model.Bank;
import org.rest.servlet.bank.dto.BankIncomingDto;
import org.rest.servlet.bank.dto.BankOutGoingDto;
import org.rest.servlet.bank.dto.BankSimpleOutGoingDto;
import org.rest.servlet.bank.dto.BankUpdateDto;

@Mapper
public interface BankMapper {
    BankMapper INSTANCE = Mappers.getMapper(BankMapper.class);

    @Mappings({
            @Mapping(target= "id", source = "bank.id"),
            @Mapping(target= "name", source = "bank.name")
    })
    BankSimpleOutGoingDto bankToBankSimpleOutGoingDto (Bank bank);

    @Mappings({
            @Mapping(target= "name", source = "bankDto.name"),
    })
    Bank bankIncomingDtoToBank(BankIncomingDto bankDto);

    @Mappings({
            @Mapping(target= "id", source = "bankDto.id"),
            @Mapping(target= "name", source = "bankDto.name")
    })
    Bank bankUpdateDtoToBank(BankUpdateDto bankDto);

    @Mappings({
            @Mapping(target= "name", source = "bank.name"),
    })
    @IterableMapping(qualifiedByName = "userToUserSimpleOutGoingDto")
    BankOutGoingDto bankToBankOutGoingDto (Bank bank);
}
