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

import com.finance.sugarmarket.app.dto.LoanDto;
import com.finance.sugarmarket.app.dto.ModifyLoanDto;
import com.finance.sugarmarket.app.service.LoanService;
import com.finance.sugarmarket.base.controller.BaseController;
import com.finance.sugarmarket.constants.MFConstants;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/app")
public class LoanController extends BaseController {
	
	@Autowired
	private LoanService loanService;
	
	private static final Logger log = LoggerFactory.getLogger(LoanController.class);
	
	@GetMapping("/get-loan-details")
	public List<LoanDto> findAllCreditCard() {
		return loanService.findAllLoans(getUserName());
	}
	
	@PostMapping("/save-loan-detail")
	public ResponseEntity<String> saveCreditCard(@RequestBody LoanDto loanDto) {
		try {
			loanService.saveOrUpdateLoanDetails(loanDto, getUserName());
		} catch (Exception e) {
			log.error("error while saving credit card: ", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(MFConstants.FAILED);
		}
		return ResponseEntity.ok(MFConstants.SUCCESS);
	}
	
	@PostMapping("/modify-loan")
	public ResponseEntity<String> modifyLoan(@RequestBody ModifyLoanDto modifyLoanDto) {
		try {
			loanService.modifyLoanDetails(modifyLoanDto);
		} catch (Exception e) {
			log.error("error while saving credit card: ", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(MFConstants.FAILED);
		}
		return ResponseEntity.ok(MFConstants.SUCCESS);
	}
}
