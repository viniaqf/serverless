package com.example.serverless.serverless.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.serverless.serverless.entity.Itens;


public interface ItensRepository extends JpaRepository<Itens, Long> {
    
    Optional <Itens> findById (Long id);
}