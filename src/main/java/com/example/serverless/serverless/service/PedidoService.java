package com.example.serverless.serverless.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.example.serverless.serverless.entity.ItemPedidoEmbeddable;
import com.example.serverless.serverless.entity.Itens;
import com.example.serverless.serverless.entity.Pedido;
import com.example.serverless.serverless.entity.User;
import com.example.serverless.serverless.repository.ItensRepository;
import com.example.serverless.serverless.repository.PedidoRepository;
import com.example.serverless.serverless.repository.UserRepository;

@Service
public class PedidoService {
    
@Autowired
private PedidoRepository pedidoRepository;

@Autowired 
private ItensRepository itensRepository;

@Autowired
private UserRepository userRepository;

    public Float calculaValorTotal(Pedido pedido) {
        Float valorTotal = 0f;

        for (ItemPedidoEmbeddable itemPedido : pedido.getItens()) {
            Assert.notNull(itemPedido.getProduto(), "O produto não pode ser nulo");
            Assert.notNull(itemPedido.getQuantidade(), "A quantidade não pode ser nula");

            
            Itens produtoBanco = itensRepository.findById(itemPedido.getProduto().getId())
                .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado: " + itemPedido.getProduto().getId()));
            
            Assert.notNull(produtoBanco.getEstoque(), "Estoque do produto não pode ser nulo: " + produtoBanco.getNome());

            if (produtoBanco.getEstoque() < itemPedido.getQuantidade()) {
                throw new IllegalArgumentException("Estoque insuficiente para o produto: " + produtoBanco.getNome());
            }

            valorTotal += produtoBanco.getPreco() * itemPedido.getQuantidade();
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
        
        Assert.notNull(pedido.getCliente(), "O cliente não pode ser nulo");
        Assert.notNull(pedido.getCliente().getId(), "O ID do cliente não pode ser nulo");
        Assert.notNull(pedido.getItens(), "A lista de itens não pode ser nula");
        
        
        User cliente = userRepository.findById(pedido.getCliente().getId())
            .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado"));
        pedido.setCliente(cliente);

        
        List<ItemPedidoEmbeddable> itensProcessados = new ArrayList<>();
        
        for (ItemPedidoEmbeddable itemPedido : pedido.getItens()) {
            Assert.notNull(itemPedido.getProduto(), "O produto não pode ser nulo");
            Assert.notNull(itemPedido.getProduto().getId(), "O ID do produto não pode ser nulo");
            Assert.notNull(itemPedido.getQuantidade(), "A quantidade não pode ser nula");
            
            
            Itens itemBanco = itensRepository.findById(itemPedido.getProduto().getId())
                .orElseThrow(() -> new IllegalArgumentException("Item não encontrado: " + itemPedido.getProduto().getId()));
            
            
            if (itemBanco.getEstoque() < itemPedido.getQuantidade()) {
                throw new IllegalArgumentException("Estoque insuficiente para: " + itemBanco.getNome());
            }
            
            
            itemBanco.setEstoque(itemBanco.getEstoque() - itemPedido.getQuantidade());
            itensRepository.save(itemBanco);
            
            
            ItemPedidoEmbeddable novoItemPedido = new ItemPedidoEmbeddable();
            novoItemPedido.setProduto(itemBanco);
            novoItemPedido.setQuantidade(itemPedido.getQuantidade());
            itensProcessados.add(novoItemPedido);
        }
        
       
        pedido.setItens(itensProcessados);
        pedido.setTotal(calculaValorTotal(pedido));
        pedido.setStatus(Pedido.Status.PENDENTE);
        
        return pedidoRepository.save(pedido);
    }

    public Pedido atualizaPedido(Pedido pedido, Integer codStatus, Long id) {
        Pedido pedidoExistente = pedidoRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Pedido não encontrado"));
        
        Assert.notNull(codStatus, "O código do status é obrigatório");
        
        // Atualiza apenas o status
        if (codStatus == 1) {
            pedidoExistente.setStatus(Pedido.Status.PROCESSANDO);
        } else if (codStatus == 2) {
            pedidoExistente.setStatus(Pedido.Status.ENVIADO);
        } else if (codStatus == 3) {
            pedidoExistente.setStatus(Pedido.Status.CANCELADO);
            // Se cancelado, devolve os itens ao estoque
            for (ItemPedidoEmbeddable item : pedidoExistente.getItens()) {
                Itens itemBanco = itensRepository.findById(item.getProduto().getId()).get();
                itemBanco.setEstoque(itemBanco.getEstoque() + item.getQuantidade());
                itensRepository.save(itemBanco);
            }
        } else {
            throw new IllegalArgumentException("Status inválido");
        }
        
        return pedidoRepository.save(pedidoExistente);
    }

    public void deletaPedido(Long id) {
        Pedido pedido = pedidoRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Pedido não encontrado"));
            
        if (pedido.getStatus() != Pedido.Status.PENDENTE && 
            pedido.getStatus() != Pedido.Status.PROCESSANDO) {
            throw new IllegalArgumentException(
                "Não é possível deletar um pedido que não está pendente ou processando");
        }
        
        pedidoRepository.deleteById(id);
    }
}