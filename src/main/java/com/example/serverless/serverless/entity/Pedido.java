package com.example.serverless.serverless.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "pedidos", schema = "public")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private User cliente;

    @ElementCollection
    @CollectionTable(name = "pedido_itens", joinColumns = @JoinColumn(name = "pedido_id"))
    private List<ItemPedidoEmbeddable> itens = new ArrayList<>(); // Inicializar a lista
    
    @Column(nullable = false)
    private Float total;

    @Column(nullable = false)
    private Status status;

    @Column(nullable = true)
    private LocalDateTime data_criacao;
    
    @Column(nullable = true)
    private LocalDateTime data_atualizacao;

    @Column(nullable = false)
    private boolean isEdited;

    public enum Status {
        PENDENTE, PROCESSANDO, ENVIADO, CANCELADO;
    }

    @PrePersist
    public void setDataCriacao() {
        this.isEdited = false;
        this.data_criacao = LocalDateTime.now();
    }

    @PreUpdate
    public void setDataAtualizacao() {
        this.isEdited = false;
        this.data_atualizacao = LocalDateTime.now();
    }
}
