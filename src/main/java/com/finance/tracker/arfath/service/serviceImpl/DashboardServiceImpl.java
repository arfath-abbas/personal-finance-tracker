package com.finance.tracker.arfath.service.serviceImpl;


import com.finance.tracker.arfath.model.dto.DashboardResponse;
import com.finance.tracker.arfath.utils.TransactionType;
import com.finance.tracker.arfath.repository.TransactionRepository;
import com.finance.tracker.arfath.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final TransactionRepository transactionRepository;

    @Override
    public DashboardResponse getDashboard(Long userId, YearMonth month) {
        LocalDate start = month.atDay(1);
        LocalDate end = month.atEndOfMonth();

        // This month
        BigDecimal income = transactionRepository.getTotalByType(userId, TransactionType.INCOME, start, end);
        if (income == null) income = BigDecimal.ZERO;
        
        BigDecimal expenses = transactionRepository.getTotalByType(userId, TransactionType.EXPENSE, start, end);
        if (expenses == null) expenses = BigDecimal.ZERO;
        
        BigDecimal balance = income.subtract(expenses);

        // Previous month
        YearMonth prevMonth = month.minusMonths(1);
        BigDecimal prevIncome = transactionRepository.getTotalByType(userId, TransactionType.INCOME, prevMonth.atDay(1), prevMonth.atEndOfMonth());
        if (prevIncome == null) prevIncome = BigDecimal.ZERO;
        
        BigDecimal prevExpenses = transactionRepository.getTotalByType(userId, TransactionType.EXPENSE, prevMonth.atDay(1), prevMonth.atEndOfMonth());
        if (prevExpenses == null) prevExpenses = BigDecimal.ZERO;
        
        BigDecimal prevBalance = prevIncome.subtract(prevExpenses);

        // Growth %
        double incomeGrowth = calculateGrowth(prevIncome, income);
        double expenseGrowth = calculateGrowth(prevExpenses, expenses);
        double balanceGrowth = calculateGrowth(prevBalance, balance);

        // Daily spending
        List<DashboardResponse.DailySpending> dailySpending = transactionRepository.getDailySpending(userId, start, end)
                .stream()
                .map(obj -> {
                    int day;
                    if (obj[0] instanceof Integer) {
                        day = (Integer) obj[0];
                    } else if (obj[0] instanceof Double) {
                        day = ((Double) obj[0]).intValue();
                    } else if (obj[0] instanceof Long) {
                        day = ((Long) obj[0]).intValue();
                    } else {
                        day = Integer.parseInt(obj[0].toString());
                    }
                    return new DashboardResponse.DailySpending(day, (BigDecimal) obj[1]);
                })
                .collect(Collectors.toList());

        // Spending by category
        List<DashboardResponse.CategorySpending> categorySpending = transactionRepository.getSpendingByCategory(userId, start, end)
                .stream()
                .map(obj -> new DashboardResponse.CategorySpending((String) obj[0], (BigDecimal) obj[1]))
                .collect(Collectors.toList());

        return DashboardResponse.builder()
                .month(month.toString())
                .income(income)
                .incomeGrowth(incomeGrowth)
                .expenses(expenses)
                .expenseGrowth(expenseGrowth)
                .balance(balance)
                .balanceGrowth(balanceGrowth)
                .dailySpending(dailySpending)
                .categorySpending(categorySpending)
                .build();
    }


    private double calculateGrowth(BigDecimal prev, BigDecimal current) {
        if (prev == null || prev.compareTo(BigDecimal.ZERO) == 0) return 0.0;
        if (current == null) current = BigDecimal.ZERO;
        return current.subtract(prev)
                .divide(prev, 2, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100))
                .doubleValue();
    }

}

