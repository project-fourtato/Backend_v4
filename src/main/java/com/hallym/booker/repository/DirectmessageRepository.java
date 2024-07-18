package com.hallym.booker.repository;

import com.hallym.booker.domain.Directmessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DirectmessageRepository extends JpaRepository<Directmessage, Long> {

    @Query(value = "SELECT m FROM Directmessage m WHERE m.senderUid = :sender_uid")
    List<Directmessage> findAllDirectmessagesBySender(@Param("sender_uid") Long senderUid);

    @Query(value = "SELECT m FROM Directmessage m WHERE m.recipientUid = :recipient_uid")
    List<Directmessage> findAllDirectMessagesByRecipient(@Param("recipient_uid") Long recipientUid);

    boolean existsByMessageId(Long messageId);

}
