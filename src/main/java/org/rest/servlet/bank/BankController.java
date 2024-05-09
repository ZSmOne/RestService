package org.rest.servlet.bank;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.rest.exception.NotFoundException;
import org.rest.model.Bank;
import org.rest.service.impl.BankServiceImpl;
import org.rest.servlet.bank.dto.BankIncomingDto;
import org.rest.servlet.bank.dto.BankOutGoingDto;
import org.rest.servlet.bank.dto.BankUpdateDto;
import org.rest.servlet.mapper.BankMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bank")
public class BankController {

    private final BankServiceImpl bankServiceImpl;
    private final ObjectMapper objectMapper;

    public BankController(BankServiceImpl bankServiceImpl) {
        this.bankServiceImpl = bankServiceImpl;
        this.objectMapper = new ObjectMapper();
    }


    @GetMapping("/{id}")
    public ResponseEntity<String> getBankById(@PathVariable("id") Long bankId) {
        try {
            Bank bank = bankServiceImpl.findById(bankId);
            BankOutGoingDto bankDto = BankMapper.INSTANCE.bankToBankOutGoingDto(bank);
            String bankJson = objectMapper.writeValueAsString(bankDto);
            return ResponseEntity.ok(bankJson);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (JsonProcessingException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/all")
    public ResponseEntity<String> getAllUsers() {
        try {
            List<Bank> banks = bankServiceImpl.findAll();
            List<BankOutGoingDto> bankOutGoingDtoList = banks.stream()
                    .map(BankMapper.INSTANCE::bankToBankOutGoingDto)
                    .toList();
            String bankJson = objectMapper.writeValueAsString(bankOutGoingDtoList);
            return ResponseEntity.ok(bankJson);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (JsonProcessingException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad request.");
        }
    }

    @PostMapping
    public ResponseEntity<String> createBank(@RequestBody BankIncomingDto bankIncomingDto) {
        try {
            Bank bank = BankMapper.INSTANCE.bankIncomingDtoToBank(bankIncomingDto);
            BankOutGoingDto bankOutGoingDto = BankMapper.INSTANCE.bankToBankOutGoingDto(bankServiceImpl.save(bank));
            String bankJson = objectMapper.writeValueAsString(bankOutGoingDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(bankJson);
        } catch (JsonProcessingException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping
    public ResponseEntity<String> updateBank(@RequestBody BankUpdateDto bankUpdateDto) {
        try{
            bankServiceImpl.update(BankMapper.INSTANCE.bankUpdateDtoToBank(bankUpdateDto));
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBank(@PathVariable("id") Long bankId) {
        try{
            bankServiceImpl.delete(bankId);
            return ResponseEntity.noContent().build();
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{bankId}/addUser")
    public BankOutGoingDto addUserToBank(@RequestParam("userId") Long userId, @PathVariable("bankId") Long bankId) {
        Bank bankAfter = bankServiceImpl.addUserToBank(bankId, userId);
        return BankMapper.INSTANCE.bankToBankOutGoingDto(bankAfter);
    }

    @DeleteMapping("/{bankId}/deleteUser")
    public ResponseEntity<String> deleteUserToBank(@RequestParam("userId") Long userId, @PathVariable("bankId") Long bankId) {
        try{
            bankServiceImpl.deleteUserToBank(bankId, userId);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<String> handle(Throwable e) {
        return ResponseEntity.badRequest().body("Bad request.");
    }
}