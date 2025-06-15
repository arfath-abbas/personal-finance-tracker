package com.finance.tracker.arfath.controller;

import com.finance.tracker.arfath.model.dto.DashboardResponse;
import com.finance.tracker.arfath.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.YearMonth;

@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin("*")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping
    public DashboardResponse getDashboard(
            @RequestParam(name = "month") @DateTimeFormat(pattern = "yyyy-MM") YearMonth month
    ) {
        // Using a hardcoded userId of 1 for now
        return dashboardService.getDashboard(1L, month);
    }
}
