package com.finance.sugarmarket.auth.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.finance.sugarmarket.auth.enums.UserPropertiesEnum;
import com.finance.sugarmarket.auth.model.MFUser;
import com.finance.sugarmarket.auth.model.MapUserProperties;
import com.finance.sugarmarket.auth.model.UserProperties;
import com.finance.sugarmarket.auth.repo.MFUserRepo;
import com.finance.sugarmarket.auth.repo.MapUserPropertiesRepo;
import com.finance.sugarmarket.auth.repo.UserPropertiesRepo;
import com.finance.sugarmarket.constants.RedisConstants;

@Service
public class UserPropertiesService {

	@Autowired
	private MapUserPropertiesRepo mapUserPropertiesRepo;
	@Autowired
	private UserPropertiesRepo userPropertiesRepo;
	@Autowired
	private MFUserRepo mfUserRepo;
	
	@Cacheable(value = RedisConstants.USER_PROPERTIES, key = "#userId")
	public Map<String, String> getAllUserProperties(Long userId) {
		String[] allProperties = UserPropertiesEnum.getAllValues();
		Map<String, String> map = new HashMap<>();
		for (String property : allProperties) {
			map.put(property, mapUserPropertiesRepo.fetchValueByPropertyName(property, userId));
		}
		return map;
	}

	public void saveUserProperty(Long userId, String property, String value) {
		UserProperties userProperty = userPropertiesRepo.findByName(property);
		MFUser user = mfUserRepo.findById(userId).get();
		MapUserProperties mapUserProperties = new MapUserProperties();
		mapUserProperties.setUser(user);
		mapUserProperties.setUserProperties(userProperty);
		mapUserProperties.setValue(value);
		mapUserPropertiesRepo.save(mapUserProperties);
		evictUserPropertiesCache(userId);
	}
	
	@CacheEvict(value = RedisConstants.USER_PROPERTIES, key = "#userId")
	public void evictUserPropertiesCache(Long userId) {
	    // This method will clear all entries in the userPropertiesCache
	}
}
