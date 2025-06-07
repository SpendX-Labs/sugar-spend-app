package com.finance.sugarmarket.app.service;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.finance.sugarmarket.app.repo.TransactionRepo;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.finance.sugarmarket.app.dto.CreditCardDto;
import com.finance.sugarmarket.app.model.CreditCard;
import com.finance.sugarmarket.app.repo.CreditCardRepo;
import com.finance.sugarmarket.auth.repo.MFUserRepo;
import com.finance.sugarmarket.base.dto.Filter;
import com.finance.sugarmarket.base.dto.ListViewDto;
import com.finance.sugarmarket.base.service.SpecificationService;
import com.finance.sugarmarket.constants.AppConstants;
import com.finance.sugarmarket.constants.FieldConstant;
import com.finance.sugarmarket.constants.RedisConstants;

@Service
public class CreditCardService extends SpecificationService<CreditCard> {

	@Autowired
	private CreditCardRepo creditCardRepo;
	@Autowired
	private TransactionRepo transactionRepo;
	@Autowired
	private MFUserRepo userRepo;
	@Autowired
	private ModelMapper modelMapper;

	private static final String CAN_NOT_DELETE = "Can not delete";

	private static final Map<String, String> filterMap = new HashMap<String, String>();

	static {
		filterMap.put(FieldConstant.USER_ID, "user.id");
	}
	
	@Cacheable(value = RedisConstants.CREDIT_CARDS, key = "#userId")
	public List<CreditCardDto> findAllCreditCardsByUserId(Long userId) {
		List<CreditCard> cards = creditCardRepo.findByUserId(userId);
		Type listType = new TypeToken<List<CreditCardDto>>() {
		}.getType();
		return modelMapper.map(cards, listType);
	}
	
	@CacheEvict(value = RedisConstants.CREDIT_CARDS, key = "#userId")
	public void evictCreditCards(Long userId) {
	    // This method will clear all entries in the user specific credit Cards
	}

	public ListViewDto<CreditCardDto> findAllCreditCard(PageRequest pageRequest, List<Filter> filters) throws Exception {
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
		evictCreditCards(userId);
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
		evictCreditCards(userId);
	}

	public String deleteCreditCard(Long id, Long userId) throws Exception {
		Specification<CreditCard> specificationFilters = getAuditSpecificationFilters(filterMap, id, userId);
		List<CreditCard> creditCardList = creditCardRepo.findAll(specificationFilters);
		if (creditCardList.isEmpty()) {
			throw new Exception("You are not authorised to modify");
		}
		CreditCard existingCard = creditCardList.get(0);
		if (!transactionRepo.findAllByCreditCardId(id).isEmpty()) {
			return CAN_NOT_DELETE;
		}
		creditCardRepo.deleteById(existingCard.getId());
		evictCreditCards(userId);
		return AppConstants.SUCCESS;
	}
}
