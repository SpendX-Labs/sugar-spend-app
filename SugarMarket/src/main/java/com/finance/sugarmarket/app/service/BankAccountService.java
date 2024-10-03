package com.finance.sugarmarket.app.service;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.finance.sugarmarket.app.dto.BankAccountDto;
import com.finance.sugarmarket.app.model.BankAccount;
import com.finance.sugarmarket.app.repo.BankAccountRepo;
import com.finance.sugarmarket.app.repo.ExpenseRepo;
import com.finance.sugarmarket.auth.repo.MFUserRepo;
import com.finance.sugarmarket.base.dto.Filter;
import com.finance.sugarmarket.base.dto.ListViewDto;
import com.finance.sugarmarket.base.service.SpecificationService;
import com.finance.sugarmarket.constants.AppConstants;
import com.finance.sugarmarket.constants.FieldConstant;

@Service
public class BankAccountService extends SpecificationService<BankAccount> {

	@Autowired
	private BankAccountRepo bankAccountRepo;
	@Autowired
	private ExpenseRepo expenseRepo;
	@Autowired
	private MFUserRepo userRepo;
	@Autowired
	private ModelMapper modelMapper;

	private static final String DELETE_MSG = "Can not delete";

	private static final Map<String, String> filterMap = new HashMap<String, String>();

	static {
		filterMap.put(FieldConstant.USER_ID, "user.id");
	}

	public ListViewDto<BankAccountDto> findAllBankAccount(PageRequest pageRequest, List<Filter> filters) throws Exception {
		Specification<BankAccount> specificationFilters = getSpecificationFilters(filters, filterMap);
		Page<BankAccount> pages = bankAccountRepo.findAll(specificationFilters, pageRequest);

		Type listType = new TypeToken<List<BankAccountDto>>() {
		}.getType();
		return new ListViewDto<BankAccountDto>(modelMapper.map(pages.getContent(), listType), pages.getTotalElements(),
				pageRequest.getOffset(), pageRequest.getPageSize());
	}

	public void saveBankAccount(BankAccountDto bankAccountDto, Long userId) {
		BankAccount bankAccount = modelMapper.map(bankAccountDto, BankAccount.class);
		bankAccount.setUser(userRepo.findById(userId).get());
		bankAccountRepo.save(bankAccount);
	}

	public void updateBankAccount(BankAccountDto bankAccountDto, Long id, Long userId) throws Exception {
		Specification<BankAccount> specificationFilters = getAuditSpecificationFilters(filterMap, id, userId);
		List<BankAccount> bankAccountList = bankAccountRepo.findAll(specificationFilters);
		if (bankAccountList.isEmpty()) {
			throw new Exception("You are not authorised to modify");
		}
		BankAccount existingBank = bankAccountList.get(0);
		modelMapper.map(bankAccountDto, existingBank);
		existingBank.setId(id);
		bankAccountRepo.save(existingBank);
	}

	public String deleteBankAccount(Long id, Long userId) throws Exception {
		Specification<BankAccount> specificationFilters = getAuditSpecificationFilters(filterMap, id, userId);
		List<BankAccount> bankAccountList = bankAccountRepo.findAll(specificationFilters);
		if (bankAccountList.isEmpty()) {
			throw new Exception("You are not authorised to modify");
		}
		BankAccount existingBank = bankAccountList.get(0);
		if (expenseRepo.findByBankAccountId(id).size() > 0) {
			return DELETE_MSG;
		}
		bankAccountRepo.deleteById(existingBank.getId());
		return AppConstants.SUCCESS;
	}

}
