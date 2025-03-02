package com.example.serverless.serverless.controller;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
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

import com.example.serverless.serverless.dto.ApiResponse;
import com.example.serverless.serverless.entity.Pedido;
import com.example.serverless.serverless.service.PedidoService;

@RestController
@RequestMapping("/api/pedidos")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;
    
    @GetMapping
    public ResponseEntity<ApiResponse<List<Pedido>>> getAll() {
        List<Pedido> pedidos = pedidoService.getAll();
        pedidos.forEach(pedido -> {
            if (pedido.getCliente() != null) {
                pedido.getCliente().setPassword(null);
            }
        });
        return ResponseEntity.ok(ApiResponse.success(pedidos));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Pedido>> getById(@PathVariable("id") final Long id) {
        Pedido pedido = pedidoService.findById(id);
        if (pedido == null) {
            return ResponseEntity.notFound().build();
        }
        if (pedido.getCliente() != null) {
            pedido.getCliente().setPassword(null);
        }
        return ResponseEntity.ok(ApiResponse.success(pedido));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Pedido>> criarPedido(@RequestBody Pedido pedido) {
        Pedido novoPedido = pedidoService.criarPedido(pedido);
        novoPedido.getCliente().setPassword(null);
        
        return ResponseEntity.created(getLocationUri(novoPedido.getId()))
            .body(ApiResponse.success(novoPedido));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<Pedido>> atualizar(
            @PathVariable("id") Long id,
            @RequestParam(required = false) Integer codStatus,
            @RequestBody(required = false) Pedido pedidoAtualizado) {
        
        Pedido pedidoResult = pedidoService.atualizaPedido(pedidoAtualizado, codStatus, id);
        if (pedidoResult.getCliente() != null) {
            pedidoResult.getCliente().setPassword(null);
        }
        
        return ResponseEntity.ok(ApiResponse.success(pedidoResult));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable("id") Long id) {
        pedidoService.deletaPedido(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    private URI getLocationUri(Long id) {
        return ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(id)
            .toUri();
    }
}