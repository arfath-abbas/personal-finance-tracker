package com.finance.tracker.arfath.service;


import com.finance.tracker.arfath.model.dto.DashboardResponse;

import java.time.YearMonth;

public interface DashboardService {
    DashboardResponse getDashboard(Long userId, YearMonth month);
}

