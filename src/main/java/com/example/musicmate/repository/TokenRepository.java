package com.example.musicmate.repository;



import com.example.musicmate.entity.Token;
import com.example.musicmate.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Integer> {
    Optional<Object> findByTokenAndUsers(Integer token, User user);

    Optional<Object> findByToken(Integer token);
}
