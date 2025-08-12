package com.example.service;

import com.example.model.Message;
import com.example.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {

    private final MessageRepository messageRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public Message sendMessage(Message message) {
        return messageRepository.save(message);
    }

    public List<Message> getMessagesByUserId(int userId) {
        return messageRepository.findBySenderIdOrReceiverId(userId, userId);
    }

    public void deleteMessage(int messageId) {
        messageRepository.deleteById(messageId);
    }
}