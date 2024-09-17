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

import com.finance.sugarmarket.agent.service.SchedulerService;
import com.finance.sugarmarket.app.model.MutualFund;
import com.finance.sugarmarket.app.service.MutualFundService;
import com.finance.sugarmarket.app.utils.MarketDataUtil;
import com.finance.sugarmarket.base.controller.BaseController;
import com.finance.sugarmarket.constants.MFConstants;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/app")
public class MutualFundController extends BaseController {

	@Autowired
	private MutualFundService mutualFundService;
	@Autowired
	private SchedulerService schedulerService;
	
	private static final Logger log = LoggerFactory.getLogger(MutualFundController.class);

	@GetMapping("/get-mutualfunds")
	public List<MutualFund> findAllMutualFunds() {
		return mutualFundService.findAllMutualFunds();
	}
	
	@GetMapping("/get-mf-api-data")
	public List<MutualFund> findMfApiMutualFunds() {
		return MarketDataUtil.getMFList();
	}
	
	@PostMapping("/save-mutualfund")
	public ResponseEntity<String> saveMutualFund(@RequestBody MutualFund fund) {
		try {
			checkAdminRole();
			mutualFundService.saveMutualFund(fund);
		} catch (Exception e) {
			log.error("error while saving mutual fund: ", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(MFConstants.FAILED);
		}
		return ResponseEntity.ok(MFConstants.SUCCESS);
	}
	
	@PostMapping("/trigger-update-nav")
	public ResponseEntity<String> triggerUpdateNav() {
		try {
			checkAdminRole();
			schedulerService.triggerAgentByClass("UpdateMutualFundAgent");
		}
		catch (Exception e) {
			log.error("error while triggering update nav: ", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(MFConstants.FAILED);
		}
		
		return ResponseEntity.ok(MFConstants.SUCCESS);
	}
}
