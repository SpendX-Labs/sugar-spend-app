package com.finance.sugarmarket.app.controller;

import com.finance.sugarmarket.app.dto.CreditCardDto;
import com.finance.sugarmarket.app.service.CreditCardService;
import com.finance.sugarmarket.base.controller.BaseController;
import com.finance.sugarmarket.base.dto.Filter;
import com.finance.sugarmarket.base.dto.ListViewDto;
import com.finance.sugarmarket.base.dto.Operands;
import com.finance.sugarmarket.base.enums.Operators;
import com.finance.sugarmarket.constants.AppConstants;
import com.finance.sugarmarket.constants.FieldConstant;
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

import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/app/credit-card")
public class CreditCardController extends BaseController {

    @Autowired
    private CreditCardService creditCardService;

    private static final Logger log = LoggerFactory.getLogger(CreditCardController.class);

    @GetMapping("all")
    public ResponseEntity<List<CreditCardDto>> findAllCreditCardByUserId() {
        try {
            return ResponseEntity.ok(creditCardService.findAllCreditCardsByUserId(getUserId()));
        } catch (Exception e) {
            log.error("error while getting credit card by userId: ", e);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ArrayList<>());
    }

    @GetMapping
    public ResponseEntity<ListViewDto<CreditCardDto>> findAllCreditCard() {
        try {
            Pair<PageRequest, List<Filter>> pair = getPageRequestAndFilters();
            return ResponseEntity.ok(creditCardService.findAllCreditCard(pair.getFirst(), pair.getSecond()));
        } catch (Exception e) {
            log.error("error while getting credit card: ", e);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ListViewDto<CreditCardDto>("Internal server error"));
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

    @PostMapping("batch")
    public ResponseEntity<String> saveBatchCreditCards(@RequestBody List<CreditCardDto> cardDetailDtos) {
        try {
            for (CreditCardDto cardDetailDto : cardDetailDtos) {
                creditCardService.saveCreditCard(cardDetailDto, getUserId());
            }
        } catch (Exception e) {
            log.error("error while saving bank account: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
        return ResponseEntity.ok(AppConstants.SUCCESS);
    }

    @PatchMapping("{id}")
    public ResponseEntity<String> updateCreditCard(@PathVariable("id") Long id,
                                                   @RequestBody CreditCardDto cardDetailDto) {
        try {
            creditCardService.updateCreditCard(cardDetailDto, id, getUserId());
        } catch (Exception e) {
            log.error("error while updating credit card: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
        return ResponseEntity.ok(AppConstants.SUCCESS);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteCreditCard(@PathVariable("id") Long id) {
        try {
            return ResponseEntity.ok(creditCardService.deleteCreditCard(id, getUserId()));
        } catch (Exception e) {
            log.error("error while deleting credit card: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @Override
    public void setSearchFilters(List<Filter> list, String searchBy) {
        List<Operands> operandList = new ArrayList<>();
        operandList.add(new Operands(FieldConstant.BANK_NAME, Operators.LIKE, searchBy));
        operandList.add(new Operands(FieldConstant.CREDIT_CARD_NAME, Operators.LIKE, searchBy));
        operandList.add(new Operands(FieldConstant.LAST_4_DIGIT, Operators.LIKE, searchBy));

        list.add(new Filter(Operators.OR, operandList));
    }

}
