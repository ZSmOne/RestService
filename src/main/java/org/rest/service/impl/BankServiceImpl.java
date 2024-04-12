package org.rest.service.impl;

import org.rest.exception.NotFoundException;
import org.rest.model.Bank;
import org.rest.model.UserToBank;
import org.rest.repository.BankRepository;
import org.rest.repository.UserRepository;
import org.rest.repository.UserToBankRepository;
import org.rest.repository.impl.BankRepositoryImpl;
import org.rest.repository.impl.UserRepositoryImpl;
import org.rest.repository.impl.UserToBankRepositoryImpl;
import org.rest.service.BankService;

import java.util.List;


public class BankServiceImpl implements BankService {

    private BankRepository bankRepository = BankRepositoryImpl.getInstance();
    private UserRepository userRepository = UserRepositoryImpl.getInstance();

    private UserToBankRepository userToBankRepository = UserToBankRepositoryImpl.getInstance();

    private static BankService instance;


    private BankServiceImpl() {
    }

    public static synchronized BankService getInstance() {
        if (instance == null) {
            instance = new BankServiceImpl();
        }
        return instance;
    }

    @Override
    public Bank save(Bank bank) {
        bank = bankRepository.save(bank);
        return bank;
    }

    @Override
    public Bank findById(Long bankId) throws NotFoundException {
        Bank bank = bankRepository.findById(bankId);
        if (bank == null)
            throw new NotFoundException("Bank not found.");
        return bank;
    }

    @Override
    public List<Bank> findAll() {
        return bankRepository.findAll();
    }

    @Override
    public void update(Bank bank) throws NotFoundException {
        isBankExists(bank.getId());
        bankRepository.update(bank);
    }

    @Override
    public boolean delete(Long bankId) throws NotFoundException {
        isBankExists(bankId);
        return bankRepository.deleteById(bankId);
    }
    @Override
    public void addUserToBank(Long userId, Long bankId) throws NotFoundException {
        isExistUser(userId);
        if (bankRepository.existById(bankId)) {
            UserToBank userToBank = new UserToBank(null, userId, bankId);
            userToBankRepository.save(userToBank);
        } else {
            throw new NotFoundException("Bank not found.");
        }
    }

    @Override
    public void deleteUserToBank(Long userId, Long bankId) throws NotFoundException {
        isExistUser(userId);
        if (bankRepository.existById(bankId)) {
            UserToBank userToBank = userToBankRepository.findByUserIdAndBankId(userId, bankId);
            if (userToBank == null)
                throw new NotFoundException("User with this bank not found.");

            userToBankRepository.deleteById(userToBank.getId());
        } else {
            throw new NotFoundException("User not found.");
        }
    }

    private void isExistUser(Long userId) throws NotFoundException {
        if (!userRepository.existById(userId)) {
            throw new NotFoundException("User not found.");
        }
    }

    private void isBankExists(Long bankId) throws NotFoundException {
        if (!bankRepository.existById(bankId)) {
            throw new NotFoundException("Bank not found.");
        }
    }
}
