package com.finance.sugarmarket.app.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.finance.sugarmarket.app.dto.ExpenseDto;
import com.finance.sugarmarket.app.model.Expense;
import com.finance.sugarmarket.app.service.CreditCardService;
import com.finance.sugarmarket.app.service.ExpenseService;
import com.finance.sugarmarket.base.controller.BaseController;
import com.finance.sugarmarket.constants.MFConstants;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/app")
public class ExpenseController extends BaseController {
	
	@Autowired
	private ExpenseService expenseService;
	@Autowired
	private CreditCardService creditCardService;
	
private static final Logger log = LoggerFactory.getLogger(ExpenseController.class);
	
	@GetMapping("/get-expense")
	public List<ExpenseDto> findAllExpense() {
		return expenseService.findAllExpense(getUserName());
	}
	
	@PostMapping("/save-expense")
	public ResponseEntity<String> saveExpense(@RequestBody Expense expense) {
		try {
			expense.setCreditCard(
					creditCardService.findByCreditCardId(expense.getCreditCard().getId()));
			expenseService.saveExpense(expense);
		} catch (Exception e) {
			log.error("error while saving expense: ", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(MFConstants.FAILED);
		}
		return ResponseEntity.ok(MFConstants.SUCCESS);
	}
	
	@PostMapping("/delete-expense")
	public ResponseEntity<String> deleteById(@RequestBody Expense expense) {
		try {
			expenseService.deleteExpense(expense.getId());
		} catch (Exception e) {
			log.error("error while deleting expense: ", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(MFConstants.FAILED);
		}
		return ResponseEntity.ok(MFConstants.SUCCESS);
	}
}
