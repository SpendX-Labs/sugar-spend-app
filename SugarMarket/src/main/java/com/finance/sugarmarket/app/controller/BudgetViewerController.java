package com.finance.sugarmarket.app.controller;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.finance.sugarmarket.app.dto.BudgetDto;
import com.finance.sugarmarket.app.model.BudgetView;
import com.finance.sugarmarket.app.service.BudgetViewerService;
import com.finance.sugarmarket.base.controller.BaseController;
import com.finance.sugarmarket.constants.MFConstants;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/app")
public class BudgetViewerController extends BaseController {

	@Autowired
	private BudgetViewerService budgetViewerService;
	
	private static final Logger log = LoggerFactory.getLogger(BudgetViewerController.class);
	
	@PostMapping("/get-budgets")
	public List<BudgetView> findAllBudget(@RequestBody Map<String, String> map) {
		return budgetViewerService.findAllBudget(Integer.parseInt(map.get("budgetYear")), map.get("budgetMonth"),
				getUserName());
	}
	
	@PostMapping("/get-budget-chart")
	public List<BudgetDto> getBudgetChart(@RequestBody Map<String, String> map) {
		return budgetViewerService.getBudgetChart(Integer.parseInt(map.get("budgetYear")), map.get("budgetMonth"),
				getUserName());
	}
	
	@PostMapping("/get-pie-chart")
	public List<BudgetDto> getPieChart(@RequestBody Map<String, String> map){
		return budgetViewerService.getPieChart(Integer.parseInt(map.get("budgetYear")), map.get("budgetMonth"),
				getUserName());
	}
	
	@PostMapping("/get-total-amount")
	public Map<String, BigDecimal> getTotalAmount(@RequestBody Map<String, String> map){
		return budgetViewerService.getTotalAmount(Integer.parseInt(map.get("budgetYear")), map.get("budgetMonth"),
				getUserName());
	}
	
	@PostMapping("/update-budget-data")
	public ResponseEntity<String> updateBudgetData() {
		try {
			budgetViewerService.updateBudgetView(new Date(), getUserId());
		} catch (Exception e) {
			log.error("error while updating budget: ", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(MFConstants.FAILED);
		}
		return ResponseEntity.ok(MFConstants.SUCCESS);
		
	}
	
	@PostMapping("/modify-remaing-amount")
	public ResponseEntity<String> modifyRemainingAmount(@RequestBody BudgetView budget) {
		try {
			BudgetView orgOBudget = budgetViewerService.findBudgetById(budget.getId());
			orgOBudget.setRemainingAmount(budget.getRemainingAmount());
			budgetViewerService.saveBudget(orgOBudget);
		}
		catch (Exception e) {
			log.error("error while modifying remiang amount: ", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(MFConstants.FAILED);
		}
		return ResponseEntity.ok(MFConstants.SUCCESS);
	}

}
