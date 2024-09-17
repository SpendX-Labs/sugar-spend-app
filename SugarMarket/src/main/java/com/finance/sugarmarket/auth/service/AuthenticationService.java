package com.finance.sugarmarket.auth.service;

import java.security.SecureRandom;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.finance.sugarmarket.auth.config.UserPrincipal;
import com.finance.sugarmarket.auth.dto.AuthenticationRequest;
import com.finance.sugarmarket.auth.dto.AuthenticationResponse;
import com.finance.sugarmarket.auth.dto.GenericResponse;
import com.finance.sugarmarket.auth.dto.SignUpRequestDTO;
import com.finance.sugarmarket.auth.dto.SignUpResponseDTO;
import com.finance.sugarmarket.auth.dto.UserDetailsDTO;
import com.finance.sugarmarket.auth.memory.Tokens;
import com.finance.sugarmarket.auth.memory.UserOTPs;
import com.finance.sugarmarket.auth.model.MFRole;
import com.finance.sugarmarket.auth.model.MFUser;
import com.finance.sugarmarket.auth.model.MapRoleUser;
import com.finance.sugarmarket.auth.model.OTPDetails;
import com.finance.sugarmarket.auth.model.Token;
import com.finance.sugarmarket.auth.repo.MFUserRepo;
import com.finance.sugarmarket.auth.repo.MapRoleUserRepo;
import com.finance.sugarmarket.auth.repo.RoleRepo;
import com.finance.sugarmarket.constants.MFConstants;
import com.finance.sugarmarket.sms.service.EmailService;

@Service
public class AuthenticationService {

	private static final Logger log = LoggerFactory.getLogger(AuthenticationService.class);

	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private UserDetailsService userDetailsService;
	@Autowired
	private JwtService jwtService;
	@Autowired
	private MFUserRepo userRepo;
	@Autowired
	private MapRoleUserRepo mapRoleUserRepo;
	@Autowired
	private RoleRepo roleRepo;
	@Autowired
	private EmailService emailService;
	@Autowired
    private PasswordEncoder passwordEncoder;

	private static final SecureRandom secureRandom = new SecureRandom();
	private static final int OTP_LENGTH = 6;
	private static final long OTP_EXPIRATION_TIME_MS = 5 * 60 * 1000;

	public AuthenticationResponse authenticate(AuthenticationRequest request) {
		authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
		UserDetails user = userDetailsService.loadUserByUsername(request.getUsername());
		var jwtToken = jwtService.generateToken(user);
		saveUserToken(user, jwtToken);
		removeUserPassword(user);
		return new AuthenticationResponse(jwtToken, user);
	}

	private void removeUserPassword(UserDetails user) {
		if (user instanceof UserPrincipal) {
			UserPrincipal up = (UserPrincipal) user;
			up.setPassword(null);
			user = up;
		}
	}

	private void saveUserToken(UserDetails user, String jwtToken) {
		Token token = new Token();
		token.setToken(jwtToken);
		token.setUser(user);
		token.setExpired(false);
		Tokens.tokenMap.put(jwtToken, token);
	}

	public UserDetails getUserDetailsByToken(String token) throws Exception {
		if (Tokens.tokenMap.get(token) == null) {
			throw new Exception("token not found");
		}
		String userName = jwtService.extractUsername(token);
		return getUserDetailsByUserName(userName);
	}

	public SignUpResponseDTO signup(SignUpRequestDTO request) {
		if (userRepo.existsByUsernameAndIsActive(request.getUsername(), true)) {
			return new SignUpResponseDTO(request.getUsername() + " already exists", false);
		}
		if(userRepo.existsByEmailAndIsActive(request.getEmailId(), true)) {
			return new SignUpResponseDTO(request.getEmailId() + " already exists", false);
		}

		MFUser user = new MFUser();
		user.setEmail(request.getEmailId());
		user.setFullname(request.getFullName());
		user.setPassword(passwordEncoder.encode(request.getPassword()));
		user.setPhonenumber(request.getPhoneNumber());
		user.setUsername(request.getUsername());
		user.setIsActive(false);
		user = userRepo.save(user);
		MapRoleUser mapRoleUser = saveMapRoleUser(user);
		if (!sendOTPEmailandSave(request.getEmailId(), request.getUsername(), request.getFullName())) {
			removeUserAndRoleMapping(user, mapRoleUser);
			return new SignUpResponseDTO("Failed to send OTP. Please try again.", false);
		}
		return new SignUpResponseDTO(request.getUsername(), request.getEmailId(),
				"OTP is sent to the registered email ID", true);
	}

