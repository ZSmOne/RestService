package org.rest.servlet.bank;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.rest.exception.NotFoundException;
import org.rest.model.Bank;
import org.rest.model.User;
import org.rest.service.impl.BankServiceImpl;
import org.rest.servlet.bank.dto.BankIncomingDto;
import org.rest.servlet.bank.dto.BankOutGoingDto;
import org.rest.servlet.bank.dto.BankUpdateDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class BankControllerTest {
    @Mock
    private BankServiceImpl bankService;

    @InjectMocks
    private BankController bankController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testGetBankById() throws NotFoundException {
        Long bankId = 1L;
        Bank bank = new Bank(bankId, "Test Bank", null);
        when(bankService.findById(bankId)).thenReturn(bank);
        ResponseEntity<String> response = bankController.getBankById(bankId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testGetBankById_NotFound() {
        Long bankId = 1L;
        when(bankService.findById(bankId)).thenThrow(new NotFoundException("Bank not found"));
        ResponseEntity<String> response = bankController.getBankById(bankId);

        verify(bankService, times(1)).findById(bankId);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testGetAllBanks() {
        List<Bank> banks = new ArrayList<>();
        banks.add(new Bank(1L, "Bank 1", null));
        banks.add(new Bank(2L, "Bank 2", null));
        when(bankService.findAll()).thenReturn(banks);
        ResponseEntity<String> response = bankController.getAllUsers();
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testGetAllBanks_NotFound() throws NotFoundException {
        when(bankService.findAll()).thenThrow(new NotFoundException("Bank not found"));
        ResponseEntity<String> response = bankController.getAllUsers();
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testCreateBank() {
        BankIncomingDto bankIncomingDto = new BankIncomingDto("Test Bank");
        Bank bank = new Bank(1L, "Test Bank", null);

        when(bankService.save(any())).thenReturn(bank);
        ResponseEntity<String> response = bankController.createBank(bankIncomingDto);
        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    void testUpdateBank_NotFound() throws NotFoundException {
        BankUpdateDto bankUpdateDto = new BankUpdateDto(1L, "UpdatedBank");
        doThrow(new NotFoundException("Bank not found")).when(bankService).update(any());
        ResponseEntity<String> response = bankController.updateBank(bankUpdateDto);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void tesDeleteUserToBank() {
        Long bankId = 1L;
        Long userId = 1L;
        doNothing().when(bankService).deleteUserToBank(bankId, userId);
        ResponseEntity<String> response = bankController.deleteUserToBank(userId, bankId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
    @Test
    void testAddUserToBank() {
        Long userId = 1L;
        Long bankId = 1L;

        Bank bankAfter = new Bank();
        User user = new User();
        user.setId(userId);
        bankAfter.setUserList(new ArrayList<>());
        bankAfter.getUserList().add(user);

        when(bankService.addUserToBank(bankId, userId)).thenReturn(bankAfter);

        BankOutGoingDto result = bankController.addUserToBank(userId, bankId);
        verify(bankService, times(1)).addUserToBank(bankId, userId);
        assertEquals(bankAfter.getId(), result.getId());
        assertEquals(bankAfter.getName(), result.getName());
        assertEquals(bankAfter.getUserList().size(), result.getUserList().size());
    }


    @Test
    void testDeleteUserToBank_NotFound() {
        Long bankId = 1L;
        Long userId = 1L;
        doThrow(new NotFoundException("User not found")).when(bankService).deleteUserToBank(userId, bankId);
        ResponseEntity<String> response = bankController.deleteUserToBank(bankId, userId);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testDeleteBank_NotFound() {
        Long bankId = 1L;
        doThrow(new NotFoundException("Bank not found")).when(bankService).delete(bankId);
        ResponseEntity<String> response = bankController.deleteBank(bankId);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
    @Test
    void testHandle() {
        Throwable exception = new Throwable("Test exception");
        ResponseEntity<String> response = bankController.handle(exception);

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assertions.assertEquals("Test exception", response.getBody());
    }
}
