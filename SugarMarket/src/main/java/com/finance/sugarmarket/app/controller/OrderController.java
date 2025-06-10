package com.finance.sugarmarket.app.controller;

import com.finance.sugarmarket.app.dto.MutualFundPortfolio;
import com.finance.sugarmarket.app.model.OrderDetail;
import com.finance.sugarmarket.app.service.CamsKFinTechPDFPerserService;
import com.finance.sugarmarket.app.service.OrderService;
import com.finance.sugarmarket.app.service.SaveOrderService;
import com.finance.sugarmarket.auth.service.MFUserService;
import com.finance.sugarmarket.base.controller.BaseController;
import com.finance.sugarmarket.constants.AppConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/app")
public class OrderController extends BaseController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private SaveOrderService saveOrderService;
    @Autowired
    private MFUserService userService;

    private final ApplicationContext context;

    @Autowired
    public OrderController(ApplicationContext context) {
        this.context = context;
    }

    private static final Logger log = LoggerFactory.getLogger(OrderController.class);

    @GetMapping("/get-order-details")
    public MutualFundPortfolio getFundData() {
        return orderService.getMutualFundPortfolio(getUserName());
    }

    @PostMapping("/save-order-detail")
    public ResponseEntity<String> saveInvestment(@RequestBody OrderDetail request) {
        String data = "";
        try {
            request.setUser(userService.getUserByUsername(getUserName()));
            data = saveOrderService.saveInvestment(request);
        } catch (Exception e) {
            log.error("error while saving fund data: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(AppConstants.FAILED);
        }
        if (!data.equals(AppConstants.SUCCESS))
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(data);
        return ResponseEntity.ok("order detail saved");
    }

    @PostMapping("/bulk-upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file,
                                             @RequestParam("password") String password) {
        try {

            saveOrderService = context.getBean(CamsKFinTechPDFPerserService.class);

            String uploadDirectory = AppConstants.FILE_UPLOAD_DIR;

            File directory = new File(uploadDirectory);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            String fileName = StringUtils.cleanPath(file.getOriginalFilename());

            Path filePath = Paths.get(uploadDirectory, fileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            saveOrderService.saveFile(filePath.toFile(), userService.getUserByUsername(getUserName()), password);

            return ResponseEntity.ok("File uploaded successfully");
        } catch (Exception e) {
            log.error("error while uploading File: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error uploading file: " + e.getMessage());
        }
    }

}
