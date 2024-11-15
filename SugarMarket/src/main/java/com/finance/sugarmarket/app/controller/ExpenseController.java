package com.finance.sugarmarket.app.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
import com.finance.sugarmarket.base.dto.ListViewDto;
import com.finance.sugarmarket.constants.AppConstants;
import com.finance.sugarmarket.constants.FieldConstant;

import jakarta.servlet.http.HttpServletRequest;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/app/expense")
public class ExpenseController extends BaseController {

	@Autowired
	private ExpenseService expenseService;

	private static final Logger log = LoggerFactory.getLogger(ExpenseController.class);

	@GetMapping
	public ResponseEntity<ListViewDto<ExpenseDto>> findAllExpense() {
		try {
			Pair<PageRequest, List<Filter>> pair = getPageRequestAndFilters();
			return ResponseEntity.ok(expenseService.findAllExpense(pair.getFirst(), pair.getSecond()));
		} catch (Exception e) {
			log.error("error while getting expense: ", e);
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(new ListViewDto<ExpenseDto>("Internal server error"));
	}

	@PostMapping
	public ResponseEntity<String> saveExpense(@RequestBody ExpenseDto expenseDto) {
		try {
			expenseService.saveExpense(expenseDto, getUserId());
		} catch (Exception e) {
			log.error("error while saving expense: ", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
		return ResponseEntity.ok(AppConstants.SUCCESS);
	}

	@PostMapping("batch")
	public ResponseEntity<String> saveBatchExpenses(@RequestBody List<ExpenseDto> expenseDtos) {
		try {
			for (ExpenseDto expenseDto : expenseDtos) {
				expenseService.saveExpense(expenseDto, getUserId());
			}
		} catch (Exception e) {
			log.error("error while saving expense: ", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
		return ResponseEntity.ok(AppConstants.SUCCESS);
	}

	@PatchMapping("{id}")
	public ResponseEntity<String> updateExpense(@PathVariable("id") Long id, @RequestBody ExpenseDto expenseDto) {
		try {
			expenseService.updateExpense(expenseDto, id, getUserId());
		} catch (Exception e) {
			log.error("error while updating expense: ", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
		return ResponseEntity.ok(AppConstants.SUCCESS);
	}

	@DeleteMapping("{id}")
	public ResponseEntity<String> deleteById(@PathVariable("id") Long id) {
		try {
			expenseService.deleteExpense(id, getUserId());
		} catch (Exception e) {
			log.error("error while deleting expense: ", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
		return ResponseEntity.ok(AppConstants.SUCCESS);
	}

	@Override
	public Map<String, Sort.Direction> getDefaultSortColumns(HttpServletRequest request) {
		Map<String, Sort.Direction> map = new HashMap<>();
		map.putAll(super.getDefaultSortColumns(request));
		map.put(FieldConstant.EXPENSE_DATE, Sort.Direction.DESC);
		map.put(FieldConstant.EXPENSE_TIME, Sort.Direction.DESC);
		return map;
	}
}
