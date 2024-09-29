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

import com.finance.sugarmarket.app.dto.CashFlowDetailDto;
import com.finance.sugarmarket.app.dto.IncomeDto;
import com.finance.sugarmarket.app.enums.CashFlowType;
import com.finance.sugarmarket.app.model.BankAccount;
import com.finance.sugarmarket.app.model.CreditCard;
import com.finance.sugarmarket.app.model.Income;
import com.finance.sugarmarket.app.repo.BankAccountRepo;
import com.finance.sugarmarket.app.repo.CreditCardRepo;
import com.finance.sugarmarket.app.repo.IncomeRepo;
import com.finance.sugarmarket.auth.repo.MFUserRepo;
import com.finance.sugarmarket.base.dto.Filter;
import com.finance.sugarmarket.base.dto.ListViewDto;
import com.finance.sugarmarket.base.service.SpecificationService;
import com.finance.sugarmarket.constants.AppConstants;
import com.finance.sugarmarket.constants.FilterFieldConstant;

@Service
public class IncomeService extends SpecificationService<Income> {
	@Autowired
	private IncomeRepo incomeRepo;
	@Autowired
	private CreditCardRepo creditCardRepo;
	@Autowired
	private BankAccountRepo bankAccountRepo;
	@Autowired
	private MFUserRepo userRepo;
	@Autowired
	private ModelMapper modelMapper;

	private static final Map<String, String> filterMap = new HashMap<String, String>();

	static {
		filterMap.put(FilterFieldConstant.USER_ID, "user.id");
		filterMap.put(FilterFieldConstant.CREDIT_CARD_ID, "creditCard.id");
	}

	public ListViewDto<IncomeDto> findAllIncome(PageRequest pageRequest, List<Filter> filters) {
		Specification<Income> specificationFilters = getSpecificationFilters(filters, filterMap);
		Page<Income> pages = incomeRepo.findAll(specificationFilters, pageRequest);
		List<IncomeDto> listDto = new ArrayList<>();
		for (Income income : pages.getContent()) {
			IncomeDto incomeDto = modelMapper.map(income, IncomeDto.class);
			if (income.getCreditCard() != null) {
				String crediCardName = income.getCreditCard().getBankName() + " "
						+ income.getCreditCard().getCreditCardName() + " (XXXX" + income.getCreditCard().getLast4Digit()
						+ ") ";
				incomeDto.setCashFlowDetails(new CashFlowDetailDto(income.getCreditCard().getId(), crediCardName));
			} else if (income.getBankAccount() != null) {
				String bankAccountName = income.getBankAccount().getBankName() + " (XXXX"
						+ income.getBankAccount().getLast4Digit() + ") ";
				incomeDto.setCashFlowDetails(new CashFlowDetailDto(income.getBankAccount().getId(), bankAccountName));
			}
			listDto.add(incomeDto);
		}
		return new ListViewDto<IncomeDto>(listDto, pages.getTotalElements(), pageRequest.getOffset(),
				pageRequest.getPageSize());
	}

	public void saveIncome(IncomeDto incomeDto, Long userId) throws Exception {
		Income income = modelMapper.map(incomeDto, Income.class);
		persistIncome(incomeDto, income, userId);
	}

	public void updateIncome(IncomeDto incomeDto, Long id, Long userId) throws Exception {
		Specification<Income> specificationFilters = getAuditSpecificationFilters(filterMap, id, userId);
		List<Income> incomeList = incomeRepo.findAll(specificationFilters);
		if (incomeList.isEmpty()) {
			throw new Exception("You are not authorised to modify");
		}
		Income existingIncome = incomeList.get(0);
		modelMapper.map(incomeDto, existingIncome);
		existingIncome.setId(id);
		persistIncome(incomeDto, existingIncome, userId);
	}

	private void persistIncome(IncomeDto incomeDto, Income income, Long userId) throws Exception {
		Long cashFlowId = incomeDto.getCashFlowDetails().getCashFlowId();
		if (cashFlowId != null) {
			if (incomeDto.getIncomeType().equals(CashFlowType.CREDITCARD)) {
				CreditCard creditCard = creditCardRepo.findById(cashFlowId).get();
				if (creditCard.getUser().getId() != userId) {
					throw new Exception("user is different from the credit card.");
				}
				income.setCreditCard(creditCard);
			} else if (incomeDto.getIncomeType().equals(CashFlowType.BANK)) {
				BankAccount bankAccount = bankAccountRepo.findById(cashFlowId).get();
				if (bankAccount.getUser().getId() != userId) {
					throw new Exception("user is different from the Bank Account.");
				}
				income.setBankAccount(bankAccount);
			}
		}
		income.setUser(userRepo.findById(userId).get());
		incomeRepo.save(income);
	}

	public String deleteIncome(Long id, Long userId) throws Exception {
		Specification<Income> specificationFilters = getAuditSpecificationFilters(filterMap, id, userId);
		List<Income> incomeList = incomeRepo.findAll(specificationFilters);
		if (incomeList.isEmpty()) {
			throw new Exception("You are not authorised to modify");
		}
		Income existingIncome = incomeList.get(0);
		incomeRepo.deleteById(existingIncome.getId());
		return AppConstants.SUCCESS;
	}
}
