package org.rest.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.rest.exception.NotFoundException;
import org.rest.model.Bank;
import org.rest.repository.BankRepository;
import org.rest.repository.UserRepository;
import org.rest.repository.UserToBankRepository;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)

class BankServiceImplTest {

    @Mock
    private BankRepository bankRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserToBankRepository userToBankRepository;

    @InjectMocks
    private BankServiceImpl bankService;

    private Bank bank;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        bank = new Bank(1L, "TestBank", List.of());
    }

    @Test
    void testSave() {
        when(bankRepository.save(bank)).thenReturn(bank);
        assertEquals(bank, bankService.save(bank));
    }

    @Test
    void testFindById_BankExists() throws NotFoundException {
        when(bankRepository.findById(1L)).thenReturn(bank);
        assertEquals(bank, bankService.findById(1L));
    }

    @Test
    void testFindById_BankNotFound() {
        when(bankRepository.findById(1L)).thenReturn(null);
        assertThrows(NotFoundException.class, () -> bankService.findById(1L));
    }

    @Test
    void testFindAll() {
        List<Bank> bankList = new ArrayList<>();
        when(bankRepository.findAll()).thenReturn(bankList);
        assertEquals(bankList, bankService.findAll());
    }

    @Test
    void testUpdate_BankExists() throws NotFoundException {

        when(bankRepository.existById(1L)).thenReturn(true);
        bankService.update(bank);
        verify(bankRepository, times(1)).update(bank);
    }

    @Test
    void testUpdate_BankNotFound() {
        when(bankRepository.existById(1L)).thenReturn(false);
        assertThrows(NotFoundException.class, () -> bankService.update(bank));
    }

    @Test
    void testDelete_BankExists() throws NotFoundException {
        when(bankRepository.deleteById(1L)).thenReturn(true);
        when(bankRepository.existById(1L)).thenReturn(true);
        assertTrue(bankService.delete(1L));
    }

    @Test
    void testDelete_BankNotFound() {
        when(bankRepository.existById(1L)).thenReturn(false);
        assertThrows(NotFoundException.class, () -> bankService.delete(1L));
    }

    @Test
    void testAddUserToBank_UserExists_BankExists() throws NotFoundException {
        when(userRepository.existById(1L)).thenReturn(true);
        when(bankRepository.existById(1L)).thenReturn(true);

        assertDoesNotThrow(() -> bankService.addUserToBank(1L, 1L));
        verify(userToBankRepository, times(1)).save(any());
    }

    @Test
    void testAddUserToBank_UserNotFound() {
        when(userRepository.existById(1L)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> bankService.addUserToBank(1L, 1L));
        verify(userToBankRepository, never()).save(any());
    }

    @Test
    void testAddUserToBank_BankNotFound() {
        when(userRepository.existById(1L)).thenReturn(true);
        when(bankRepository.existById(1L)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> bankService.addUserToBank(1L, 1L));
        verify(userToBankRepository, never()).save(any());
    }
}