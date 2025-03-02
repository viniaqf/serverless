package com.example.serverless.serverless.controller;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.serverless.serverless.entity.Itens;
import com.example.serverless.serverless.service.ItensService;

@RestController
@RequestMapping("/api/itens")
public class ItensController {

    @Autowired 
    private ItensService itensService;

    @GetMapping()
    public ResponseEntity<List<Itens>> getAll() {
        return ResponseEntity.ok(itensService.findAll());
 
    }

    @PostMapping()
    public ResponseEntity<Itens> post(@RequestBody Itens item) {
        Itens createdItens = itensService.post(item);
        URI location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{nome}")
            .buildAndExpand(createdItens.getNome())
            .toUri();

        return ResponseEntity.created(location).body(createdItens);
    }

    @PatchMapping("{id}")
    public ResponseEntity<String> put(@PathVariable("id") @RequestBody Itens item, final String nome) {
        itensService.atualizar(item, nome);
        return ResponseEntity.ok("Item atualizado com sucesso");
    }

}