package com.finance.sugarmarket.app.controller;

import com.finance.sugarmarket.app.budgetview.dto.AutoDebitDto;
import com.finance.sugarmarket.app.budgetview.dto.ExpenseReportDto;
import com.finance.sugarmarket.app.service.BudgetViewerService;
import com.finance.sugarmarket.app.service.CreditCardService;
import com.finance.sugarmarket.base.controller.BaseController;
import com.finance.sugarmarket.constants.AppConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/app/budget")
public class BudgetViewerController extends BaseController {

    @Autowired
    private BudgetViewerService budgetViewerService;
    @Autowired
    private CreditCardService creditCardService;

    private static final Logger log = LoggerFactory.getLogger(BudgetViewerController.class);

    @GetMapping("expense-report")
    public ExpenseReportDto getExpenseReport(@RequestParam(value = "month", required = false) String month,
                                             @RequestParam(value = "year", required = true) Integer year) {
        return budgetViewerService.getExpenseReport(getUserId(), month, year);
    }

    @GetMapping("next-month-report")
    public AutoDebitDto getNextMonthBudget() {
        return budgetViewerService.getNextMonthBudget(getUserId());
    }

    @PostMapping("update-budget-data/{userId}")
    public ResponseEntity<String> updateBudgetData(@PathVariable("userId") Long userId) {
        try {
            checkAdminRole();
            creditCardService.findAllCreditCardsByUserId(userId).forEach(creditCard -> {
                budgetViewerService.updateBudgetView(new Date(), creditCard.getId());
            });
        } catch (Exception e) {
            log.error("error while updating budget: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(AppConstants.FAILED);
        }
        return ResponseEntity.ok(AppConstants.SUCCESS);
    }

}
