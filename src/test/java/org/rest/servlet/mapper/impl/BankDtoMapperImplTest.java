package org.rest.servlet.mapper.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.rest.model.Bank;
import org.rest.repository.UserToBankRepository;
import org.rest.servlet.dto.BankIncomingDto;
import org.rest.servlet.dto.BankOutGoingDto;
import org.rest.servlet.dto.BankUpdateDto;
import org.rest.servlet.mapper.BankDtoMapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
class BankDtoMapperImplTest {
    private BankDtoMapper bankDtoMapper;
    private UserToBankRepository userToBankRepository;

    @BeforeEach
    public void setUp() {
        bankDtoMapper = BankDtoMapperImpl.getInstance();
        userToBankRepository = mock(UserToBankRepository.class);
        BankDtoMapperImpl.userToBankRepository = userToBankRepository;
    }

    @Test
    void map_BankIncomingDto_ReturnsBank() {
        BankIncomingDto dto = new BankIncomingDto("TestBank");
        Bank result = bankDtoMapper.map(dto);

        assertNotNull(result);
        assertEquals("TestBank", result.getName());
        assertNull(result.getId());
    }

    @Test
    void map_Bank_ReturnsBankOutGoingDto() {
        Bank bank = new Bank(1L, "TestBank", null);
        when(userToBankRepository.findUsersByBankId(1L)).thenReturn(new ArrayList<>());
        BankOutGoingDto result = bankDtoMapper.map(bank);
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("TestBank", result.getName());
        assertTrue(result.getUserList().isEmpty());
    }

    @Test
    void map_BankUpdateDto_ReturnsBank() {
        // Arrange
        BankUpdateDto updateDto = new BankUpdateDto(1L, "UpdatedBankName");
        Bank result = bankDtoMapper.map(updateDto);
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("UpdatedBankName", result.getName());
    }

    @Test
    void map_ListOfBanks_ReturnsListOfBankOutGoingDto() {
        List<Bank> bankList = Arrays.asList(
                new Bank(1L, "Bank1", null),
                new Bank(2L, "Bank2", null),
                new Bank(3L, "Bank3", null)
        );
        when(userToBankRepository.findUsersByBankId(anyLong())).thenReturn(new ArrayList<>());
        List<BankOutGoingDto> result = bankDtoMapper.map(bankList);

        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals("Bank1", result.get(0).getName());
        assertEquals("Bank2", result.get(1).getName());
        assertEquals("Bank3", result.get(2).getName());
    }

    @Test
    void mapUpdateList_ListOfBankUpdateDto_ReturnsListOfBanks() {
        List<BankUpdateDto> bankUpdateDtoList = Arrays.asList(
                new BankUpdateDto(1L, "UpdatedBank1"),
                new BankUpdateDto(2L, "UpdatedBank2"),
                new BankUpdateDto(3L, "UpdatedBank3")
        );

        List<Bank> result = bankDtoMapper.mapUpdateList(bankUpdateDtoList);

        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals("UpdatedBank1", result.get(0).getName());
        assertEquals(2L, result.get(1).getId());
        assertEquals("UpdatedBank2", result.get(1).getName());
        assertEquals(3L, result.get(2).getId());
        assertEquals("UpdatedBank3", result.get(2).getName());
    }
}
