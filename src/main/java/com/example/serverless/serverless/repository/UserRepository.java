package com.example.serverless.serverless.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.serverless.serverless.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    Optional <User> findByEmail(String email);

    Optional <User> findByName (String name);

    Optional <User> findById (Long id);
    
}