package com.example.serverless.serverless.controller;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.serverless.serverless.entity.Pedido;
import com.example.serverless.serverless.service.PedidoService;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;
    
    @GetMapping()
    public ResponseEntity<List<Pedido>> getAll() {
        List<Pedido> pedidos = pedidoService.getAll();
        pedidos.forEach(pedido -> {
            if (pedido.getCliente() != null) {
                pedido.getCliente().setPassword(null);
            }
        });
        return ResponseEntity.ok().body(pedidos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pedido> getById(@PathVariable("id") final Long id) {
        Pedido pedido = pedidoService.findById(id);
        if (pedido.getCliente() != null) {
            pedido.getCliente().setPassword(null);
        }
        return ResponseEntity.ok(pedido);
    }

    @PostMapping
    public ResponseEntity<Pedido> criarPedido(@RequestBody Pedido pedido) {
        Pedido novoPedido = pedidoService.criarPedido(pedido);
        URI location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(novoPedido.getId())
            .toUri();
        
        // Limpa informações sensíveis ou desnecessárias antes de retornar
        novoPedido.getCliente().setPassword(null);
        
        return ResponseEntity.created(location).body(novoPedido);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Pedido> atualizar(
            @PathVariable("id") Long id,
            @RequestParam(required = false) Integer codStatus,
            @RequestBody(required = false) Pedido pedidoAtualizado) {
        
        
        Pedido pedidoExistente = pedidoService.findById(id);
        if (pedidoExistente == null) {
            return ResponseEntity.notFound().build();
        }
        
        
        Pedido pedidoResult = pedidoService.atualizaPedido(pedidoAtualizado, codStatus, id);
        
        
        if (pedidoResult.getCliente() != null) {
            pedidoResult.getCliente().setPassword(null);
        }
        
        return ResponseEntity.ok(pedidoResult);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        pedidoService.deletaPedido(id);
        return ResponseEntity.noContent().build();
    }
}