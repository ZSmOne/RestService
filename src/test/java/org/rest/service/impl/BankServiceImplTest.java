package org.rest.service.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.rest.exception.NotFoundException;
import org.rest.model.Bank;
import org.rest.model.User;
import org.rest.repository.BankCrudRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)

class BankServiceImplTest {

    @Mock
    private BankCrudRepository bankRepository;

    @Mock
    private UserServiceImpl userService;

    @InjectMocks
    private BankServiceImpl bankService;

    private Bank bank;
    @BeforeEach
    void setUp() {
        bank = new Bank(1L, "TestBank", List.of());
    }

    @Test
    void testSave() {
        when(bankRepository.save(bank)).thenReturn(bank);
        Assertions.assertEquals(bank, bankService.save(bank));
    }

    @Test
    void testFindById_BankExists() throws NotFoundException {
        when(bankRepository.findById(1L)).thenReturn(Optional.ofNullable(bank));
        Assertions.assertEquals(bank, bankService.findById(1L));
    }


    @Test
    void testFindAll() {
        List<Bank> bankList = new ArrayList<>();
        when(bankRepository.findAll()).thenReturn(bankList);
        Assertions.assertEquals(bankList, bankService.findAll());
    }

    @Test
    void testUpdate() throws NotFoundException {

        when(bankRepository.existsById(1L)).thenReturn(true);
        bankService.update(bank);
        verify(bankRepository, times(1)).save(bank);
    }



    @Test
    void testDelete_BankExists() throws NotFoundException {
        when(bankRepository.existsById(1L)).thenReturn(true);
        bankService.delete(1L);
        verify(bankRepository, times(1)).deleteById(1L);
    }


    @Test
    void testAddUserToBank_UserExists_BankExists() throws NotFoundException {

        Bank bank = new Bank();
        User user = new User();
        user.setId(1L);
        bank.setId(1L);
        bank.setUserList(new ArrayList<>());

        when(bankRepository.findById(1L)).thenReturn(Optional.of(bank));
        when(userService.getUser(1L)).thenReturn(user);

        Bank updatedBank = bankService.addUserToBank(1L, 1L);

        verify(bankRepository, times(1)).findById(1L);
        verify(userService, times(1)).getUser(1L);
        Assertions.assertTrue(updatedBank.getUserList().contains(user));
    }
}