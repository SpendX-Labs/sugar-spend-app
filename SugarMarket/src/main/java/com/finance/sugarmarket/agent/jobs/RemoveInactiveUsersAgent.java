package com.finance.sugarmarket.agent.jobs;

import java.util.List;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.finance.sugarmarket.auth.memory.UserOTPs;
import com.finance.sugarmarket.auth.model.MFUser;
import com.finance.sugarmarket.auth.model.MapRoleUser;
import com.finance.sugarmarket.auth.repo.MFUserRepo;
import com.finance.sugarmarket.auth.repo.MapRoleUserRepo;

@Component
public class RemoveInactiveUsersAgent implements Job {
	private static final Logger log = LoggerFactory.getLogger(RemoveInactiveUsersAgent.class);
	
	@Autowired
	private MFUserRepo userRepo;
	@Autowired
	private MapRoleUserRepo mapRoleUserRepo;
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		log.info("Strated removeing Inactive users");
		List<String> expiredList = UserOTPs.getInstance().getExpiredUserIds();
		
		expiredList.forEach(userId -> {
			MFUser user = userRepo.findByUsername(userId);
			if(user.getIsActive()) {
				return;
			}
			log.info("trying to remove: " + userId);
			MapRoleUser mapRoleUser = mapRoleUserRepo.findByUser(user.getId());
			userRepo.delete(user);
			mapRoleUserRepo.delete(mapRoleUser);
		});
		log.info("removed Inactive users");
	}
}
