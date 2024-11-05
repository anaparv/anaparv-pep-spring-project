package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.entity.Message;
import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {

    // Find messages by the user ID
    List<Message> findByPostedBy(Integer postedBy);

    // Delete by id
    @Modifying
    @Query("DELETE FROM Message m WHERE m.id = :messageId")
    void deleteById(@Param("messageId") Integer messageId);
}
