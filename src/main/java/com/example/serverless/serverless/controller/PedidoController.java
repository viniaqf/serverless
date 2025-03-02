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
        return ResponseEntity.ok(null);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pedido> getById(@PathVariable("id") final Long id) {
        return ResponseEntity.ok(null);
    }

    @PostMapping()
    public ResponseEntity<Pedido> post(@RequestBody Pedido pedido) {
        Pedido createdPedido = pedidoService.criarPedido(pedido);
        URI location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(createdPedido.getId())
            .toUri();
        return ResponseEntity.created(location).body(createdPedido);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<String> atualizar(@PathVariable("id") @RequestBody Pedido pedido, Integer codStatus, Long id) {
        pedidoService.atualizaPedido(pedido, codStatus, id);
        return ResponseEntity.ok("Pedido atualizado com sucesso");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        pedidoService.deletaPedido(id);
        return ResponseEntity.noContent().build();
    }
}