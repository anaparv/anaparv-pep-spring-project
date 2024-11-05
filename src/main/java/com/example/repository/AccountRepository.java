package com.example.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.entity.Account;

public interface AccountRepository extends JpaRepository<Account, Integer> {
    
    // Check if a user already exists
    Optional<Account> findByUsername(String username);
    boolean existsByUsername(String username);
}
