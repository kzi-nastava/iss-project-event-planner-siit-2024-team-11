package org.example.eventy.users.repositories;

import org.example.eventy.interactions.model.Notification;
import org.example.eventy.users.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);

    User findByPhoneNumber(String phone);

    @Transactional
    @Modifying
    @Query(value = "UPDATE Users SET first_name = :firstName, last_name = :lastName, user_type = :userType WHERE id = :userId", nativeQuery = true)
    void upgradeUserToOrganizer(@Param("userId") Long userId,
                                @Param("firstName") String firstName,
                                @Param("lastName") String lastName,
                                @Param("userType") String userType);

    @Transactional
    @Modifying
    @Query(value = "UPDATE Users SET name = :name, description = :description, user_type = :userType WHERE id = :userId", nativeQuery = true)
    void upgradeUserToProvider(@Param("userId") Long userId,
                               @Param("name") String name,
                               @Param("description") String description,
                               @Param("userType") String userType);

    @Query("SELECT n FROM User u JOIN u.notifications n WHERE u.id = :userId ORDER BY n.timestamp DESC")
    Page<Notification> findAllNotificationsByUserId(@Param("userId") Long userId,
                                                    Pageable pageable);

    @Query("SELECT COUNT(n) > 0 FROM User u JOIN u.notifications n WHERE u.id = :userId AND n.timestamp > u.lastReadNotifications")
    boolean hasNewNotifications(@Param("userId") Long userId);

    @Query("SELECT u.id FROM User u JOIN u.acceptedEvents e WHERE e.id = :eventId")
    List<Long> findUserIdsByAcceptedEventId(@Param("eventId") Long eventId);
}
