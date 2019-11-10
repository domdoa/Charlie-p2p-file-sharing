package com.filesharing.iot.repository;

import com.filesharing.iot.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(final String email);

    @Query(value = "SELECT u FROM User u WHERE u.user_id = ?1")
    User findByUserId(final long user_id);
}