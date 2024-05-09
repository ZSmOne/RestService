package org.rest.service.impl;

import org.rest.model.Bank;
import org.rest.model.User;
import org.rest.repository.BankCrudRepository;
import org.rest.service.BankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.StreamSupport;

@Service
public class BankServiceImpl implements BankService {
    private final BankCrudRepository bankCrudRepository;
    private final UserServiceImpl userServiceImpl;

    @Autowired
    public BankServiceImpl(BankCrudRepository bankCrudRepository, UserServiceImpl userServiceImpl) {
        this.bankCrudRepository = bankCrudRepository;
        this.userServiceImpl = userServiceImpl;
    }

    @Override
    public Bank findById(Long id){
        return bankCrudRepository.findById(id).orElseThrow(() -> new IllegalStateException("Bank not found"));
    }

    @Override
    public List<Bank> findAll() {
        Iterable<Bank> iterable = bankCrudRepository.findAll();
        return StreamSupport.stream(iterable.spliterator(), false)
                .toList();    }

    @Override
    public Bank save(Bank bank) {
        return bankCrudRepository.save(bank);
    }

    @Override
    public void update(Bank bank) {
        if (bankCrudRepository.existsById(bank.getId())) {
            bankCrudRepository.save(bank);
        }
    }

    @Override
    public void delete(Long id) {
        if (bankCrudRepository.existsById(id))
            bankCrudRepository.deleteById(id);
    }

    public Bank addUserToBank(Long bankId, Long userId) {
        Bank bank = bankCrudRepository.findById(bankId).orElseThrow(() -> new IllegalStateException("Bank not found"));
        User user = userServiceImpl.getUser(userId);
        bank.getUserList().add(user);
        bankCrudRepository.save(bank);
        return bank;
    }

    public void deleteUserToBank(Long bankId, Long userId) {
        Bank bank = bankCrudRepository.findById(bankId).orElseThrow(() -> new IllegalStateException("Bank not found"));
        if (userServiceImpl.getUser(userId) != null) {
            bank.getUserList().removeIf(u -> Objects.equals(u.getId(), userId));
            bankCrudRepository.save(bank);
        } else {
            throw new IllegalStateException("User not found");
        }
    }
}
