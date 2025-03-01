package com.example.serverless.serverless.entity;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
@AllArgsConstructor
@Table(name = "pedidos", schema = "public")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "cliente_id", nullable = false)
    private User cliente;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "pedido_id")
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
