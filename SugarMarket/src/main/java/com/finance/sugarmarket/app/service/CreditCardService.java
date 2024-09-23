package com.finance.sugarmarket.app.service;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.finance.sugarmarket.app.dto.CreditCardDto;
import com.finance.sugarmarket.app.model.CreditCard;
import com.finance.sugarmarket.app.repo.CreditCardRepo;
import com.finance.sugarmarket.app.repo.ExpenseRepo;
import com.finance.sugarmarket.auth.repo.MFUserRepo;
import com.finance.sugarmarket.base.dto.Filter;
import com.finance.sugarmarket.base.service.SpecificationService;
import com.finance.sugarmarket.constants.AppConstants;
import com.finance.sugarmarket.constants.FilterFieldConstant;

@Service
public class CreditCardService extends SpecificationService<CreditCard> {

	@Autowired
	private CreditCardRepo creditCardRepo;
	@Autowired
	private ExpenseRepo expenseRepo;
	@Autowired
	private MFUserRepo userRepo;
	@Autowired
	private ModelMapper modelMapper;

	private static final String DELETE_MSG = "Can not delete as it reffered in child table";

	private static final Map<String, String> filterMap = new HashMap<String, String>();

	static {
		filterMap.put(FilterFieldConstant.USER_ID, "user.id");
	}

	public List<CreditCardDto> findAllCreditCard(PageRequest pageRequest, List<Filter> filters) {
		Specification<CreditCard> specificationFilters = getSpecificationFilters(filters, filterMap);
		List<CreditCard> list = creditCardRepo.findAll(specificationFilters, pageRequest).getContent();
		Type listType = new TypeToken<List<CreditCardDto>>() {
		}.getType();
		return modelMapper.map(list, listType);
	}

	public void saveCreditCard(CreditCardDto cardDeatilDto, Integer userId) {
		CreditCard creditCard = modelMapper.map(cardDeatilDto, CreditCard.class);
		creditCard.setUser(userRepo.findById(userId).get());
		creditCardRepo.save(creditCard);
	}

	public void updateCreditCard(CreditCardDto cardDetailDto, Integer id, Integer userId) throws Exception {
		Specification<CreditCard> specificationFilters = getAuditSpecificationFilters(filterMap, id, userId);
		List<CreditCard> creditCardList = creditCardRepo.findAll(specificationFilters);
		if (creditCardList.isEmpty()) {
			throw new Exception("You are not authorised to modify");
		}
		CreditCard existingCard = creditCardList.get(0);
		modelMapper.map(cardDetailDto, existingCard);
		existingCard.setId(id);
		creditCardRepo.save(existingCard);
	}

	public String deleteCreditCard(Integer id, Integer userId) throws Exception {
		Specification<CreditCard> specificationFilters = getAuditSpecificationFilters(filterMap, id, userId);
		List<CreditCard> creditCardList = creditCardRepo.findAll(specificationFilters);
		if (creditCardList.isEmpty()) {
			throw new Exception("You are not authorised to modify");
		}
		CreditCard existingCard = creditCardList.get(0);
		if (expenseRepo.findByCreditCardId(id).size() > 0) {
			return DELETE_MSG;
		}
		creditCardRepo.deleteById(existingCard.getId());
		return AppConstants.SUCCESS;
	}
}
