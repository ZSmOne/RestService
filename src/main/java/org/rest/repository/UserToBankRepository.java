package org.rest.repository;

import org.rest.model.Bank;
import org.rest.model.User;
import org.rest.model.UserToBank;

import java.util.List;

public interface UserToBankRepository extends Repository<UserToBank, Long >{
    boolean deleteByUserId(Long userId);

    boolean deleteByBankId(Long bankId);

    List<UserToBank> findAllByUserId(Long userId);

    List<Bank> findBanksByUserId(Long userId);

    List<UserToBank> findAllByBankId(Long bankId);

    List<User> findUsersByBankId(Long bankId);

    UserToBank findByUserIdAndBankId(Long userId, Long bankId);
}
