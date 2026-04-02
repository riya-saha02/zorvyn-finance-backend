package com.riya.expense_tracker.repository;

import com.riya.expense_tracker.model.Transaction;
import com.riya.expense_tracker.model.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    // These naming conventions automatically create the SQL queries for you!
    List<Transaction> findByType(TransactionType type);
    List<Transaction> findByCategory(String category);
}