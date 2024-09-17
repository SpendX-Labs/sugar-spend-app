package com.finance.sugarmarket.app.service;

import java.lang.reflect.Type;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finance.sugarmarket.app.dto.CreditCardDto;
import com.finance.sugarmarket.app.model.CreditCard;
import com.finance.sugarmarket.app.repo.CreditCardRepo;
import com.finance.sugarmarket.app.repo.ExpenseRepo;
import com.finance.sugarmarket.constants.MFConstants;

@Service
public class CreditCardService {
	
	@Autowired
	private CreditCardRepo creditCardRepo;
	@Autowired
	private ExpenseRepo expenseRepo;
	@Autowired
	private ModelMapper modelMapper;
	
	private static final String DELETE_MSG = "Can not delete as it reffered in child table";
	
	public List<CreditCardDto> findAllCreditCard(String userName) {
		List<CreditCard> list = creditCardRepo.findByUsername(userName);
		Type listType = new TypeToken<List<CreditCardDto>>() {}.getType();
		return modelMapper.map(list, listType);
	}
	
	public void saveCreditCard(CreditCard cardDeatil) {
		creditCardRepo.save(cardDeatil);
	}
	
	public CreditCard findByCreditCardId(Integer id) {
		return creditCardRepo.findById(id).get();
	}
	
	public String deleteCreditCard(Integer id) {
		if(expenseRepo.findByCreditCardId(id).size() > 0) {
			return DELETE_MSG;
		}
		creditCardRepo.deleteById(id);
		return MFConstants.SUCCESS;
	}

}
