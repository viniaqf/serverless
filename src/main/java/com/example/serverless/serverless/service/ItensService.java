package com.example.serverless.serverless.service;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.Assert;

import com.example.serverless.serverless.entity.Itens;
import com.example.serverless.serverless.entity.User;
import com.example.serverless.serverless.repository.ItensRepository;

import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;

@Service
@Slf4j
@RequiredArgsConstructor
public class ItensService {
    
    private final ItensRepository itensRepository;
    private final UserService userService;

    public List<Itens> findAll() {
        return itensRepository.findAll();
    }

    public Itens post(Itens item) {
        // Obtém o usuário autenticado atual
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = auth.getName();
        User currentUser = userService.findByEmail(userEmail);
        
        log.info("Iniciando a criação de um item pelo usuário: {} (ID: {})", 
            currentUser.getName(), currentUser.getId());

        try {
            Assert.notNull(item.getNome(), "O nome é obrigatório");
            Assert.notNull(item.getPreco(), "O preço é obrigatório");
            Assert.notNull(item.getEstoque(), "A quantidade em estoque é obrigatória");
            
            Itens savedItem = itensRepository.save(item);
            log.info("Item criado com sucesso. ID: {}", savedItem.getId());
            return savedItem;
        } catch (Exception e) {
            log.error("Erro ao criar item: {}", e.getMessage());
            throw e;
        }
    }

    public Itens atualizar (Itens item, Long id) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = auth.getName();
        User currentUser = userService.findByEmail(userEmail);

        log.info("Iniciando a atualização de um item pelo usuário: {} (ID: {})", 
            currentUser.getName(), currentUser.getId());
        try{
        Assert.notNull(item.getNome(), "O nome é obrigatório");
        Assert.notNull(item.getPreco(), "O preço é obrigatório");
        Assert.notNull(item.getEstoque(), "A quantidade em estoque é obrigatória");
        return itensRepository.save(item);
    }catch (Exception e) {
        log.error("Erro ao atualizar item: {}", e.getMessage());
        throw e;
    }  
    
}

    public void delete(Long id){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = auth.getName();
        User currentUser = userService.findByEmail(userEmail);
        
        log.info("Iniciando a criação de um item pelo usuário: {} (ID: {})", 
            currentUser.getName(), currentUser.getId());
        try{
            itensRepository.deleteById(id);
        }catch (Exception e) {
            log.error("Erro ao deletar item: {}", e.getMessage());
            throw e;
        }
        
    }
}