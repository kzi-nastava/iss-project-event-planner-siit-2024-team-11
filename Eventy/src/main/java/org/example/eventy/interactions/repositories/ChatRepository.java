package org.example.eventy.interactions.repositories;

import org.example.eventy.interactions.model.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ChatRepository extends JpaRepository<Chat, Long> {

    @Query(value = "SELECT c FROM Chat c WHERE c.endUserOne.id = :endUserId OR c.endUserTwo.id = :endUserId " +
            "ORDER BY c.lastMessageTime DESC")
    public List<Chat> findAllByEndUser(Long endUserId);

    @Query(value = "SELECT c FROM Chat c WHERE (c.endUserOne.id = :endUserOne AND c.endUserTwo.id = :endUserTwo) " +
            "OR (c.endUserOne.id = :endUserTwo AND c.endUserTwo.id = :endUserOne)")
    public Chat findByEndUserOneAndEndUserTwo(Long endUserOne, Long endUserTwo);
}
