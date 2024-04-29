package org.vlad.rest.servlet.mapper;

import org.junit.jupiter.api.Test;
import org.rest.model.Bank;
import org.rest.servlet.bank.dto.BankIncomingDto;
import org.rest.servlet.bank.dto.BankOutGoingDto;
import org.rest.servlet.bank.dto.BankSimpleOutGoingDto;
import org.rest.servlet.bank.dto.BankUpdateDto;
import org.rest.servlet.mapper.BankMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BankMapperTest {

    @Test
    void testBankToBankSimpleOutGoingDto() {
        Bank bank = new Bank(1L, "Test Bank", null);
        BankSimpleOutGoingDto dto = BankMapper.INSTANCE.bankToBankSimpleOutGoingDto(bank);
        assertEquals(bank.getId(), dto.getId());
        assertEquals(bank.getName(), dto.getName());
    }

    @Test
    void testBankIncomingDtoToBank() {
        BankIncomingDto dto = new BankIncomingDto("Test Bank");
        Bank bank = BankMapper.INSTANCE.bankIncomingDtoToBank(dto);
        assertEquals(dto.getName(), bank.getName());
    }

    @Test
    void testBankUpdateDtoToBank() {
        BankUpdateDto dto = new BankUpdateDto(1L, "Updated Bank");
        Bank bank = BankMapper.INSTANCE.bankUpdateDtoToBank(dto);
        assertEquals(dto.getId(), bank.getId());
        assertEquals(dto.getName(), bank.getName());
    }

    @Test
    void testBankToBankOutGoingDto() {
        Bank bank = new Bank(1L, "Test Bank", null);
        BankOutGoingDto dto = BankMapper.INSTANCE.bankToBankOutGoingDto(bank);
        assertEquals(bank.getName(), dto.getName());
    }
}
