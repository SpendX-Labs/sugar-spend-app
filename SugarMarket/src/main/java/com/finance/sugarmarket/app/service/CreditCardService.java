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

import com.finance.sugarmarket.app.dto.CreditCardDto;
import com.finance.sugarmarket.app.model.CreditCard;
import com.finance.sugarmarket.app.repo.CreditCardRepo;
import com.finance.sugarmarket.app.repo.ExpenseRepo;
import com.finance.sugarmarket.auth.repo.MFUserRepo;
import com.finance.sugarmarket.base.dto.Filter;
import com.finance.sugarmarket.base.dto.ListViewDto;
import com.finance.sugarmarket.base.service.SpecificationService;
import com.finance.sugarmarket.constants.AppConstants;
import com.finance.sugarmarket.constants.FieldConstant;

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

	private static final String DELETE_MSG = "Can not delete";

	private static final Map<String, String> filterMap = new HashMap<String, String>();

	static {
		filterMap.put(FieldConstant.USER_ID, "user.id");
	}

	public ListViewDto<CreditCardDto> findAllCreditCard(PageRequest pageRequest, List<Filter> filters) {
		Specification<CreditCard> specificationFilters = getSpecificationFilters(filters, filterMap);
		Page<CreditCard> pages = creditCardRepo.findAll(specificationFilters, pageRequest);

		Type listType = new TypeToken<List<CreditCardDto>>() {
		}.getType();
		return new ListViewDto<CreditCardDto>(modelMapper.map(pages.getContent(), listType), pages.getTotalElements(),
				pageRequest.getOffset(), pageRequest.getPageSize());
	}

	public void saveCreditCard(CreditCardDto cardDeatilDto, Long userId) {
		CreditCard creditCard = modelMapper.map(cardDeatilDto, CreditCard.class);
		creditCard.setUser(userRepo.findById(userId).get());
		creditCardRepo.save(creditCard);
	}

	public void updateCreditCard(CreditCardDto cardDetailDto, Long id, Long userId) throws Exception {
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

	public String deleteCreditCard(Long id, Long userId) throws Exception {
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
