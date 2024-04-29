package org.vlad.rest.servlet.bank;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.rest.exception.NotFoundException;
import org.rest.model.Bank;
import org.rest.service.BankService;
import org.rest.servlet.bank.BankController;
import org.rest.servlet.bank.dto.BankIncomingDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

class BankControllerTest {
    @Mock
    private BankService bankService;

    @InjectMocks
    private BankController bankController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testGetBankById() throws IOException, NotFoundException {
        Long bankId = 1L;
        Bank bank = new Bank(bankId, "Test Bank", null);
        when(bankService.findById(bankId)).thenReturn(bank);
        ResponseEntity<String> response = bankController.getBankById(bankId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testGetAllBanks() throws IOException {
        List<Bank> banks = new ArrayList<>();
        banks.add(new Bank(1L, "Bank 1", null));
        banks.add(new Bank(2L, "Bank 2", null));
        when(bankService.findAll()).thenReturn(banks);
        ResponseEntity<String> response = bankController.getAllUsers();
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testCreateBank() throws IOException {
        String json = "{\"name\":\"Test Bank\"}";
        BankIncomingDto bankIncomingDto = new BankIncomingDto("Test Bank");
        Bank bank = new Bank(1L, "Test Bank", null);

        when(bankService.save(any())).thenReturn(bank);
        ResponseEntity<String> response = bankController.createBank(json);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    void testUpdateBank_NotFound() throws IOException, NotFoundException {
        String json = "{\"id\":1,\"name\":\"Updated Bank\"}";
        doThrow(new NotFoundException("Bank not found")).when(bankService).update(any());
        ResponseEntity<String> response = bankController.updateBank(json);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testAddUserToBank() {
        Long bankId = 1L;
        Long userId = 1L;
        ResponseEntity<String> response = bankController.addUserToBank(bankId, userId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
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
}
