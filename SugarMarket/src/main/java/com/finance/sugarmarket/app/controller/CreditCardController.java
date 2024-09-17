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

import com.finance.sugarmarket.app.dto.CreditCardDto;
import com.finance.sugarmarket.app.model.CreditCard;
import com.finance.sugarmarket.app.service.CreditCardService;
import com.finance.sugarmarket.auth.service.MFUserService;
import com.finance.sugarmarket.base.controller.BaseController;
import com.finance.sugarmarket.constants.MFConstants;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/app")
public class CreditCardController extends BaseController {
	
	@Autowired
	private CreditCardService creditCardService;
	@Autowired
	private MFUserService userService;
	
	private static final Logger log = LoggerFactory.getLogger(CreditCardController.class);
	
	@GetMapping("/get-credit-cards")
	public List<CreditCardDto> findAllCreditCard() {
		return creditCardService.findAllCreditCard(getUserName());
	}
	
	@PostMapping("/save-credit-card")
	public ResponseEntity<String> saveCreditCard(@RequestBody CreditCard cardDetail) {
		try {
			cardDetail.setUser(userService.getUserByUsername(getUserName()));
			creditCardService.saveCreditCard(cardDetail);
		} catch (Exception e) {
			log.error("error while saving credit card: ", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(MFConstants.FAILED);
		}
		return ResponseEntity.ok(MFConstants.SUCCESS);
	}
	
	@PostMapping("delete-credit-card")
	public ResponseEntity<String> deleteCreditCard(@RequestBody CreditCard cardDetail) {
		try {
			return ResponseEntity.ok(creditCardService.deleteCreditCard(cardDetail.getId()));
		} catch (Exception e) {
			log.error("error while saving credit card: ", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(MFConstants.FAILED);
		}
	}

}
