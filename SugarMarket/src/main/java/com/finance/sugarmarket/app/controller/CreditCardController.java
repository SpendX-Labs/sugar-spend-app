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

import com.finance.sugarmarket.app.dto.CreditCardDto;
import com.finance.sugarmarket.app.service.CreditCardService;
import com.finance.sugarmarket.base.controller.BaseController;
import com.finance.sugarmarket.base.dto.Filter;
import com.finance.sugarmarket.base.dto.ListViewDto;
import com.finance.sugarmarket.constants.AppConstants;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/app/credit-card")
public class CreditCardController extends BaseController {

	@Autowired
	private CreditCardService creditCardService;

	private static final Logger log = LoggerFactory.getLogger(CreditCardController.class);

	@GetMapping
	public ListViewDto<CreditCardDto> findAllCreditCard() {
		Pair<PageRequest, List<Filter>> pair = getPageRequestAndFilters();
		return creditCardService.findAllCreditCard(pair.getFirst(), pair.getSecond());
	}

	@PostMapping
	public ResponseEntity<String> saveCreditCard(@RequestBody CreditCardDto cardDetailDto) {
		try {
			creditCardService.saveCreditCard(cardDetailDto, getUserId());
		} catch (Exception e) {
			log.error("error while saving credit card: ", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
		return ResponseEntity.ok(AppConstants.SUCCESS);
	}

	@PatchMapping("{id}")
	public ResponseEntity<String> updateCreditCard(@PathVariable("id") Integer id,
			@RequestBody CreditCardDto cardDetailDto) {
		try {
			creditCardService.updateCreditCard(cardDetailDto, id, getUserId());
		} catch (Exception e) {
			log.error("error while saving credit card: ", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
		return ResponseEntity.ok(AppConstants.SUCCESS);
	}

	@DeleteMapping("{id}")
	public ResponseEntity<String> deleteCreditCard(@PathVariable("id") Integer id) {
		try {
			return ResponseEntity.ok(creditCardService.deleteCreditCard(id, getUserId()));
		} catch (Exception e) {
			log.error("error while saving credit card: ", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}

}
