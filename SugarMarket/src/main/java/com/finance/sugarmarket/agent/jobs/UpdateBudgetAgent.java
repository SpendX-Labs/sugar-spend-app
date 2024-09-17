package com.finance.sugarmarket.agent.jobs;

import java.util.Date;
import java.util.List;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.finance.sugarmarket.app.service.BudgetViewerService;
import com.finance.sugarmarket.auth.model.MFUser;
import com.finance.sugarmarket.auth.service.MFUserService;

@Component
public class UpdateBudgetAgent implements Job {
	
	@Autowired
	private BudgetViewerService budgetViewerService;
	@Autowired
	private MFUserService mfUserService;
	
	private static final Logger log = LoggerFactory.getLogger(UpdateBudgetAgent.class);

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		log.info("started UpdateBudgetAgent class..");
		List<MFUser> listOfUsers = mfUserService.findAllUsers();
		Date date = (Date) context.getJobDetail().getJobDataMap().get("date");
		for(MFUser user : listOfUsers) {
			budgetViewerService.updateBudgetView(date, user.getId());
		}
		
	}

}
