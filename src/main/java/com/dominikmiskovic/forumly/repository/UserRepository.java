package com.dominikmiskovic.forumly.repository;

import com.dominikmiskovic.forumly.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    // TODO: might also consider (for login/registration checks):
    // boolean existsByUsername(String username);
}
