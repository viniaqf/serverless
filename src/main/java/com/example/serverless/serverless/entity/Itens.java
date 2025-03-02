package com.example.serverless.serverless.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@Table(name = "itens", schema = "public")
public class Itens {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nome;
    
    @Column(nullable = false)    
    private Float preco;
    
    @Column(nullable = false)
    private Integer estoque;

    // Remover este campo pois agora está no ItemPedidoEmbeddable
    // @Transient
    // private Integer quantidade;
    
    @Column(nullable = true)
    private String descricao;
}
