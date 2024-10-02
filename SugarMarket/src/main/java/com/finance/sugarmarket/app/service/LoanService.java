package com.finance.sugarmarket.app.service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
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
import com.finance.sugarmarket.app.dto.LoanDto;
import com.finance.sugarmarket.app.dto.ModifyLoanDto;
import com.finance.sugarmarket.app.enums.LoanType;
import com.finance.sugarmarket.app.model.Loan;
import com.finance.sugarmarket.app.repo.CreditCardRepo;
import com.finance.sugarmarket.app.repo.LoanRepo;
import com.finance.sugarmarket.auth.repo.MFUserRepo;
import com.finance.sugarmarket.base.dto.Filter;
import com.finance.sugarmarket.base.dto.ListViewDto;
import com.finance.sugarmarket.base.service.SpecificationService;
import com.finance.sugarmarket.constants.FilterFieldConstant;

@Service
public class LoanService extends SpecificationService<Loan> {
	@Autowired
	private LoanRepo loanRepo;
	@Autowired
	private CreditCardRepo creditCardRepo;
	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	private MFUserRepo userRepo;
	private static final BigDecimal HUNDRED = new BigDecimal(100);
	private static final BigDecimal TWELVE = new BigDecimal(12);

	private static final Map<String, String> filterMap = new HashMap<String, String>();

	static {
		filterMap.put(FilterFieldConstant.USER_ID, "user.id");
	}

	public ListViewDto<LoanDto> findAllLoans(PageRequest pageRequest, List<Filter> filters) {
		Specification<Loan> specificationFilters = getSpecificationFilters(filters, filterMap);
		Page<Loan> pages = loanRepo.findAll(specificationFilters, pageRequest);
		List<LoanDto> listDto = new ArrayList<>();
		for (Loan loan : pages.getContent()) {
			LoanDto loanDto = modelMapper.map(loan, LoanDto.class);
			if (loan.getCreditCard() != null) {
				loanDto.setCreditCardName(
						loan.getCreditCard().getBankName() + " " + loan.getCreditCard().getCreditCardName());
				loanDto.setLast4Digit(loan.getCreditCard().getLast4Digit());
			}
			listDto.add(loanDto);
		}
		return new ListViewDto<LoanDto>(listDto, pages.getTotalElements(), pageRequest.getOffset(),
				pageRequest.getPageSize());

	}

	public void saveOrUpdateLoanDetails(LoanDto loandto, String username) throws Exception {
		if (loandto.getId() != null && loanRepo.findById(loandto.getId()).get().isUpdateLock()) {
			throw new Exception("Update for this emi is not possible");
		}
		Loan loan = modelMapper.map(loandto, Loan.class);
		loan.setUser(userRepo.findByUsername(username));
		if (loandto.getCreditCardId() != null && loandto.getCreditCardId() > 0) {
			loan.setCreditCard(creditCardRepo.findById(loandto.getCreditCardId()).get());
		} else {
			loan.setCreditCard(null);
		}
		if (loan.isNoCostEmi()) {
			if (loan.getPrincipalAmount() == null || loan.getInterestAmount() == null) {
				loan.setEmiAmount(
						loan.getTotalAmount().divide(new BigDecimal(loan.getTenure()), 2, RoundingMode.HALF_UP));
				loan.setPrincipalAmount(
						calculatePrincipal(loan.getEmiAmount(), loan.getInterestRate(), loan.getTenure()));
				loan.setInterestAmount(loan.getTotalAmount().subtract(loan.getPrincipalAmount()));
			}
		} else if (loan.getLoanType().equals(LoanType.REDUCING)) {
			loan.setPrincipalAmount(loan.getTotalAmount());
			loan.setEmiAmount(
					calculateReducingEMI(loan.getPrincipalAmount(), loan.getInterestRate(), loan.getTenure()));
			loan.setInterestAmount(
					calculateReducingTotalInterest(loan.getPrincipalAmount(), loan.getEmiAmount(), loan.getTenure()));
		} else if (loan.getLoanType().equals(LoanType.FLAT)) {
			loan.setPrincipalAmount(loan.getTotalAmount());
			loan.setInterestAmount(
					calculateFlatInterestAmount(loan.getPrincipalAmount(), loan.getInterestRate(), loan.getTenure()));
			loan.setEmiAmount(calculateFlatEMI(loan.getPrincipalAmount(), loan.getInterestAmount(), loan.getTenure()));
		}
		loan.setRemainingAmount(loan.getPrincipalAmount().add(loan.getInterestAmount()));
		loan.setRemainingPrincipalAmount(loan.getPrincipalAmount());
		loan.setRemainingInterestAmount(loan.getInterestAmount());
		loan.setRemainingTenure(loan.getTenure());
		loan.setUpdateLock(false);

		loanRepo.save(loan);
	}

