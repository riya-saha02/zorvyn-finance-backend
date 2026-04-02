package com.riya.expense_tracker.controller;

import com.riya.expense_tracker.model.Transaction;
import com.riya.expense_tracker.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    private TransactionRepository transactionRepository;

    // Requirement: Viewer, Analyst, and Admin can all VIEW records
    @GetMapping
    @PreAuthorize("hasAnyRole('VIEWER', 'ANALYST', 'ADMIN')")
    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    // Requirement: Only ADMIN can CREATE records
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Transaction createTransaction(@RequestBody Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    // Requirement: Only ADMIN can UPDATE records
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Transaction updateTransaction(@PathVariable Long id, @RequestBody Transaction details) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Record not found"));

        transaction.setAmount(details.getAmount());
        transaction.setCategory(details.getCategory());
        transaction.setType(details.getType());
        transaction.setNotes(details.getNotes());

        return transactionRepository.save(transaction);
    }

    // Requirement: Only ADMIN can DELETE records
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteTransaction(@PathVariable Long id) {
        transactionRepository.deleteById(id);
        return "Transaction deleted successfully";
    }
}