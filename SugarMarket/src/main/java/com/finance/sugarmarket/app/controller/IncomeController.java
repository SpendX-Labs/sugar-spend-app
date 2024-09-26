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

import com.finance.sugarmarket.app.dto.IncomeDto;
import com.finance.sugarmarket.app.service.IncomeService;
import com.finance.sugarmarket.base.controller.BaseController;
import com.finance.sugarmarket.base.dto.Filter;
import com.finance.sugarmarket.base.dto.ListViewDto;
import com.finance.sugarmarket.constants.AppConstants;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/app/income")
public class IncomeController extends BaseController {
	@Autowired
	private IncomeService incomeService;

	private static final Logger log = LoggerFactory.getLogger(IncomeController.class);

	@GetMapping
	public ListViewDto<IncomeDto> findAllIncome() {
		Pair<PageRequest, List<Filter>> pair = getPageRequestAndFilters();
		return incomeService.findAllIncome(pair.getFirst(), pair.getSecond());
	}

	@PostMapping
	public ResponseEntity<String> saveIncome(@RequestBody IncomeDto incomeDto) {
		try {
			incomeService.saveIncome(incomeDto, getUserId());
		} catch (Exception e) {
			log.error("error while saving income: ", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
		return ResponseEntity.ok(AppConstants.SUCCESS);
	}

	@PatchMapping("{id}")
	public ResponseEntity<String> updateIncome(@PathVariable("id") Long id, @RequestBody IncomeDto incomeDto) {
		try {
			incomeService.updateIncome(incomeDto, id, getUserId());
		} catch (Exception e) {
			log.error("error while saving income: ", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
		return ResponseEntity.ok(AppConstants.SUCCESS);
	}

	@DeleteMapping("{id}")
	public ResponseEntity<String> deleteById(@PathVariable("id") Long id) {
		try {
			incomeService.deleteIncome(id, getUserId());
		} catch (Exception e) {
			log.error("error while deleting income: ", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
		return ResponseEntity.ok(AppConstants.SUCCESS);
	}
}
