package com.riya.expense_tracker.repository;

import com.riya.expense_tracker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Spring Boot automatically writes the SQL for this just based on the method name!
    Optional<User> findByUsername(String username);
}