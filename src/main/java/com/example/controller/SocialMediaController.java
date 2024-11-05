package com.example.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.service.AccountService;
import com.example.service.MessageService;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
@RestController
public class SocialMediaController {

    private final AccountService accountService;

    @Autowired
    private MessageService messageService;

    @Autowired
    public SocialMediaController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/register")
    public ResponseEntity<Account> register(@RequestBody Account account) {
        Account createdAccount = accountService.registerAccount(account);
        return ResponseEntity.ok(createdAccount);
    }

    @PostMapping("/login")
    public ResponseEntity<Account> login(@RequestBody Account account) {
        Account authenticatedAccount = accountService.login(account.getUsername(), account.getPassword());
    
        if (authenticatedAccount != null) {
            // 200 OK with account details
            return ResponseEntity.ok(authenticatedAccount); 
        } else {
            // 401 Unauthorized
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); 
        }
    }
    
    @PostMapping("/messages")
    public ResponseEntity<Message> createMessage(@RequestBody Message message) {
        try {
            Message createdMessage = messageService.createMessage(message);
            // Return 200 OK with message details
            return ResponseEntity.ok(createdMessage); 
        } catch (ResponseStatusException e) {
            // Return the appropriate error status
            return ResponseEntity.status(e.getStatus()).body(null); 
        }
    }

    @GetMapping("/messages")
    public ResponseEntity<List<Message>> getAllMessages() {
        // Retrieve all messages
        List<Message> messages = messageService.getAllMessages();
        // Return 200 OK with the list of messages
        return ResponseEntity.ok(messages);
    }

    @GetMapping("/messages/{messageId}")
    public ResponseEntity<Message> getMessageById(@PathVariable Integer messageId) {
        Optional<Message> messageOptional = messageService.getMessageById(messageId);

        if (messageOptional.isPresent()) {
            return ResponseEntity.ok(messageOptional.get());
        } else {
            // Keep the body null for empty response
            return ResponseEntity.ok().body(null); 
        }
    }

    @DeleteMapping("/messages/{messageId}")
    public ResponseEntity<Integer> deleteMessage(@PathVariable Integer messageId) {
        int rowsUpdated = messageService.deleteMessage(messageId);
        
        // Response based on whether a message was deleted
        if (rowsUpdated > 0) {
            // Return the number of rows modified
            return ResponseEntity.ok(rowsUpdated); 
        } else {
            // Return empty body if no rows updated
            return ResponseEntity.ok().body(null); 
        }
    }

    @PatchMapping("/messages/{messageId}")
    public ResponseEntity<Integer> updateMessage(@PathVariable Integer messageId, @RequestBody Map<String, String> requestBody) {
        String messageText = requestBody.get("messageText");
    
        // Validate the messageText
        if (messageText == null || messageText.trim().isEmpty() || messageText.length() > 255) {
            // 400 Bad Request
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build(); 
        }
    
        int rowsUpdated = messageService.updateMessage(messageId, messageText);
        
        if (rowsUpdated > 0) {
            // Return number of rows updated (1)
            return ResponseEntity.ok(rowsUpdated); 
        } else {
            // Message ID does not exist
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build(); 
        }
    }

    @GetMapping("/accounts/{accountId}/messages")
    public ResponseEntity<List<Message>> getMessagesByAccountId(@PathVariable Integer accountId) {
        List<Message> messages = messageService.getMessagesByUserId(accountId);
        // Return messages, empty list if none found
        return ResponseEntity.ok(messages); 
    }
}