	private MapRoleUser saveMapRoleUser(MFUser user) {
		MFRole role = roleRepo.getReferenceById(2);
		MapRoleUser mapRoleUser = new MapRoleUser();
		mapRoleUser.setRole(role);
		mapRoleUser.setUser(user);
		return mapRoleUserRepo.save(mapRoleUser);
	}

	private boolean sendOTPEmailandSave(String emailId, String username, String fullName) {
		try {
			log.info("sending otp for " + username);
			OTPDetails otp = new OTPDetails(generateOTP(), OTP_EXPIRATION_TIME_MS);
			UserOTPs.getInstance().addOtp(username, otp); //add otp
			String subject = fullName + "! Here is your OTP";
			String body = otp.getOtp() + " is your OTP for Sugar Spend. Please do not share to anyone.\nArigato";
			emailService.sendSMS(emailId, subject, body);
		} catch (Exception e) {
			log.error("Error while sending OTP.", e);
			return false;
		}
		return true;
	}

	private String generateOTP() throws Exception {
		StringBuilder otp = new StringBuilder();
		for (int i = 0; i < OTP_LENGTH; i++) {
			otp.append(secureRandom.nextInt(10));
		}
		return otp.toString();
	}

	private void removeUserAndRoleMapping(MFUser user, MapRoleUser mapRoleUser) {
		mapRoleUserRepo.delete(mapRoleUser);
		userRepo.delete(user);
	}

	public SignUpResponseDTO verifyotp(SignUpRequestDTO request) {
		OTPDetails otp = UserOTPs.getInstance().getOtp(request.getUsername());
		if(otp != null && 
				(otp.getExpirationTime() > System.currentTimeMillis() 
						|| otp.getOtp().equals(request.getOtp()))) {
			MFUser user = userRepo.findByUsername(request.getUsername());
			user.setIsActive(true);
			userRepo.save(user);
			UserOTPs.getInstance().removeOtp(request.getUsername());
			return new SignUpResponseDTO(request.getUsername(), request.getEmailId(),
					"Signup successful please login", true);
		}
		else if(otp != null && otp.getExpirationTime() < System.currentTimeMillis()) {
			UserOTPs.getInstance().removeOtp(request.getUsername());
			return new SignUpResponseDTO(request.getUsername(), request.getEmailId(),
					"OTP Expired", false);
		}
		return new SignUpResponseDTO(request.getUsername(), request.getEmailId(),
				"incorrect OTP", false);
	}
	
	public UserDetails saveUserDetails(UserDetailsDTO userDto, String userName) {
		MFUser user = userRepo.findByUsername(userName);
		user.setFullname(userDto.getFullName());
		user.setPhonenumber(user.getPhonenumber());
		OTPDetails otp = UserOTPs.getInstance().getOtp(userName);
		if(otp != null && 
				(otp.getExpirationTime() > System.currentTimeMillis() 
						|| otp.getOtp().equals(userDto.getOtp()))) {
			if(StringUtils.isNotBlank(userDto.getEmail())) {
				user.setEmail(userDto.getEmail());
			}
			if(StringUtils.isNotBlank(userName))
			user.setPassword(userDto.getPassword());
		}
		userRepo.saveAndFlush(user);
		return getUserDetailsByUserName(userName);
	}
	
	private UserDetails getUserDetailsByUserName(String userName) {
		UserDetails user = userDetailsService.loadUserByUsername(userName);
		removeUserPassword(user);
		return user;
	}
	
	public GenericResponse forgetPassword(Map<String, String> request) {
		MFUser user = null;
		if(request.get(MFConstants.REQUEST_ID).contains("@")) {
			user = userRepo.findByEmail(request.get(MFConstants.REQUEST_ID));
		}
		else {
			user = userRepo.findByUsername(request.get(MFConstants.REQUEST_ID));
		}
		
		if(user == null) {
			return new GenericResponse("User doesn't exits", false);
		}
		
		if(!sendOTPEmailandSave(user.getEmail(), user.getUsername(), user.getFullname())) {
			return new GenericResponse("Failed to send OTP, Please try later", false);
		}
		
		return new GenericResponse("OTP sent to your email", true);
		
	}

}