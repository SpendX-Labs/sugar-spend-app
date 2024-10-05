package com.finance.sugarmarket.agent.constants;

import java.util.HashMap;
import java.util.Map;

public class Schedulers {

	private static Schedulers instance;

	Map<String, String> jobs;

	private Schedulers() {
		jobs = new HashMap<>();
		// add jobs here
		jobs.put("UpdateBudgetAgent", "0 0 0 ? * * *");
		jobs.put("UpdateMutualFundAgent", "0 0 0-10/2 ? * TUE,WED,THU,FRI,SAT *");
		jobs.put("AddIncomeEntry", "0 0 1 28 * ? *");
	}

	public static synchronized Schedulers getInstance() {
		if (instance == null) {
			instance = new Schedulers();
		}
		return instance;
	}

	public Map<String, String> getAllJobs() {
		return jobs;
	}

}
