package com.example.serverless.serverless.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter @Setter
@Table(name = "itens", schema = "public")
@Entity
public class Itens {
    
    private String nome;
    private String descricao;
    private Float preco;
    private Integer quantidade; 
}
