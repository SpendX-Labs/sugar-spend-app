package com.finance.sugarmarket.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.finance.sugarmarket.auth.config.UserPrincipal;
import com.finance.sugarmarket.auth.model.MFUser;
import com.finance.sugarmarket.auth.model.MapRoleUser;
import com.finance.sugarmarket.auth.repo.MFUserRepo;
import com.finance.sugarmarket.auth.repo.MapRoleUserRepo;
import com.finance.sugarmarket.auth.util.AuthenticationUtil;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private MFUserRepo userRepo;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		MFUser user;
		if (AuthenticationUtil.getInstance().isValidEmail(username)) {
			user = userRepo.findByEmailAndISActive(username);
		} else {
			user = userRepo.findBYUsernameAndISActive(username);
		}
		if (user == null)
			throw new UsernameNotFoundException(username + " not found");

		return new UserPrincipal(user.getId(), user.getUsername(), user.getPassword());
	}

}