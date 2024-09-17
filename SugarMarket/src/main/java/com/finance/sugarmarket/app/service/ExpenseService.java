package com.finance.sugarmarket.app.service;

import java.lang.reflect.Type;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finance.sugarmarket.app.dto.ExpenseDto;
import com.finance.sugarmarket.app.model.Expense;
import com.finance.sugarmarket.app.repo.ExpenseRepo;

@Service
public class ExpenseService {
	
	@Autowired
	private ExpenseRepo expenseRepo;
	@Autowired
	private ModelMapper modelMapper;
	
	public List<ExpenseDto> findAllExpense(String userName){
		List<Expense> list = expenseRepo.findByUsername(userName);
		Type listType = new TypeToken<List<ExpenseDto>>() {}.getType();
		return modelMapper.map(list, listType);
	}
	
	public void saveExpense(Expense expense) {
		expenseRepo.save(expense);
	}
	
	public void deleteExpense(Integer id) {
		expenseRepo.deleteById(id);
	}
}
