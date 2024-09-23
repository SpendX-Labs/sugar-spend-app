package com.finance.sugarmarket.app.controller;

import java.util.List;

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

import com.finance.sugarmarket.app.dto.ExpenseDto;
import com.finance.sugarmarket.app.service.ExpenseService;
import com.finance.sugarmarket.base.controller.BaseController;
import com.finance.sugarmarket.base.dto.Filter;
import com.finance.sugarmarket.constants.AppConstants;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/app/expense")
public class ExpenseController extends BaseController {
	
	@Autowired
	private ExpenseService expenseService;
	
private static final Logger log = LoggerFactory.getLogger(ExpenseController.class);
	
	@GetMapping
	public List<ExpenseDto> findAllExpense() {
		Pair<PageRequest, List<Filter>> pair = getPageRequestAndFilters();
		return expenseService.findAllExpense(pair.getFirst(), pair.getSecond());
	}
	
	@PostMapping("save")
	public ResponseEntity<String> saveExpense(@RequestBody ExpenseDto expenseDto) {
		try {
			expenseService.saveExpense(expenseDto, getUserId());
		} catch (Exception e) {
			log.error("error while saving expense: ", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
		}
		return ResponseEntity.ok(AppConstants.SUCCESS);
	}
	
	@PatchMapping("update/{id}")
	public ResponseEntity<String> updateExpense(@PathVariable("id") Integer id, @RequestBody ExpenseDto expenseDto) {
		try {
			expenseService.updateExpense(expenseDto, id, getUserId());
		} catch (Exception e) {
			log.error("error while saving expense: ", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
		}
		return ResponseEntity.ok(AppConstants.SUCCESS);
	}
	
	@DeleteMapping("delete/{id}")
	public ResponseEntity<String> deleteById(@PathVariable("id") Integer id) {
		try {
			expenseService.deleteExpense(id, getUserId());
		} catch (Exception e) {
			log.error("error while deleting expense: ", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
		}
		return ResponseEntity.ok(AppConstants.SUCCESS);
	}
}
