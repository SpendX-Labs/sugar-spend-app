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

import com.finance.sugarmarket.app.dto.LoanDto;
import com.finance.sugarmarket.app.dto.LoanPrepaymentRequestDto;
import com.finance.sugarmarket.app.service.LoanService;
import com.finance.sugarmarket.base.controller.BaseController;
import com.finance.sugarmarket.base.dto.Filter;
import com.finance.sugarmarket.base.dto.ListViewDto;
import com.finance.sugarmarket.constants.AppConstants;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/app/loan")
public class LoanController extends BaseController {

	@Autowired
	private LoanService loanService;

	private static final Logger log = LoggerFactory.getLogger(LoanController.class);

	@GetMapping
	public ResponseEntity<ListViewDto<LoanDto>> findAllCreditCard() {
		try {
			Pair<PageRequest, List<Filter>> pair = getPageRequestAndFilters();
			return ResponseEntity.ok(loanService.findAllLoans(pair.getFirst(), pair.getSecond()));
		} catch (Exception e) {
			log.error("error while getting loan: ", e);
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(new ListViewDto<LoanDto>("Internal server error"));
	}

	@PostMapping
	public ResponseEntity<String> saveCreditCard(@RequestBody LoanDto loanDto) {
		try {
			loanService.saveLoan(loanDto, getUserId());
		} catch (Exception e) {
			log.error("error while saving loan: ", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(AppConstants.FAILED);
		}
		return ResponseEntity.ok(AppConstants.SUCCESS);
	}

	@PatchMapping("{id}")
	public ResponseEntity<String> updateExpense(@PathVariable("id") Long id, @RequestBody LoanDto loanDto) {
		try {
			loanService.updateLoan(loanDto, id, getUserId());
		} catch (Exception e) {
			log.error("error while updaing loan: ", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(AppConstants.FAILED);
		}
		return ResponseEntity.ok(AppConstants.SUCCESS);
	}

	@PatchMapping("{id}/prepayment")
	public ResponseEntity<String> makePrincipalPrepayment(@PathVariable("id") Long id,
			@RequestBody LoanPrepaymentRequestDto modifyLoanDto) {
		try {
			loanService.makePrincipalPrepayment(modifyLoanDto, id, getUserId());
		} catch (Exception e) {
			log.error("error while pre paying loan: ", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(AppConstants.FAILED);
		}
		return ResponseEntity.ok(AppConstants.SUCCESS);
	}

	@DeleteMapping("{id}")
	public ResponseEntity<String> deleteById(@PathVariable("id") Long id) {
		try {
			loanService.deleteLoan(id, getUserId());
		} catch (Exception e) {
			log.error("error while deleting loan: ", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
		return ResponseEntity.ok(AppConstants.SUCCESS);
	}
}
