package com.listadecompras.repository;

import com.listadecompras.model.ItemDeCompra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * O Repository é a camada responsável por FALAR COM O BANCO DE DADOS.
 *
 * Ao estender JpaRepository<ItemDeCompra, Long>, ganhamos de graça:
 *   - save(item)         → INSERT ou UPDATE no banco
 *   - findById(id)       → SELECT por ID
 *   - findAll()          → SELECT * (busca todos)
 *   - deleteById(id)     → DELETE por ID
 *   - existsById(id)     → verifica se um registro existe
 *
 * Não precisamos escrever nenhum SQL para essas operações básicas!
 *
 * Comparação com a versão console:
 * - Antes: os dados ficavam em um ArrayList<ItemDeCompra> na memória.
 *          Ao fechar o programa, tudo era perdido (exceto o que salvávamos no .txt).
 * - Agora: o JPA salva os dados num banco de dados real.
 *          Os dados persistem entre execuções da aplicação.
 */
@Repository
public interface ItemDeCompraRepository extends JpaRepository<ItemDeCompra, Long> {
    // Nenhum método extra necessário por enquanto.
    // O JpaRepository já fornece tudo que precisamos.
}
