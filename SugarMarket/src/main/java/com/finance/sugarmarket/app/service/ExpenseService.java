package com.finance.sugarmarket.app.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.finance.sugarmarket.app.dto.ExpenseDto;
import com.finance.sugarmarket.app.model.CreditCard;
import com.finance.sugarmarket.app.model.Expense;
import com.finance.sugarmarket.app.repo.CreditCardRepo;
import com.finance.sugarmarket.app.repo.ExpenseRepo;
import com.finance.sugarmarket.base.dto.Filter;
import com.finance.sugarmarket.base.dto.ListViewDto;
import com.finance.sugarmarket.base.service.SpecificationService;
import com.finance.sugarmarket.constants.AppConstants;
import com.finance.sugarmarket.constants.FilterFieldConstant;

@Service
public class ExpenseService extends SpecificationService<Expense> {

	@Autowired
	private ExpenseRepo expenseRepo;
	@Autowired
	private CreditCardRepo creditCardRepo;
	@Autowired
	private ModelMapper modelMapper;

	private static final Map<String, String> filterMap = new HashMap<String, String>();

	static {
		filterMap.put(FilterFieldConstant.USER_ID, "creditCard.user.id");
		filterMap.put(FilterFieldConstant.CREDIT_CARD_ID, "creditCard.id");
	}

	public ListViewDto<ExpenseDto> findAllExpense(PageRequest pageRequest, List<Filter> filters) {
		Specification<Expense> specificationFilters = getSpecificationFilters(filters, filterMap);
		Page<Expense> pages = expenseRepo.findAll(specificationFilters, pageRequest);
		List<ExpenseDto> listDto = new ArrayList<>();
		for (Expense expense : pages.getContent()) {
			ExpenseDto expenseDto = modelMapper.map(expense, ExpenseDto.class);
			expenseDto.setCrediCardId(expense.getCreditCard().getId());
			String crediCardName = expense.getCreditCard().getBankName() + expense.getCreditCard().getCreditCardName()
					+ " (" + expense.getCreditCard().getLast4Digit() + ") ";
			expenseDto.setCreditCardName(crediCardName);
			listDto.add(expenseDto);
		}
		return new ListViewDto<ExpenseDto>(listDto, pages.getTotalElements(), pageRequest.getOffset(),
				pageRequest.getPageSize());
	}

	public void saveExpense(ExpenseDto expenseDto, Integer userId) throws Exception {
		CreditCard creditCard = creditCardRepo.findById(expenseDto.getCrediCardId()).get();
		if (creditCard.getUser().getId() != userId) {
			throw new Exception("user is different from the credit card.");
		}
		Expense expense = modelMapper.map(expenseDto, Expense.class);
		expense.setCreditCard(creditCard);
		expenseRepo.save(expense);
	}

	public void updateExpense(ExpenseDto expenseDto, Integer id, Integer userId) throws Exception {
		Specification<Expense> specificationFilters = getAuditSpecificationFilters(filterMap, id, userId);
		List<Expense> expenseList = expenseRepo.findAll(specificationFilters);
		if (expenseList.isEmpty()) {
			throw new Exception("You are not authorised to modify");
		}
		Expense existingExpense = expenseList.get(0);
		modelMapper.map(expenseDto, existingExpense);
		existingExpense.setId(id);
		expenseRepo.save(existingExpense);
	}

	public String deleteExpense(Integer id, Integer userId) throws Exception {
		Specification<Expense> specificationFilters = getAuditSpecificationFilters(filterMap, id, userId);
		List<Expense> expenseList = expenseRepo.findAll(specificationFilters);
		if (expenseList.isEmpty()) {
			throw new Exception("You are not authorised to modify");
		}
		Expense existingExpense = expenseList.get(0);
		creditCardRepo.deleteById(existingExpense.getId());
		return AppConstants.SUCCESS;
	}
}
