package com.finance.tracker.arfath.model.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardResponse {

    private String month;
    private BigDecimal income;
    private double incomeGrowth;
    private BigDecimal expenses;
    private double expenseGrowth;
    private BigDecimal balance;
    private double balanceGrowth;
    private List<DailySpending> dailySpending;
    private List<CategorySpending> categorySpending;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DailySpending {
        private int day;
        private BigDecimal amount;
    }
    
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CategorySpending {
        private String category;
        private BigDecimal amount;
    }
}
