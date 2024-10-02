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

import com.finance.sugarmarket.app.dto.BankAccountDto;
import com.finance.sugarmarket.app.service.BankAccountService;
import com.finance.sugarmarket.base.controller.BaseController;
import com.finance.sugarmarket.base.dto.Filter;
import com.finance.sugarmarket.base.dto.ListViewDto;
import com.finance.sugarmarket.base.enums.FilterOperation;
import com.finance.sugarmarket.constants.AppConstants;
import com.finance.sugarmarket.constants.FieldConstant;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/app/bank-account")
public class BankAccountController extends BaseController {

	@Autowired
	private BankAccountService bankAccountService;

	private static final Logger log = LoggerFactory.getLogger(CreditCardController.class);

	@GetMapping
	public ListViewDto<BankAccountDto> findAllBankAccount() {
		Pair<PageRequest, List<Filter>> pair = getPageRequestAndFilters();
		return bankAccountService.findAllBankAccount(pair.getFirst(), pair.getSecond());
	}

	@PostMapping
	public ResponseEntity<String> saveBankAccount(@RequestBody BankAccountDto bankAccountDto) {
		try {
			bankAccountService.saveBankAccount(bankAccountDto, getUserId());
		} catch (Exception e) {
			log.error("error while saving credit card: ", e);
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
			log.error("error while saving credit card: ", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
		return ResponseEntity.ok(AppConstants.SUCCESS);
	}

	@DeleteMapping("{id}")
	public ResponseEntity<String> deleteBankAccount(@PathVariable("id") Long id) {
		try {
			return ResponseEntity.ok(bankAccountService.deleteBankAccount(id, getUserId()));
		} catch (Exception e) {
			log.error("error while saving credit card: ", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}

	public void setSearchFilters(List<Filter> list, String searchBy) {
		list.add(new Filter(FieldConstant.BANK_NAME, FilterOperation.LIKE, searchBy));
		list.add(new Filter(FieldConstant.LAST_4_DIGIT, FilterOperation.LIKE, searchBy));
		list.add(new Filter(FieldConstant.DEBIT_CARD_LAST_4_DIGIT, FilterOperation.LIKE, searchBy));
	}

}
