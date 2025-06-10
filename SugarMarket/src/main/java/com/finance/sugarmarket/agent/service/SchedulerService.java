package com.finance.sugarmarket.agent.service;

import com.finance.sugarmarket.agent.constants.ScheduledJob;
import com.finance.sugarmarket.constants.AppConstants;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class SchedulerService {
    private static final Logger log = LoggerFactory.getLogger(SchedulerService.class);

    @Autowired
    private Scheduler scheduler;

    @PostConstruct
    public void init() {
        try {
            scheduler.start();
            log.info("Scheduler started successfully");

            for (ScheduledJob schedulerConfig : ScheduledJob.values()) {
                if (!schedulerConfig.getIsScheduledRun()) {
                    log.debug("Skipping job {} - not scheduled to run", schedulerConfig.getJobName());
                    continue;
                }

                try {
                    String className = AppConstants.SCHEDULER_AGENTS_PACKAGE + AppConstants.DOT + schedulerConfig.getJobName();
                    Class<?> clazz = Class.forName(className);
                    JobDetail jobDetails = buildJobDetail(clazz);
                    Trigger trigger = buildTrigger(clazz, schedulerConfig.getCronExpression());
                    scheduler.scheduleJob(jobDetails, trigger);
                    log.info("Successfully scheduled job: {} with cron: {}", schedulerConfig.getJobName(), schedulerConfig.getCronExpression());
                } catch (ClassNotFoundException e) {
                    log.error("Job class not found for: {}", schedulerConfig.getJobName(), e);
                } catch (Exception e) {
                    log.error("Error while scheduling job: {}", schedulerConfig.getJobName(), e);
                }
            }
        } catch (Exception e) {
            log.error("Error while starting scheduler: ", e);
        }
    }

    /**
     * Triggers a job with optional data
     */
    public void triggerAgentByClass(ScheduledJob scheduledJob, Map<String, Object> data) throws Exception {
        log.info("Triggering job: {}", scheduledJob.getJobName());

        String className = AppConstants.SCHEDULER_AGENTS_PACKAGE + AppConstants.DOT + scheduledJob.getJobName();
        Class<?> clazz = Class.forName(className);
        JobKey jobKey = new JobKey(clazz.getSimpleName());

        // Create JobDataMap from the provided data
        JobDataMap jobDataMap = new JobDataMap();
        if (data != null && !data.isEmpty()) {
            jobDataMap.putAll(data);
        }
        // Always add timestamp for tracking
        jobDataMap.put("triggerTime", new Date());

        if (scheduler.checkExists(jobKey)) {
            // Trigger existing job with data
            scheduler.triggerJob(jobKey, jobDataMap);
            log.info("Triggered existing job: {} with data keys: {}", scheduledJob.getJobName(),
                    data != null ? data.keySet() : "none");
        } else {
            // Create new job with data and trigger it immediately
            JobDetail jobDetails = buildJobDetailWithData(clazz, jobDataMap);
            Trigger trigger = buildImmediateTrigger(clazz);
            scheduler.scheduleJob(jobDetails, trigger);
            log.info("Scheduled and triggered new job: {} with data keys: {}", scheduledJob.getJobName(),
                    data != null ? data.keySet() : "none");
        }
    }

    /**
     * Convenience method for single key-value pair
     */
    public void triggerAgentByClass(ScheduledJob scheduledJob, String key, Object value) throws Exception {
        Map<String, Object> data = new HashMap<>();
        data.put(key, value);
        triggerAgentByClass(scheduledJob, data);
    }

    /**
     * Convenience method for multiple key-value pairs
     */
    public void triggerAgentByClass(ScheduledJob scheduledJob, Object... keyValuePairs) throws Exception {
        if (keyValuePairs.length % 2 != 0) {
            throw new IllegalArgumentException("Key-value pairs must be even number of arguments");
        }

        Map<String, Object> data = new HashMap<>();
        for (int i = 0; i < keyValuePairs.length; i += 2) {
            String key = (String) keyValuePairs[i];
            Object value = keyValuePairs[i + 1];
            data.put(key, value);
        }

        triggerAgentByClass(scheduledJob, data);
    }

    /**
     * Check if a job exists in the scheduler
     */
    public boolean jobExists(ScheduledJob scheduledJob) throws Exception {
        String className = AppConstants.SCHEDULER_AGENTS_PACKAGE + AppConstants.DOT + scheduledJob.getJobName();
        Class<?> clazz = Class.forName(className);
        JobKey jobKey = new JobKey(clazz.getSimpleName());
        return scheduler.checkExists(jobKey);
    }

    /**
     * Pause a specific job
     */
    public void pauseJob(ScheduledJob scheduledJob) throws Exception {
        String className = AppConstants.SCHEDULER_AGENTS_PACKAGE + AppConstants.DOT + scheduledJob.getJobName();
        Class<?> clazz = Class.forName(className);
        JobKey jobKey = new JobKey(clazz.getSimpleName());
        scheduler.pauseJob(jobKey);
        log.info("Paused job: {}", scheduledJob.getJobName());
    }

    /**
     * Resume a specific job
     */
    public void resumeJob(ScheduledJob scheduledJob) throws Exception {
        String className = AppConstants.SCHEDULER_AGENTS_PACKAGE + AppConstants.DOT + scheduledJob.getJobName();
        Class<?> clazz = Class.forName(className);
        JobKey jobKey = new JobKey(clazz.getSimpleName());
        scheduler.resumeJob(jobKey);
        log.info("Resumed job: {}", scheduledJob.getJobName());
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private JobDetail buildJobDetail(Class jobClass) {
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("scheduledTime", new Date());
        jobDataMap.put("jobType", "scheduled");

        return JobBuilder.newJob(jobClass)
                .withIdentity(jobClass.getSimpleName())
                .setJobData(jobDataMap)
                .build();
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private JobDetail buildJobDetailWithData(Class jobClass, JobDataMap jobDataMap) {
        // Add default data if not present
        if (!jobDataMap.containsKey("jobType")) {
            jobDataMap.put("jobType", "triggered");
        }

        return JobBuilder.newJob(jobClass)
                .withIdentity(jobClass.getSimpleName())
                .usingJobData(jobDataMap)
                .build();
    }

    @SuppressWarnings("rawtypes")
    private Trigger buildTrigger(Class jobClass, String cronExpression) {
        return TriggerBuilder.newTrigger()
                .withIdentity(jobClass.getSimpleName() + "_trigger")
                .withSchedule(CronScheduleBuilder.cronSchedule(cronExpression))
                .startAt(new Date(System.currentTimeMillis()))
                .build();
    }

    @SuppressWarnings("rawtypes")
    private Trigger buildImmediateTrigger(Class jobClass) {
        return TriggerBuilder.newTrigger()
                .withIdentity(jobClass.getSimpleName() + "_immediate_trigger")
                .startNow()
                .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                        .withRepeatCount(0) // Execute only once
                        .withMisfireHandlingInstructionFireNow())
                .build();
    }

    @PreDestroy
    public void preDestroy() {
        try {
            if (scheduler != null && !scheduler.isShutdown()) {
                scheduler.shutdown(true); // Wait for running jobs to complete
                log.info("Scheduler shutdown successfully");
            }
        } catch (Exception e) {
            log.error("Error while stopping scheduler: ", e);
        }
    }
}