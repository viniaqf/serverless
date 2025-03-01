package com.example.serverless.serverless.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Entity
@AllArgsConstructor
@Getter @Setter
@Table(name = "itens", schema = "public")
public class Itens {
    @Id
    private String nome;
    private String descricao;
    private Float preco;
    private Integer quantidade; 
}
