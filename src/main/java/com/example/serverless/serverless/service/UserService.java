package com.example.serverless.serverless.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.example.serverless.serverless.entity.User;
import com.example.serverless.serverless.repository.PedidoRepository;
import com.example.serverless.serverless.repository.UserRepository;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PedidoRepository pedidoRepository;


    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }   

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public User post(User user) {
        Assert.notNull(user.getName(), "O nome é obrigatório");
        Assert.notNull(user.getEmail(), "O email é obrigatório");
        Assert.notNull(user.getPassword(), "A senha é obrigatória");
        Assert.notNull(user.getRole(), "O papel é obrigatório");
        return userRepository.save(user);
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

        var pedidos = pedidoRepository.findByClienteId(id);
        
        Assert.isTrue(pedidos.isEmpty(),  "Não é possível excluir o usuário, pois ele possui pedidos associados");
        userRepository.deleteById(id);
    }


}