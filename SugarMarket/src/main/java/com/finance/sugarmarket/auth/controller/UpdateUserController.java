package com.finance.sugarmarket.auth.controller;

import com.finance.sugarmarket.auth.dto.GenericResponse;
import com.finance.sugarmarket.auth.dto.UpdatePasswordDto;
import com.finance.sugarmarket.auth.dto.UserDetailsDTO;
import com.finance.sugarmarket.auth.service.AuthenticationService;
import com.finance.sugarmarket.base.controller.BaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/settings/user")
public class UpdateUserController extends BaseController {
    @Autowired
    private AuthenticationService authenticationService;

    private static final Logger log = LoggerFactory.getLogger(UpdateUserController.class);

    @PatchMapping("email/{emailId}")
    public ResponseEntity<GenericResponse> updateEmail(@PathVariable("emailId") String emailId) {
        GenericResponse response = authenticationService.updateEmail(emailId, getUserId());
        if (response.getStatus()) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    @PatchMapping("email/verify/{otp}")
    public ResponseEntity<GenericResponse> verifyEmailOTP(@PathVariable("otp") String otp) {
        GenericResponse response = authenticationService.confirmEmailCheckWithOtp(otp, getUserId());
        if (response.getStatus()) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    @PatchMapping("password")
    public ResponseEntity<GenericResponse> updatePassword(@RequestBody UpdatePasswordDto passwordDto) {
        GenericResponse response = authenticationService.updatePassword(passwordDto, getUserId());
        if (response.getStatus()) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    @PatchMapping("userinfo")
    public ResponseEntity<UserDetailsDTO> updateUserInfo(@RequestBody UserDetailsDTO request) {
        try {
            return ResponseEntity.ok(authenticationService.saveUserDetails(request, getUserId()));
        } catch (Exception e) {
            log.error("updateUserInfo failed", e);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new UserDetailsDTO("Error while saving User details."));
    }
}
