package org.rest.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.rest.model.Bank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.stream.StreamSupport;


@ExtendWith(SpringExtension.class)
class BankCrudRepositoryTest extends TestDatabaseHelper{

    @Autowired
    public BankCrudRepository bankRepository;

    @Test
    void save() {
        String expectedName = "new bank name";
        Bank bank = new Bank(null, expectedName, null);
        bank = bankRepository.save(bank);
        Bank resultBank = bankRepository.findById(bank.getId()).orElseThrow();

        Assertions.assertNotNull(resultBank);
        Assertions.assertEquals(expectedName, resultBank.getName());

    }

    @DisplayName("Find by ID")
    @ParameterizedTest
    @CsvSource(value = {
            "1, true",
            "3, true",
            "100, false"
    })
    void findById(Long expectedId , Boolean expectedValue) {
        Bank bank = bankRepository.findById(expectedId).orElse(null);
        Assertions.assertEquals(expectedValue, bank != null);
        if (bank != null)
            Assertions.assertEquals(expectedId, bank.getId());
    }

    @Test
    void findAll() {
        int expectedSize = 4;
        int resultSize = StreamSupport.stream(bankRepository.findAll().spliterator(), false)
                .toList().size();
        Assertions.assertEquals(expectedSize, resultSize);
    }

    @DisplayName("Exist by ID")
    @ParameterizedTest
    @CsvSource(value = {
            "1; true",
            "3; true",
            "100; false"
    }, delimiter = ';')
    void exitsById(Long bankId, Boolean expectedValue) {
        boolean isCityExist = bankRepository.existsById(bankId);
        Assertions.assertEquals(expectedValue, isCityExist);
    }
    @Test
    void deleteById() {
        Boolean expectedValue = false;
        int expectedSize = StreamSupport.stream(bankRepository.findAll().spliterator(), false)
                .toList().size();
                //bankRepository.findAll().size();
        Bank bank = new Bank(null, "New bank", List.of());
        bank = bankRepository.save(bank);
        int resultSizeBefore = StreamSupport.stream(bankRepository.findAll().spliterator(), false)
                .toList().size();
        Assertions.assertNotEquals(expectedSize, resultSizeBefore);
        bankRepository.deleteById(bank.getId());
        boolean resultDelete = bankRepository.existsById(bank.getId());
        int resultSizeAfter = StreamSupport.stream(bankRepository.findAll().spliterator(), false)
                .toList().size();

        Assertions.assertEquals(expectedValue, resultDelete);
        Assertions.assertEquals(expectedSize, resultSizeAfter);
    }

}
