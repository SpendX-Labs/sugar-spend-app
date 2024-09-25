package com.finance.sugarmarket.agent.jobs;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

@Component
public class AddIncomeEntry implements Job {

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		

	}

}
