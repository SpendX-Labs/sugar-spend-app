package com.finance.sugarmarket.app.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finance.sugarmarket.app.dto.MarketData;
import com.finance.sugarmarket.app.model.MutualFund;
import com.finance.sugarmarket.app.repo.MutualFundRepo;
import com.finance.sugarmarket.app.utils.MarketDataUtil;
import com.finance.sugarmarket.constants.MFConstants;

@Service
public class MutualFundService {

	private static final Logger log = LoggerFactory.getLogger(MutualFundService.class);

	private static final SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

	@Autowired
	private MutualFundRepo mutualfundRepo;

	public List<MutualFund> findAllMutualFunds() {
		return mutualfundRepo.findAll();
	}

	public void saveMutualFund(MutualFund fund) {
		mutualfundRepo.save(fund);
	}

	public void updateCurrentNav() {
		List<MutualFund> funds = mutualfundRepo.findAll();

		funds.forEach(fund -> {
			log.info("processing Fund: " + fund.getSchemeName());
			try {
				MarketData marketData = MarketDataUtil.getHistoricalData(fund.getSchemeCode());
				long updatedDateMillis = System.currentTimeMillis();
				updatedDateMillis = sdf.parse(sdf.format(new Date(updatedDateMillis))).getTime();
				while (marketData.getNavData().get(new Date(updatedDateMillis)) == null) {
					updatedDateMillis = updatedDateMillis - MFConstants.ONE_DAY_IN_MILLIS;
				}
				Date updatedDate = new Date(updatedDateMillis);
				if (marketData.getNavData().get(new Date(updatedDateMillis)) == null) {
					log.info(sdf.format(updatedDate) + " is not available for " + fund.getSchemeName());
				}
				fund.setCurrentNav(marketData.getNavData().get(new Date(updatedDateMillis)));

				// set historical data
				// 1 day
				updatedDateMillis = updatedDate.getTime() - MFConstants.ONE_DAY_IN_MILLIS;
				if (marketData.getNavData().firstKey().before(new Date(updatedDateMillis))) {
					while (marketData.getNavData().get(new Date(updatedDateMillis)) == null) {
						updatedDateMillis = updatedDateMillis - MFConstants.ONE_DAY_IN_MILLIS;
					}
					fund.setDay1Nav(marketData.getNavData().get(new Date(updatedDateMillis)));
				}

				// 1 W
				updatedDateMillis = updatedDate.getTime() - MFConstants.ONE_DAY_IN_MILLIS * 7;
				if (marketData.getNavData().firstKey().before(new Date(updatedDateMillis))) {
					while (marketData.getNavData().get(new Date(updatedDateMillis)) == null) {
						updatedDateMillis = updatedDateMillis - MFConstants.ONE_DAY_IN_MILLIS;
					}
					fund.setWeek1Nav(marketData.getNavData().get(new Date(updatedDateMillis)));
				}

				// 1 M
				updatedDateMillis = updatedDate.getTime() - MFConstants.ONE_DAY_IN_MILLIS * 30;
				if (marketData.getNavData().firstKey().before(new Date(updatedDateMillis))) {
					while (marketData.getNavData().get(new Date(updatedDateMillis)) == null) {
						updatedDateMillis = updatedDateMillis - MFConstants.ONE_DAY_IN_MILLIS;
					}
					fund.setMonth1Nav(marketData.getNavData().get(new Date(updatedDateMillis)));
				}

				// 6 M
				updatedDateMillis = updatedDate.getTime() - MFConstants.ONE_DAY_IN_MILLIS * 30 * 6;
				if (marketData.getNavData().firstKey().before(new Date(updatedDateMillis))) {
					while (marketData.getNavData().get(new Date(updatedDateMillis)) == null) {
						updatedDateMillis = updatedDateMillis - MFConstants.ONE_DAY_IN_MILLIS;
					}
					fund.setMonth6Nav(marketData.getNavData().get(new Date(updatedDateMillis)));
				}

				// 1 Y
				updatedDateMillis = updatedDate.getTime() - MFConstants.ONE_DAY_IN_MILLIS * 30 * 12;
				if (marketData.getNavData().firstKey().before(new Date(updatedDateMillis))) {
					while (marketData.getNavData().get(new Date(updatedDateMillis)) == null) {
						updatedDateMillis = updatedDateMillis - MFConstants.ONE_DAY_IN_MILLIS;
					}
					fund.setYear1Nav(marketData.getNavData().get(new Date(updatedDateMillis)));
				}

				// 3 Y
				updatedDateMillis = updatedDate.getTime() - MFConstants.ONE_DAY_IN_MILLIS * 30 * 12 * 3;
				if (marketData.getNavData().firstKey().before(new Date(updatedDateMillis))) {
					while (marketData.getNavData().get(new Date(updatedDateMillis)) == null) {
						updatedDateMillis = updatedDateMillis - MFConstants.ONE_DAY_IN_MILLIS;
					}
					fund.setYear3Nav(marketData.getNavData().get(new Date(updatedDateMillis)));
				}

				// 5 Y
				updatedDateMillis = updatedDate.getTime() - MFConstants.ONE_DAY_IN_MILLIS * 30 * 12 * 5;
				if (marketData.getNavData().firstKey().before(new Date(updatedDateMillis))) {
					while (marketData.getNavData().get(new Date(updatedDateMillis)) == null) {
						updatedDateMillis = updatedDateMillis - MFConstants.ONE_DAY_IN_MILLIS;
					}
					fund.setYear5Nav(marketData.getNavData().get(new Date(updatedDateMillis)));
				}

				// All
				fund.setFirstNav(marketData.getNavData().firstEntry().getValue());
			
				
				fund.setLastNavDate(updatedDate);
				saveMutualFund(fund);

			} catch (Exception e) {
				log.info("Error while processing Fund: " + fund.getSchemeName(), e);
			}

		});
	}

}
