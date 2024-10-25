package com.finance.sugarmarket.auth.service;

import java.security.SecureRandom;

import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
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
import com.finance.sugarmarket.auth.dto.UpdatePasswordDto;
import com.finance.sugarmarket.auth.dto.UserDetailsDTO;
import com.finance.sugarmarket.auth.dto.UserTempData;
import com.finance.sugarmarket.auth.model.MFRole;
import com.finance.sugarmarket.auth.model.MFUser;
import com.finance.sugarmarket.auth.model.MapRoleUser;
import com.finance.sugarmarket.auth.repo.MFUserRepo;
import com.finance.sugarmarket.auth.repo.MapRoleUserRepo;
import com.finance.sugarmarket.auth.repo.RoleRepo;
import com.finance.sugarmarket.auth.util.AuthenticationUtil;
import com.finance.sugarmarket.base.enums.SignUpPrefixType;
import com.finance.sugarmarket.constants.AppConstants;
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
	@Autowired
	private UserJwtCacheService jtwCacheService;
	@Autowired
	private UserOTPCacheService userOtpCacheService;
	@Autowired
	private ModelMapper modelMapper;

	private static final SecureRandom secureRandom = new SecureRandom();
	private static final int OTP_LENGTH = 6;

	public AuthenticationResponse authenticate(AuthenticationRequest request, String loggedInBy) {
		authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

		UserDetails user = userDetailsService.loadUserByUsername(request.getUsername());
		removeUserPassword(user);
		String jwtToken = jwtService.generateToken(user);
		jtwCacheService.saveUserToken(jwtToken, user, loggedInBy);
		return new AuthenticationResponse(jwtToken, user);
	}

	private void removeUserPassword(UserDetails user) {
		if (user instanceof UserPrincipal) {
			UserPrincipal up = (UserPrincipal) user;
			up.setPassword(null);
			user = up;
		}
	}

	public SignUpResponseDTO signup(SignUpRequestDTO request) {
		if (userRepo.existsByUsernameAndIsActive(request.getUsername(), true)) {
			return new SignUpResponseDTO(request.getUsername() + " already exists", false);
		}
		if (userRepo.existsByEmailAndIsActive(request.getEmailId(), true)) {
			return new SignUpResponseDTO(request.getEmailId() + " already exists", false);
		}

		MFUser user = new MFUser();
		if (!AuthenticationUtil.getInstance().isValidEmail(request.getEmailId())) {
			return new SignUpResponseDTO("Invalid email", false);
		}
		user.setEmail(request.getEmailId());
		user.setFullname(request.getFullName());
		String passwordPolicy = AuthenticationUtil.getInstance().checkPasswordPolicy(request.getPassword());
		if (!passwordPolicy.equals(AppConstants.SUCCESS)) {
			return new SignUpResponseDTO(passwordPolicy, false);
		}
		user.setPassword(passwordEncoder.encode(request.getPassword()));
		user.setPhonenumber(request.getPhoneNumber());
		user.setUsername(request.getUsername());
		user.setIsActive(true);

		String otpDetail = sendOTPEmailandSave(request.getEmailId(), request.getUsername(), request.getFullName());

		if (StringUtils.isBlank(otpDetail)) {
			return new SignUpResponseDTO("Failed to send OTP. Please try again.", false);
		}
		userOtpCacheService.saveUserInCache(new UserTempData(user, otpDetail), request.getUsername());
		return new SignUpResponseDTO(request.getUsername(), request.getEmailId(),
				"OTP is sent to the registered email ID", true);
	}

	private String sendOTPEmailandSave(String emailId, String username, String fullName) {
		String otp = null;
		try {
			log.info("sending otp for " + username);
			otp = generateOTP();
			String subject = fullName + "! Here is your OTP";
			String body = otp + " is your OTP for Sugar Spend. Please do not share to anyone.\nArigato";
			emailService.sendSMS(emailId, subject, body);
		} catch (Exception e) {
			log.error("Error while sending OTP.", e);
		}
		return otp;
	}

	private String generateOTP() throws Exception {
		StringBuilder otp = new StringBuilder();
		for (int i = 0; i < OTP_LENGTH; i++) {
			otp.append(secureRandom.nextInt(10));
		}
		return otp.toString();
	}

	public SignUpResponseDTO verifyotp(SignUpRequestDTO request) {
		UserTempData userTempData = userOtpCacheService.getUserInCache(SignUpPrefixType.USER_SIGNUP,
				request.getUsername());
		if (userTempData != null && userTempData.getOtp() != null && userTempData.getOtp().equals(request.getOtp())) {
			MFUser user = userTempData.getUser();
			user = userRepo.save(user);
			saveMapRoleUser(user);
			return new SignUpResponseDTO(request.getUsername(), request.getEmailId(), "Signup successful please login",
					true);
		} else if (userTempData != null && userTempData.getOtp() != null
				&& !userTempData.getOtp().equals(request.getOtp())) {
			return new SignUpResponseDTO(request.getUsername(), request.getEmailId(), "incorrect OTP", false);
		}
		return new SignUpResponseDTO(request.getUsername(), request.getEmailId(), "OTP Expired", false);
	}

	private MapRoleUser saveMapRoleUser(MFUser user) {
		MFRole role = roleRepo.getReferenceById(2L);
		MapRoleUser mapRoleUser = new MapRoleUser();
		mapRoleUser.setRole(role);
		mapRoleUser.setUser(user);
		return mapRoleUserRepo.save(mapRoleUser);
	}

	public UserDetails saveUserDetails(UserDetailsDTO userDto, Long userId) throws Exception {
		MFUser user = userRepo.findById(userId).get();
		modelMapper.map(userDto, user);
		userRepo.saveAndFlush(user);
		UserDetails userDetails = getUserDetailsByUserName(user.getUsername());
		jtwCacheService.updateAllCacheKeyValues(user.getId(), userDetails);
		return userDetails;
	}

	private UserDetails getUserDetailsByUserName(String userName) {
		UserDetails user = userDetailsService.loadUserByUsername(userName);
		removeUserPassword(user);
		return user;
	}

	public GenericResponse forgetPassword(AuthenticationRequest request) {
		MFUser user = null;
		String passwordPolicy = AuthenticationUtil.getInstance().checkPasswordPolicy(request.getPassword());
		if (!passwordPolicy.equals(AppConstants.SUCCESS)) {
			return new SignUpResponseDTO(passwordPolicy, false);
		}
		String password = passwordEncoder.encode(request.getPassword());
		if (AuthenticationUtil.getInstance().isValidEmail(request.getUsername())) {
			user = userRepo.findByEmailAndISActive(request.getUsername());
		} else {
			user = userRepo.findBYUsernameAndISActive(request.getUsername());
		}

		if (user == null) {
			return new GenericResponse("User doesn't exits", false);
		}

		String otpDetail = sendOTPEmailandSave(user.getEmail(), user.getUsername(), user.getFullname());

		if (StringUtils.isBlank(otpDetail)) {
			return new GenericResponse("Failed to send OTP. Please try again.", false);
		}
		userOtpCacheService.saveUserInCache(new UserTempData(password, otpDetail, SignUpPrefixType.FORGET_PASSWORD),
				user.getUsername());
		return new GenericResponse("OTP is sent to the registered email ID", true);
	}

	public GenericResponse confirmPasswordCheckWithOtp(String otp, String username) {
		MFUser user = null;
		if (AuthenticationUtil.getInstance().isValidEmail(username)) {
			user = userRepo.findByEmailAndISActive(username);
		} else {
			user = userRepo.findBYUsernameAndISActive(username);
		}
		UserTempData userTempData = userOtpCacheService.getUserInCache(SignUpPrefixType.FORGET_PASSWORD,
				user.getUsername());
		if (userTempData != null && userTempData.getOtp() != null && userTempData.getOtp().equals(otp)) {
			user.setPassword(userTempData.getPassword());
			user = userRepo.save(user);
			return new GenericResponse("Email Changed Successfully", true);
		} else if (userTempData != null && userTempData.getOtp() != null && !userTempData.getOtp().equals(otp)) {
			return new GenericResponse("incorrect OTP", false);
		}
		return new GenericResponse("OTP Expired", false);
	}

	public GenericResponse updateEmail(String email, Long userId) {
		MFUser user = userRepo.findById(userId).get();
		String otpDetail = sendOTPEmailandSave(email, user.getUsername(), user.getFullname());
		if (StringUtils.isBlank(otpDetail)) {
			return new GenericResponse("Failed to send OTP. Please try again.", false);
		}
		userOtpCacheService.saveUserInCache(new UserTempData(email, otpDetail, SignUpPrefixType.UPDATE_EMAIL),
				user.getUsername());
		return new GenericResponse("OTP is sent to the registered email ID", true);
	}

	public GenericResponse confirmEmailCheckWithOtp(String otp, Long userId) {
		MFUser user = userRepo.findById(userId).get();
		UserTempData userTempData = userOtpCacheService.getUserInCache(SignUpPrefixType.UPDATE_EMAIL,
				user.getUsername());
		if (userTempData != null && userTempData.getOtp() != null && userTempData.getOtp().equals(otp)) {
			user.setEmail(userTempData.getEmail());
			user = userRepo.save(user);
			return new GenericResponse("Email Changed Successfully", true);
		} else if (userTempData != null && userTempData.getOtp() != null && !userTempData.getOtp().equals(otp)) {
			return new GenericResponse("incorrect OTP", false);
		}
		return new GenericResponse("OTP Expired", false);
	}

	public GenericResponse updatePassword(UpdatePasswordDto passwordDto, Long userId) {

		if (!passwordDto.getNewPassword().equals(passwordDto.getConfirmPassword())) {
			return new GenericResponse("Password Didn't match ", false);
		}
		String passwordPolicy = AuthenticationUtil.getInstance().checkPasswordPolicy(passwordDto.getNewPassword());

		if (!passwordPolicy.equals(AppConstants.SUCCESS)) {
			return new SignUpResponseDTO(passwordPolicy, false);
		}

		MFUser user = userRepo.findById(userId).get();
		if (!passwordEncoder.matches(passwordDto.getCurrentPassword(), user.getPassword())) {
	        return new GenericResponse("Wrong password", false);
	    }
		
		String encryptedNewPassword = passwordEncoder.encode(passwordDto.getNewPassword());

		user.setPassword(encryptedNewPassword);

		userRepo.save(user);

		return new GenericResponse("Email Changed Successfully", true);
	}
}