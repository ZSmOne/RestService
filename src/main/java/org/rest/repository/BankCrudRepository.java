package org.rest.repository;

import org.rest.model.Bank;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface  BankCrudRepository extends CrudRepository<Bank, Long> {
}
