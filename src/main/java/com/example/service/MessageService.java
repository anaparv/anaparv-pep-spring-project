package com.example.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.example.entity.Message;
import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;

@Service
public class MessageService {
    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private AccountRepository accountRepository; 

    public Message createMessage(Message message) {
        // Validate message text
        if (message.getMessageText() == null || message.getMessageText().isBlank() || message.getMessageText().length() > 255) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Message text must be non-blank and under 255 characters");
        }

        // Check if the user exists
        if (!accountRepository.existsById(message.getPostedBy())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "PostedBy user does not exist");
        }

        // Save the new message to the database
        return messageRepository.save(message);
    }

    // Return all messages from the database
    public List<Message> getAllMessages() {
        return messageRepository.findAll(); 
    }

    public Optional<Message> getMessageById(Integer messageId) {
        return messageRepository.findById(messageId);
    }

    @Transactional
    public int deleteMessage(Integer messageId) {
        // Check if the message exists first
        boolean exists = messageRepository.existsById(messageId);
        if (exists) {
            messageRepository.deleteById(messageId);
            // One row deleted
            return 1; 
        }
        // No row deleted
        return 0; 
    }

    public int updateMessage(Integer messageId, String messageText) {
        Optional<Message> optionalMessage = messageRepository.findById(messageId);
        
        if (optionalMessage.isPresent()) {
            Message message = optionalMessage.get();
            message.setMessageText(messageText);
            messageRepository.save(message);
            // Indicate that one row was updated
            return 1; 
        }
        // No message found, so no rows were updated
        return 0; 
    }

    // Retrieve messages by postedBy user ID
    public List<Message> getMessagesByUserId(Integer userId) {
        return messageRepository.findByPostedBy(userId);
    }
}
