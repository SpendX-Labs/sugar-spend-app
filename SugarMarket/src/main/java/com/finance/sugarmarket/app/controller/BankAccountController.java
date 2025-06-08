package com.finance.sugarmarket.app.controller;

import com.finance.sugarmarket.app.dto.BankAccountDto;
import com.finance.sugarmarket.app.service.BankAccountService;
import com.finance.sugarmarket.base.controller.BaseController;
import com.finance.sugarmarket.base.dto.Filter;
import com.finance.sugarmarket.base.dto.ListViewDto;
import com.finance.sugarmarket.base.dto.Operands;
import com.finance.sugarmarket.base.enums.Operators;
import com.finance.sugarmarket.constants.AppConstants;
import com.finance.sugarmarket.constants.FieldConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/app/bank-account")
public class BankAccountController extends BaseController {

    @Autowired
    private BankAccountService bankAccountService;

    private static final Logger log = LoggerFactory.getLogger(BankAccountController.class);

    @GetMapping("all")
    public ResponseEntity<List<BankAccountDto>> findAllBankAccountsByUserId() {
        try {
            return ResponseEntity.ok(bankAccountService.findAllBankAccountsByUserId(getUserId()));
        } catch (Exception e) {
            log.error("error while getting credit card by userId: ", e);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ArrayList<>());
    }

    @GetMapping
    public ResponseEntity<ListViewDto<BankAccountDto>> findAllBankAccount() {
        try {
            Pair<PageRequest, List<Filter>> pair = getPageRequestAndFilters();
            return ResponseEntity.ok(bankAccountService.findAllBankAccount(pair.getFirst(), pair.getSecond()));
        } catch (Exception e) {
            log.error("error while getting bank account: ", e);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ListViewDto<BankAccountDto>("Internal server error"));
    }

    @PostMapping
    public ResponseEntity<String> saveBankAccount(@RequestBody BankAccountDto bankAccountDto) {
        try {
            bankAccountService.saveBankAccount(bankAccountDto, getUserId());
        } catch (Exception e) {
            log.error("error while saving bank account: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
        return ResponseEntity.ok(AppConstants.SUCCESS);
    }

    @PostMapping("batch")
    public ResponseEntity<String> saveBatchBankAccounts(@RequestBody List<BankAccountDto> bankAccountDtos) {
        try {
            for (BankAccountDto bankAccountDto : bankAccountDtos) {
                bankAccountService.saveBankAccount(bankAccountDto, getUserId());
            }
        } catch (Exception e) {
            log.error("error while saving bank account: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
        return ResponseEntity.ok(AppConstants.SUCCESS);
    }

    @PatchMapping("{id}")
    public ResponseEntity<String> updateBankAccount(@PathVariable("id") Long id,
                                                    @RequestBody BankAccountDto bankAccountDto) {
        try {
            bankAccountService.updateBankAccount(bankAccountDto, id, getUserId());
        } catch (Exception e) {
            log.error("error while updating bank account: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
        return ResponseEntity.ok(AppConstants.SUCCESS);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteBankAccount(@PathVariable("id") Long id) {
        try {
            return ResponseEntity.ok(bankAccountService.deleteBankAccount(id, getUserId()));
        } catch (Exception e) {
            log.error("error while deleting bank account: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @Override
    public void setSearchFilters(List<Filter> list, String searchBy) {
        List<Operands> operandList = new ArrayList<>();
        operandList.add(new Operands(FieldConstant.BANK_NAME, Operators.LIKE, searchBy));
        operandList.add(new Operands(FieldConstant.LAST_4_DIGIT, Operators.LIKE, searchBy));
        operandList.add(new Operands(FieldConstant.DEBIT_CARD_LAST_4_DIGIT, Operators.LIKE, searchBy));

        list.add(new Filter(Operators.OR, operandList));

    }

}
