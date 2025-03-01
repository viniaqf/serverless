package com.example.serverless.serverless.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.serverless.serverless.entity.Itens;

public interface ItensRepository extends JpaRepository<Itens, String> {
    
}