package org.rest.service;

import org.rest.exception.NotFoundException;
import org.rest.model.Bank;

public interface BankService extends Service<Bank, Long>{

    void addUserToBank(Long userId, Long bankId) throws NotFoundException;

    void deleteUserToBank(Long userId, Long bankId) throws NotFoundException;
}
