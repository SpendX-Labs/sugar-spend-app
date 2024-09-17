package com.finance.sugarmarket.agent.service;

import java.util.Date;

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

import com.finance.sugarmarket.agent.constants.Schedulers;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

@Service
public class SchedulerService {
	private static final Logger log = LoggerFactory.getLogger(SchedulerService.class);

	@Autowired
	private Scheduler scheduler;

	@PostConstruct
	public void init() {
		try {
			scheduler.start();
			Schedulers.getInstance().getAllJobs().forEach((jobName, cron)->{
				try {
					String className = "com.finance.sugarmarket.agent.jobs." + jobName;
					Class<?> clazz = Class.forName(className);
					JobDetail jobDetails = buildJobDetail(clazz);
					Trigger trigger = buildTrigger(clazz, cron);
					scheduler.scheduleJob(jobDetails, trigger);
				}
				catch (Exception e) {
					log.error("error while scheduling job " + jobName, e);
				}
			});
		} catch (Exception e) {
			log.error("error while starting scheduler: ", e);
		}
	}

	public void triggerAgentByClass(String jobName) throws Exception {
		log.info("Triggering " + jobName);
		String className = "com.finance.sugarmarket.agent.jobs." + jobName;
		Class<?> clazz = Class.forName(className);
		JobKey jobKey = new JobKey(clazz.getSimpleName());
		if (scheduler.checkExists(jobKey)) {
	        scheduler.triggerJob(jobKey);
	        log.info("Triggered job: " + jobName);
	    } else {
	        JobDetail jobDetails = buildJobDetail(clazz);
	        Trigger trigger = buildTrigger(clazz);
	        scheduler.scheduleJob(jobDetails, trigger);
	        log.info("Scheduled and triggered job: " + jobName);
	    }
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private JobDetail buildJobDetail(Class jobClass) {
		JobDataMap jobDataMap = new JobDataMap();
		jobDataMap.put("date", new Date());
//        jobDataMap.put(jobClass.getSimpleName(), info);

		return JobBuilder.newJob(jobClass).withIdentity(jobClass.getSimpleName()).setJobData(jobDataMap).build();
	}

	@SuppressWarnings("rawtypes")
	private Trigger buildTrigger(Class jobClass, String cronExpression) {
		return TriggerBuilder.newTrigger().withIdentity(jobClass.getSimpleName())
				.withSchedule(CronScheduleBuilder.cronSchedule(cronExpression))
				.startAt(new Date(System.currentTimeMillis())).build();
	}
	
	@SuppressWarnings("rawtypes")
	private Trigger buildTrigger(Class jobClass) {
		return TriggerBuilder.newTrigger()
                .withIdentity(jobClass.getSimpleName())
                .startNow()
                .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                        .withMisfireHandlingInstructionFireNow())
                .build();
	}

//	private Trigger buildTrigger(Class jobClass) {
//		return TriggerBuilder.newTrigger().withIdentity(jobClass.getSimpleName()).startNow().build();
//	}

	@PreDestroy
	public void preDestroy() {
		try {
			scheduler.shutdown();
		} catch (Exception e) {
			log.error("error while stopping scheduler: ", e);
		}
	}
}