package org.rest.servlet.bank;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.rest.exception.NotFoundException;
import org.rest.model.Bank;
import org.rest.service.BankService;
import org.rest.servlet.bank.dto.BankIncomingDto;
import org.rest.servlet.bank.dto.BankOutGoingDto;
import org.rest.servlet.bank.dto.BankUpdateDto;
import org.rest.servlet.mapper.BankMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bank")
public class BankController {

    private final BankService bankService;
    private final ObjectMapper objectMapper;

    @Autowired
    public BankController(BankService bankService) {
        this.bankService = bankService;
        this.objectMapper = new ObjectMapper();
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getBankById(@PathVariable("id") Long bankId) {
        try {
            Bank bank = bankService.findById(bankId);
            BankOutGoingDto bankDto = BankMapper.INSTANCE.bankToBankOutGoingDto(bank);
            String bankJson = objectMapper.writeValueAsString(bankDto);
            return ResponseEntity.ok(bankJson);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad request.");
        }
    }

    @GetMapping("/all")
    public ResponseEntity<String> getAllUsers() {
        try {
            List<Bank> banks = bankService.findAll();
            List<BankOutGoingDto> bankOutGoingDtos = banks.stream()
                    .map(BankMapper.INSTANCE::bankToBankOutGoingDto)
                    .toList();
            String bankJson = objectMapper.writeValueAsString(bankOutGoingDtos);
            return ResponseEntity.ok(bankJson);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad request.");
        }
    }

    @PostMapping
    public ResponseEntity<String> createBank(@RequestBody String json) {
        try {
            BankIncomingDto bankIncomingDto = objectMapper.readValue(json, BankIncomingDto.class);
            Bank bank = BankMapper.INSTANCE.bankIncomingDtoToBank(bankIncomingDto);
            BankOutGoingDto bankOutGoingDto = BankMapper.INSTANCE.bankToBankOutGoingDto(bankService.save(bank));
            String bankJson = objectMapper.writeValueAsString(bankOutGoingDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(bankJson);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Incorrect bank Object.");
        }
    }

    @PutMapping
    public ResponseEntity<String> updateBank(@RequestBody String json) {
        try {
            BankUpdateDto bankUpdateDto = objectMapper.readValue(json, BankUpdateDto.class);
             bankService.update(BankMapper.INSTANCE.bankUpdateDtoToBank(bankUpdateDto));
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Incorrect bank Object.");
        }
    }

    @PutMapping("/{bankId}/addUser/{userId}")
    public ResponseEntity<String> addUserToBank(@PathVariable("bankId") Long bankId, @PathVariable("userId") Long userId) {
        try {
            if (userId != null) {
                bankService.addUserToBank(userId, bankId);
            }
            return ResponseEntity.status(HttpStatus.OK).build();
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad request.");
        }
    }

        @PutMapping("/{bankId}/deleteUser/{userId}")
    public ResponseEntity<String> deleteUserToBank(@PathVariable("bankId") Long bankId, @PathVariable("userId") Long userId) {
        try {
           if (userId != null) {
                bankService.deleteUserToBank(userId, bankId);
            }
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad request.");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBank(@PathVariable("id") Long bankId) {
        try {
            bankService.delete(bankId);
            return ResponseEntity.noContent().build();
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad request.");
        }
    }
}