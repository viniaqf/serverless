package com.example.serverless.serverless.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.example.serverless.serverless.entity.User;
import com.example.serverless.serverless.repository.PedidoRepository;
import com.example.serverless.serverless.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserService {
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PedidoRepository pedidoRepository;


    public User findById(Long id) {
        log.debug("Buscando usuário por ID: {}", id);
        return userRepository.findById(id)
            .orElseThrow(() -> {
                log.error("Usuário não encontrado com ID: {}", id);
                return new IllegalArgumentException("Usuário não encontrado");
            });
    }   

    public List<User> getAll() {
        log.debug("Buscando todos os usuários");
        List<User> users = userRepository.findAll();
        log.info("Encontrados {} usuários", users.size());
        return users;
    }

    public User post(User user) {
        Assert.notNull(user.getName(), "O nome é obrigatório");
        Assert.notNull(user.getEmail(), "O email é obrigatório");
        Assert.notNull(user.getPassword(), "A senha é obrigatória");
        Assert.notNull(user.getRole(), "O papel é obrigatório");
        log.info("Salvando novo usuário: {}", user.getEmail());
        try {
            User savedUser = userRepository.save(user);
            log.info("Usuário salvo com sucesso. ID: {}", savedUser.getId());
            return savedUser;
        } catch (Exception e) {
            log.error("Erro ao salvar usuário: {}", e.getMessage());
            throw e;
        }
    }

    public User atualizar(User user, Long id) {
        Assert.notNull(user.getId(), "O id é obrigatório");
        Assert.notNull(user.getName(), "O nome é obrigatório");
        Assert.notNull(user.getEmail(), "O email é obrigatório");
        Assert.notNull(user.getPassword(), "A senha é obrigatória");
        Assert.notNull(user.getRole(), "O papel é obrigatório");
        return userRepository.save(user);
    }

    public void delete(Long id) {
        log.info("Deletando usuário com ID: {}", id);
        try {
            var pedidos = pedidoRepository.findByClienteId(id);
        
            Assert.isTrue(pedidos.isEmpty(),  "Não é possível excluir o usuário, pois ele possui pedidos associados");
            userRepository.deleteById(id);
            log.info("Usuário deletado com sucesso");
        } catch (Exception e) {
            log.error("Erro ao deletar usuário: {}", e.getMessage());
            throw e;
        }
    }

    public User findByEmail(String email) {
        log.debug("Buscando usuário por email: {}", email);
        return userRepository.findByEmail(email)
            .orElseThrow(() -> {
                log.error("Usuário não encontrado com email: {}", email);
                return new IllegalArgumentException("Usuário não encontrado");
            });
    }
}