package com.example.serverless.serverless.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor

public class ItemPedidoEmbeddable {
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "produto_id", nullable = false)
    private Itens produto;

    @Column(nullable = false)
    private Integer quantidade;


}