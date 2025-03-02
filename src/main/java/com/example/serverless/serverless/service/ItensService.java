package com.example.serverless.serverless.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.example.serverless.serverless.entity.Itens;
import com.example.serverless.serverless.repository.ItensRepository;

@Service
public class ItensService {
    
    @Autowired
    private ItensRepository itensRepository;

    public List<Itens> findAll() {
        return itensRepository.findAll();
    }

    public Itens post (Itens item) {
        Assert.notNull(item.getNome(), "O nome é obrigatório");
        Assert.notNull(item.getPreco(), "O preço é obrigatório");
        Assert.notNull(item.getQuantidade(), "A quantidade é obrigatória");
        return itensRepository.save(item);
    }

    public Itens atualizar (Itens item, String nome) {
        Assert.notNull(item.getNome(), "O nome é obrigatório");
        Assert.notNull(item.getPreco(), "O preço é obrigatório");
        Assert.notNull(item.getQuantidade(), "A quantidade é obrigatória");
        return itensRepository.save(item);
    }

    public void delete(String nome) {
        itensRepository.deleteById(nome);
    }
}