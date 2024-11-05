package com.example.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.entity.Account;
import com.example.repository.AccountRepository;

@Service
public class AccountService {
    
    @Autowired
    private AccountRepository accountRepository;

    public Account registerAccount(Account account) {
        // Validate username
        if (account.getUsername() == null || account.getUsername().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username cannot be blank");
        }

        // Validate password length
        if (account.getPassword() == null || account.getPassword().length() < 4) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password must be at least 4 characters long");
        }

        // Check if the username already exists
        if (accountRepository.existsByUsername(account.getUsername())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already exists");
        }

        // Save the new account to the database
        return accountRepository.save(account);
    }

    public Account login(String username, String password) {
        Optional<Account> optionalAccount = accountRepository.findByUsername(username);
        
        if (optionalAccount.isPresent()) {
            Account account = optionalAccount.get();
            // Replace with a secure password check
            if (account.getPassword().equals(password)) { 
                // Return the authenticated account
                return account; 
            }
        }
        // Return null if authentication fails
        return null; 
    }
}
