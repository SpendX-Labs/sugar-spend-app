package com.finance.sugarmarket.app.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.finance.sugarmarket.app.dto.CashFlowDetailDto;
import com.finance.sugarmarket.app.dto.ExpenseDto;
import com.finance.sugarmarket.app.enums.CashFlowType;
import com.finance.sugarmarket.app.model.BankAccount;
import com.finance.sugarmarket.app.model.CreditCard;
import com.finance.sugarmarket.app.model.Expense;
import com.finance.sugarmarket.app.repo.BankAccountRepo;
import com.finance.sugarmarket.app.repo.BudgetViewRepo;
import com.finance.sugarmarket.app.repo.CreditCardRepo;
import com.finance.sugarmarket.app.repo.ExpenseRepo;
import com.finance.sugarmarket.auth.repo.MFUserRepo;
import com.finance.sugarmarket.base.dto.Filter;
import com.finance.sugarmarket.base.dto.ListViewDto;
import com.finance.sugarmarket.base.service.SpecificationService;
import com.finance.sugarmarket.constants.AppConstants;
import com.finance.sugarmarket.constants.FieldConstant;

@Service
public class ExpenseService extends SpecificationService<Expense> {

	@Autowired
	private ExpenseRepo expenseRepo;
	@Autowired
	private CreditCardRepo creditCardRepo;
	@Autowired
	private BankAccountRepo bankAccountRepo;
	@Autowired
	private MFUserRepo userRepo;
	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	private BudgetViewRepo budgetViewRepo;

	private static final Map<String, String> filterMap = new HashMap<String, String>();

	static {
		filterMap.put(FieldConstant.USER_ID, "user.id");
		filterMap.put(FieldConstant.CREDIT_CARD_ID, "creditCard.id");
	}

	public ListViewDto<ExpenseDto> findAllExpense(PageRequest pageRequest, List<Filter> filters) throws Exception {
		Specification<Expense> specificationFilters = getSpecificationFilters(filters, filterMap);
		Page<Expense> pages = expenseRepo.findAll(specificationFilters, pageRequest);
		List<ExpenseDto> listDto = new ArrayList<>();
		for (Expense expense : pages.getContent()) {
			ExpenseDto expenseDto = modelMapper.map(expense, ExpenseDto.class);
			if (expense.getCreditCard() != null) {
				String crediCardName = expense.getCreditCard().getBankName() + " "
						+ expense.getCreditCard().getCreditCardName() + " (XXXX"
						+ expense.getCreditCard().getLast4Digit() + ") ";
				expenseDto.setCashFlowDetails(new CashFlowDetailDto(expense.getCreditCard().getId(), crediCardName));
			} else if (expense.getBankAccount() != null) {
				String bankAccountName = expense.getBankAccount().getBankName() + " (XXXX"
						+ expense.getBankAccount().getLast4Digit() + ") ";
				expenseDto.setCashFlowDetails(new CashFlowDetailDto(expense.getBankAccount().getId(), bankAccountName));
			}
			listDto.add(expenseDto);
		}
		return new ListViewDto<ExpenseDto>(listDto, pages.getTotalElements(), pageRequest.getOffset(),
				pageRequest.getPageSize());
	}

	public void saveExpense(ExpenseDto expenseDto, Long userId) throws Exception {
		Expense expense = modelMapper.map(expenseDto, Expense.class);
		expense.setUser(userRepo.findById(userId).get());
		persistExpense(expenseDto, expense, userId);
	}

	public void updateExpense(ExpenseDto expenseDto, Long id, Long userId) throws Exception {
		Specification<Expense> specificationFilters = getAuditSpecificationFilters(filterMap, id, userId);
		List<Expense> expenseList = expenseRepo.findAll(specificationFilters);
		if (expenseList.isEmpty()) {
			throw new Exception("You are not authorised to modify");
		}
		Expense existingExpense = expenseList.get(0);
		modelMapper.map(expenseDto, existingExpense);
		existingExpense.setId(id);
		persistExpense(expenseDto, existingExpense, userId);
	}

	private void persistExpense(ExpenseDto expenseDto, Expense expense, Long userId) throws Exception {
		Long cashFlowId = expenseDto.getCashFlowDetails().getCashFlowId();
		if (cashFlowId != null) {
			if (expenseDto.getExpenseType().equals(CashFlowType.CREDITCARD)) {
				CreditCard creditCard = creditCardRepo.findById(cashFlowId).get();
				if (creditCard.getUser().getId() != userId) {
					throw new Exception("user is different from the credit card.");
				}
				expense.setCreditCard(creditCard);
			} else if (expenseDto.getExpenseType().equals(CashFlowType.BANK)) {
				BankAccount bankAccount = bankAccountRepo.findById(cashFlowId).get();
				if (bankAccount.getUser().getId() != userId) {
					throw new Exception("user is different from the Bank Account.");
				}
				expense.setBankAccount(bankAccount);
			}
		}
		expenseRepo.save(expense);
		if (expense.getExpenseType().equals(CashFlowType.CREDITCARD)) {
			budgetViewRepo.updateBudgetView(new Date(), userId);
		}
	}

	public String deleteExpense(Long id, Long userId) throws Exception {
		Specification<Expense> specificationFilters = getAuditSpecificationFilters(filterMap, id, userId);
		List<Expense> expenseList = expenseRepo.findAll(specificationFilters);
		if (expenseList.isEmpty()) {
			throw new Exception("You are not authorised to delete");
		}
		Expense existingExpense = expenseList.get(0);
		expenseRepo.deleteById(existingExpense.getId());
		return AppConstants.SUCCESS;
	}
}
