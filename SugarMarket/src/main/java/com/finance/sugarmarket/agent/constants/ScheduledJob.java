package com.finance.sugarmarket.agent.constants;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public enum ScheduledJob {

    UPDATE_BUDGET_AGENT("UpdateBudgetAgent", "0 0 0 ? * * *", false),
    UPDATE_MUTUAL_FUND_AGENT("UpdateMutualFundAgent", "0 0 0-10/2 ? * TUE,WED,THU,FRI,SAT *", false);
//	ADD_INCOME_ENTRY_AGENT("AddIncomeEntryAgent", "0 0 1 28 * ? *", true),
//	LOAN_SCHEDULER_AGENT("LoanSchedulerAgent", "0 0 0 ? * * *", true);

    private final String jobName;
    private final String cronExpression;
    private final Boolean isScheduledRun;

    ScheduledJob(String jobName, String cronExpression, Boolean isScheduledRun) {
        this.jobName = jobName;
        this.cronExpression = cronExpression;
        this.isScheduledRun = isScheduledRun;
    }

    public String getJobName() {
        return jobName;
    }

    public String getCronExpression() {
        return cronExpression;
    }

    public Boolean getIsScheduledRun() {
        return isScheduledRun;
    }

    public static Map<String, String> getAllJobsAsMap() {
        return Arrays.stream(values())
                .collect(Collectors.toMap(
                        ScheduledJob::getJobName,
                        ScheduledJob::getCronExpression
                ));
    }

    public static ScheduledJob getByJobName(String jobName) {
        return Arrays.stream(values())
                .filter(scheduler -> scheduler.getJobName().equals(jobName))
                .findFirst()
                .orElse(null);
    }

    public static String getCronExpressionByJobName(String jobName) {
        ScheduledJob scheduler = getByJobName(jobName);
        return scheduler != null ? scheduler.getCronExpression() : null;
    }
}