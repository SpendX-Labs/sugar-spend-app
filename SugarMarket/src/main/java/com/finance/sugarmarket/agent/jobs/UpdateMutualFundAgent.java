package com.finance.sugarmarket.agent.jobs;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.finance.sugarmarket.app.service.MutualFundService;

@Component
public class UpdateMutualFundAgent implements Job {
	
	@Autowired
	private MutualFundService mutualFundservice;

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		mutualFundservice.updateCurrentNav();
		
	}

}
