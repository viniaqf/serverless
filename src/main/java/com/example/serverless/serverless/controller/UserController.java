package com.example.serverless.serverless.controller;

import org.springframework.web.bind.annotation.RestController;

import com.example.serverless.serverless.entity.Pedido;
import com.example.serverless.serverless.entity.User;
import com.example.serverless.serverless.service.UserService;

import java.util.List;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;




@RestController
@RequestMapping("/api/user")
public class UserController {

@Autowired
private UserService userService;

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAll() {
        return ResponseEntity.ok(userService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getById(@PathVariable("id") final Long id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    @PostMapping()
    public ResponseEntity<User> post(@RequestBody User user) {
        User createdUser = userService.post(user);
        URI location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(createdUser.getId())
            .toUri();
        
        return ResponseEntity.created(location).body(createdUser);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<String> atualizar(@RequestBody User user, @PathVariable("id") final Long id) {
        userService.atualizar(user, id);
        return ResponseEntity.ok("Usuário atualizado com sucesso");
    }

    @DeleteMapping(" /{id}")
    public ResponseEntity<?> delete(@PathVariable("id") final Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }


}