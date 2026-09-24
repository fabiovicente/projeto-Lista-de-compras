package com.listadecompras.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

/**
 * @Entity diz ao Spring que esta classe representa uma TABELA no banco de dados.
 * O JPA vai criar automaticamente a tabela "item_de_compra" com base nos campos abaixo.
 *
 * Diferença em relação à versão console:
 * - Antes: a classe era só um "molde" para guardar dados na memória (ArrayList).
 * - Agora: a classe é mapeada para uma tabela real no banco de dados.
 */
@Entity
public class ItemDeCompra {

    /**
     * @Id marca este campo como a CHAVE PRIMÁRIA da tabela (identificador único).
     * @GeneratedValue diz que o banco vai gerar o ID automaticamente (auto-incremento).
     * Antes não precisávamos de ID porque usávamos o índice do ArrayList (0, 1, 2...).
     * No banco de dados, cada linha precisa de um identificador único.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * @NotBlank: validação — o nome não pode ser nulo nem vazio.
     * O Spring vai rejeitar a requisição automaticamente se esta regra for violada.
     */
    @NotBlank(message = "O nome do item não pode ser vazio.")
    private String nome;

    /**
     * @Min(1): validação — a quantidade mínima é 1.
     */
    @Min(value = 1, message = "A quantidade deve ser no mínimo 1.")
    private int quantidade;

    /**
     * @Positive: validação — o preço deve ser maior que zero.
     */
    @Positive(message = "O preço deve ser um valor positivo.")
    private double preco;

    // Construtor padrão (sem argumentos) — obrigatório para o JPA funcionar
    public ItemDeCompra() {}

    // Construtor com argumentos — conveniente para criar objetos no código
    public ItemDeCompra(String nome, int quantidade, double preco) {
        this.nome = nome;
        this.quantidade = quantidade;
        this.preco = preco;
    }

    // --- Getters e Setters ---
    // Setters são necessários agora pois o JPA precisa preencher os campos ao carregar do banco

    public Long getId() { return id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public int getQuantidade() { return quantidade; }
    public void setQuantidade(int quantidade) { this.quantidade = quantidade; }

    public double getPreco() { return preco; }
    public void setPreco(double preco) { this.preco = preco; }
}
