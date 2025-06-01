package com.finance.sugarmarket.app.controller;

import com.finance.sugarmarket.app.dto.TransactionDto;
import com.finance.sugarmarket.app.service.TransactionService;
import com.finance.sugarmarket.base.controller.BaseController;
import com.finance.sugarmarket.constants.AppConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/app/transaction")
public class TransactionController extends BaseController {

    private static final Logger log = LoggerFactory.getLogger(TransactionController.class);

    @Autowired
    private TransactionService transactionService;

    @PostMapping
    public ResponseEntity<String> saveTransaction(@RequestBody TransactionDto transaction) {
        try {
            transactionService.saveTransaction(transaction, getUserId());
        } catch (Exception e) {
            log.error("error while saving expense: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
        return ResponseEntity.ok(AppConstants.SUCCESS);
    }
}