	private BigDecimal calculatePrincipal(BigDecimal emi, BigDecimal annualRate, int tenureMonths) {
		// Convert annual interest rate to monthly interest rate
		BigDecimal monthlyRate = annualRate.divide(TWELVE.multiply(HUNDRED), MathContext.DECIMAL128);

		// Calculate (1 + monthlyRate) ^ tenureMonths
		BigDecimal one = BigDecimal.ONE;
		BigDecimal compoundFactor = one.add(monthlyRate).pow(tenureMonths, MathContext.DECIMAL128);

		// Calculate principal using the rearranged EMI formula
		BigDecimal numerator = emi.multiply(compoundFactor.subtract(one));
		BigDecimal denominator = monthlyRate.multiply(compoundFactor);
		BigDecimal principal = numerator.divide(denominator, MathContext.DECIMAL128).setScale(2, RoundingMode.HALF_UP);

		return principal;
	}

	private BigDecimal calculateReducingEMI(BigDecimal principal, BigDecimal rate, Integer tenure) {
		// Monthly rate calculation: rate / 12 / 100
		BigDecimal monthlyRate = rate.divide(TWELVE, 10, RoundingMode.HALF_UP).divide(HUNDRED, 10,
				RoundingMode.HALF_UP);

		// (1 + monthlyRate) ^ tenure
		BigDecimal onePlusRatePowerTenure = BigDecimal.ONE.add(monthlyRate).pow(tenure);

		// EMI formula: [P * r * (1 + r)^n] / [(1 + r)^n - 1]
		BigDecimal numerator = principal.multiply(monthlyRate).multiply(onePlusRatePowerTenure);
		BigDecimal denominator = onePlusRatePowerTenure.subtract(BigDecimal.ONE);

		return numerator.divide(denominator, 2, RoundingMode.HALF_UP); // Scale 2 for two decimal places
	}

	private BigDecimal calculateReducingTotalInterest(BigDecimal principal, BigDecimal emi, Integer tenure) {
		BigDecimal totalPayment = emi.multiply(new BigDecimal(tenure));
		return totalPayment.subtract(principal);
	}

	private BigDecimal calculateFlatInterestAmount(BigDecimal principal, BigDecimal rate, Integer tenure) {
		// rate / 100 to get decimal form of interest rate
		BigDecimal interestRateDecimal = rate.divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_UP);

