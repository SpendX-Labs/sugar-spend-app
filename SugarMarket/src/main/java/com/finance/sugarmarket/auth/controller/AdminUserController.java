package com.finance.sugarmarket.auth.controller;

import com.finance.sugarmarket.auth.dto.UserDetailsDTO;
import com.finance.sugarmarket.auth.service.MFUserService;
import com.finance.sugarmarket.base.controller.BaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/admin/user")
public class AdminUserController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(AdminUserController.class);

    @Autowired
    private MFUserService userService;

    @GetMapping
    public ResponseEntity<List<UserDetailsDTO>> getAllUsers() {
        try {
            checkAdminRole();
            return ResponseEntity.ok(userService.findAllUserDTOs());
        } catch (Exception e) {
            log.error("error while getting user Properties: ", e);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ArrayList<>());
    }
}
