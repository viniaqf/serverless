package com.example.serverless.serverless.entity;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@Table(name = "pedidos", schema = "public")
@Entity
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private User cliente;
    
    private User email;

    private List<Itens> itens;
    
    private Float total;

    private Status status;

    private LocalDateTime data_criacao;
    
    private LocalDateTime data_atualizacao;

    public enum Status {
        PENDENTE, PROCESSANDO, ENVIADO, CANCELADO;
    }


    public LocalDateTime setDataCriacao() {
        return LocalDateTime.now();
    }

    public LocalDateTime setDataAtualizacao() {
        return LocalDateTime.now();
    }
}
