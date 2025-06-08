package com.finance.sugarmarket.app.controller;

import com.finance.sugarmarket.app.dto.LineChartDto;
import com.finance.sugarmarket.app.service.AnalysisService;
import com.finance.sugarmarket.base.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/app")
public class AnalysisController extends BaseController {

    @Autowired
    private AnalysisService analysisService;

    @GetMapping("/get-line-chart")
    public List<LineChartDto> getLineCharts() {
        return analysisService.getLineCharts(getUserName());
    }
}
