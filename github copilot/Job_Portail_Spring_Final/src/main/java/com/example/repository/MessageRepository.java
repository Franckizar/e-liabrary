package com.example.repository;

import com.example.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {
    List<Message> findBySenderId(Integer senderId);
    List<Message> findByReceiverId(Integer receiverId);
    List<Message> findBySenderIdAndReceiverId(Integer senderId, Integer receiverId);
}