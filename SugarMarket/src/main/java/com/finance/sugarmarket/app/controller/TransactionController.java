package com.finance.sugarmarket.app.controller;

import com.finance.sugarmarket.app.dto.TransactionDto;
import com.finance.sugarmarket.app.dto.TransactionMobileInputDto;
import com.finance.sugarmarket.app.service.TransactionService;
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
@RequestMapping("/app/transaction")
public class TransactionController extends BaseController {

    private static final Logger log = LoggerFactory.getLogger(TransactionController.class);

    @Autowired
    private TransactionService transactionService;

    @GetMapping
    public ResponseEntity<ListViewDto<TransactionDto>> findAllTransactions() {
        try {
            Pair<PageRequest, List<Filter>> pair = getPageRequestAndFilters();
            return ResponseEntity.ok(transactionService.findAllTransactions(pair.getFirst(), pair.getSecond()));
        } catch (Exception e) {
            log.error("error while getting transaction: ", e);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ListViewDto<>("Internal server error"));
    }

    @PostMapping
    public ResponseEntity<String> saveTransaction(@RequestBody TransactionDto transactionDto) {
        try {
            transactionService.saveTransaction(transactionDto, getUserId());
        } catch (Exception e) {
            log.error("error while saving transaction: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
        return ResponseEntity.ok(AppConstants.SUCCESS);
    }

    @PatchMapping("{id}")
    public ResponseEntity<String> updateTransaction(@PathVariable("id") Long id, @RequestBody TransactionDto transactionDto) {
        try {
            transactionService.updateTransaction(transactionDto, id, getUserId());
        } catch (Exception e) {
            log.error("error while updating transaction: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
        return ResponseEntity.ok(AppConstants.SUCCESS);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteById(@PathVariable("id") Long id) {
        try {
            transactionService.deleteTransaction(id, getUserId());
        } catch (Exception e) {
            log.error("error while deleting transaction: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
        return ResponseEntity.ok(AppConstants.SUCCESS);
    }

    @PostMapping("mobile")
    public ResponseEntity<String> saveTransactionForMobile(@RequestBody TransactionMobileInputDto transaction) {
        try {
            transactionService.saveTransactionForMobile(transaction, getUserId());
        } catch (Exception e) {
            log.error("error while saving transaction: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
        return ResponseEntity.ok(AppConstants.SUCCESS);
    }

    @Override
    public void setSearchFilters(List<Filter> list, String searchBy) {
        List<Operands> operandList = new ArrayList<>();
        operandList.add(new Operands(FieldConstant.BANK_ACCOUNT_NAME, Operators.LIKE, searchBy));
        operandList.add(new Operands(FieldConstant.BANK_NAME_FOR_CARD, Operators.LIKE, searchBy));
        operandList.add(new Operands(FieldConstant.CREDIT_CARD_NAME, Operators.LIKE, searchBy));
        operandList.add(new Operands(FieldConstant.BANK_LAST_4_DIGIT, Operators.LIKE, searchBy));
        operandList.add(new Operands(FieldConstant.CARD_LAST_4_DIGIT, Operators.LIKE, searchBy));

        list.add(new Filter(Operators.OR, operandList));
    }
}
