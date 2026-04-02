package com.riya.expense_tracker.controller;

import com.riya.expense_tracker.model.Transaction;
import com.riya.expense_tracker.model.TransactionType;
import com.riya.expense_tracker.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    @Autowired
    private TransactionRepository transactionRepository;

    @GetMapping("/summary")
    public Map<String, Object> getDashboardSummary() {
        List<Transaction> all = transactionRepository.findAll();

        // Calculate Totals using Java Streams
        BigDecimal totalIncome = all.stream()
                .filter(t -> t.getType() == TransactionType.INCOME)
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalExpense = all.stream()
                .filter(t -> t.getType() == TransactionType.EXPENSE)
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Category wise totals
        Map<String, BigDecimal> categoryTotals = all.stream()
                .collect(Collectors.groupingBy(
                        Transaction::getCategory,
                        Collectors.mapping(Transaction::getAmount, Collectors.reducing(BigDecimal.ZERO, BigDecimal::add))
                ));

        return Map.of(
                "totalIncome", totalIncome,
                "totalExpenses", totalExpense,
                "netBalance", totalIncome.subtract(totalExpense),
                "categoryWise", categoryTotals,
                "recentActivity", all.stream().limit(5).collect(Collectors.toList())
        );
    }
}