		// (principal * rate * tenure) / 12
		return principal.multiply(interestRateDecimal).multiply(BigDecimal.valueOf(tenure))
				.divide(BigDecimal.valueOf(12), 2, RoundingMode.HALF_UP); // Rounding to 2 decimal places
	}

	private BigDecimal calculateFlatEMI(BigDecimal principal, BigDecimal totalInterest, Integer tenure) {
		// totalAmountPayable = principal + totalInterest
		BigDecimal totalAmountPayable = principal.add(totalInterest);

		// EMI = totalAmountPayable / tenure
		return totalAmountPayable.divide(BigDecimal.valueOf(tenure), 2, RoundingMode.HALF_UP); // Rounding to 2 decimal
																								// places
	}

	public void modifyLoanDetails(ModifyLoanDto modifyLoanDto) {
		Loan loan = loanRepo.findById(modifyLoanDto.getId()).get();
		loan.setUpdateLock(true);
		Integer paidMonths = modifyLoanDto.getAlreadyPaidMonth();
		if (paidMonths != null && paidMonths > 0) {
			if (loan.getLoanType().equals(LoanType.REDUCING)) {
				loan.setRemainingPrincipalAmount(calculateRemainingPrincipalReducing(loan.getPrincipalAmount(),
						loan.getInterestRate(), loan.getTenure(), paidMonths, loan.getEmiAmount()));
				loan.setRemainingInterestAmount(calculateRemainingInterestReducing(loan.getRemainingPrincipalAmount(),
						loan.getInterestRate(), loan.getTenure(), paidMonths, loan.getEmiAmount()));
			} else {
				loan.setRemainingPrincipalAmount(
						calculateRemainingPrincipalFlat(loan.getPrincipalAmount(), loan.getTenure(), paidMonths));
				loan.setRemainingInterestAmount(
						calculateRemainingInterestFlat(loan.getInterestAmount(), loan.getTenure(), paidMonths));
			}
			loan.setRemainingTenure(loan.getTenure() - paidMonths);
			loan.setRemainingAmount(loan.getRemainingInterestAmount().add(loan.getRemainingPrincipalAmount()));
		} else {
//			if(loan.getLoanType().equals(LoanType.REDUCING)) {
//				BigDecimal newPrincipal = loan.getRemainingAmount().subtract(modifyLoanDto.getPrincipalRepay());
//				BigDecimal monthlyInterestRate = annualInterestRate.divide(HUNDRED, RoundingMode.HALF_UP).divide(BigDecimal.valueOf(12), RoundingMode.HALF_UP);
//		        BigDecimal emi = calculateEMI(newPrincipal, monthlyInterestRate, newTenureMonths);
//			} else {
//				
//			}
		}
		loanRepo.save(loan);
	}

	private BigDecimal calculateRemainingPrincipalReducing(BigDecimal principal, BigDecimal rate, int totalTenure,
			int paidMonths, BigDecimal emi) {
		// Calculate the EMI using the correct method
		BigDecimal monthlyRate = rate.divide(BigDecimal.valueOf(12 * 100), 10, RoundingMode.HALF_UP);

		// If no months are paid, return the original principal
		if (paidMonths <= 0) {
			return principal;
		}

		// Calculate the remaining principal after paid months
		BigDecimal remainingPrincipal = principal;
		for (int month = 0; month < paidMonths; month++) {
			// Interest for this month
			BigDecimal interestForMonth = remainingPrincipal.multiply(monthlyRate);
			// Principal portion of EMI
			BigDecimal principalPortion = emi.subtract(interestForMonth);
			// Subtract the principal portion from remaining principal
			remainingPrincipal = remainingPrincipal.subtract(principalPortion);
		}

		return remainingPrincipal.setScale(2, RoundingMode.HALF_UP); // Rounded to 2 decimal places
	}

	private BigDecimal calculateRemainingInterestReducing(BigDecimal remaingPrincipal, BigDecimal rate, int tenure,
			int paidMonths, BigDecimal emi) {
		int remainingTenure = tenure - paidMonths;
		return emi.multiply(BigDecimal.valueOf(remainingTenure)).subtract(remaingPrincipal);
	}

	private BigDecimal calculateRemainingPrincipalFlat(BigDecimal principal, int totalTenure, int paidMonths) {
		BigDecimal principalRepaid = principal.divide(BigDecimal.valueOf(totalTenure), 10, RoundingMode.HALF_UP)
				.multiply(BigDecimal.valueOf(paidMonths));
		return principal.subtract(principalRepaid);
	}

	private BigDecimal calculateRemainingInterestFlat(BigDecimal totalInterest, int totalTenure, int paidMonths) {
		BigDecimal remainingTenure = BigDecimal.valueOf(totalTenure - paidMonths);
		return totalInterest.multiply(remainingTenure).divide(BigDecimal.valueOf(totalTenure), 2, RoundingMode.HALF_UP);
	}

}
