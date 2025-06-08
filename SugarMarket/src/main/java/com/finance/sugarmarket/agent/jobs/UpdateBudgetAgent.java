package com.finance.sugarmarket.agent.jobs;

import com.finance.sugarmarket.app.service.BudgetViewerService;
import com.finance.sugarmarket.constants.AppConstants;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class UpdateBudgetAgent implements Job {

    @Autowired
    private BudgetViewerService budgetViewerService;

    private static final Logger log = LoggerFactory.getLogger(UpdateBudgetAgent.class);

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        log.info("started UpdateBudgetAgent class..");
        Date date = (Date) context.getJobDetail().getJobDataMap().get(AppConstants.DATE);
        Long creditCardId = context.getJobDetail().getJobDataMap().getLong(AppConstants.CREDIT_CARD_ID);
        budgetViewerService.updateBudgetView(date, creditCardId);
    }
}
