package com.example.serverless.serverless.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.example.serverless.serverless.entity.Itens;
import com.example.serverless.serverless.entity.Pedido;
import com.example.serverless.serverless.repository.ItensRepository;
import com.example.serverless.serverless.repository.PedidoRepository;

@Service
public class PedidoService {
    
@Autowired
private PedidoRepository pedidoRepository;

@Autowired 
private ItensRepository itensRepository;

    public Float calculaValorTotal(Pedido pedido) {
        Float valorTotal = 0f;
        for (Itens item : pedido.getItens()) {
            Itens itemBanco = itensRepository.findById(item.getNome()).orElseThrow( () -> new IllegalArgumentException("Item não encontrado:" + item.getNome()));

            valorTotal += itemBanco.getPreco() * itemBanco.getQuantidade();
        }
        return valorTotal;
    }

    public Pedido findById(Long id) {
        return pedidoRepository.findById(id).orElse(null);
    }

    public List<Pedido> getAll() {
        return pedidoRepository.findAll();
    }

    public Pedido criarPedido(Pedido pedido) {

        Assert.notNull(pedido.getCliente(), "O campo cliente é obrigatório");
        Assert.notNull(pedido.getItens(), "O campo itens é obrigatório");
        
        pedido.setTotal(calculaValorTotal(pedido));
        pedido.setStatus(Pedido.Status.PENDENTE);
        
        return pedidoRepository.save(pedido);
    }

    public Pedido atualizaPedido(Pedido pedido, Integer codStatus, Long id) {
        Assert.notNull(pedido.getId(), "O campo id é obrigatório");
        Assert.notNull(pedido.getCliente(), "O campo cliente é obrigatório");
        Assert.notNull(pedido.getItens(), "O campo itens é obrigatório");
        
        pedido.setTotal(calculaValorTotal(pedido));
        
        if (codStatus == 1) {
            pedido.setStatus(Pedido.Status.PROCESSANDO);
        } else if (codStatus == 2) {
            pedido.setStatus(Pedido.Status.ENVIADO);
        } else if (codStatus == 3) {
            pedido.setStatus(Pedido.Status.CANCELADO);
        }
        
        return pedidoRepository.save(pedido);
    }

    public void deletaPedido(Long id) {
        Pedido pedido = new Pedido();
        if (pedido.getStatus() != Pedido.Status.PENDENTE && pedido.getStatus() != Pedido.Status.PROCESSANDO) {
            throw new IllegalArgumentException("Não é possível deletar um pedido que não está pendente ou processando");
        }
        else{
            pedidoRepository.deleteById(id);
        }
    }
}