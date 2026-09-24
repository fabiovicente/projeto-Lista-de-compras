package com.listadecompras.service;

import com.listadecompras.model.ItemDeCompra;
import com.listadecompras.repository.ItemDeCompraRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * O Service é a camada de LÓGICA DE NEGÓCIO da aplicação.
 *
 * Ele fica entre o Controller (que recebe as requisições HTTP)
 * e o Repository (que acessa o banco de dados).
 *
 * Por que ter essa camada separada?
 * - Organização: cada camada tem uma responsabilidade clara.
 * - Reutilização: a lógica pode ser usada por múltiplos controllers.
 * - Testabilidade: fica mais fácil testar a lógica isoladamente.
 *
 * Comparação com a versão console:
 * - Antes: a lógica estava toda espalhada nos métodos estáticos do ListaDeComprasApp
 *          (adicionarItem, removerItem, calcularCustoTotal, etc.).
 * - Agora: essa mesma lógica vive aqui no Service, de forma organizada.
 */
@Service
public class ItemDeCompraService {

    // O Spring injeta automaticamente o Repository aqui (Injeção de Dependência)
    private final ItemDeCompraRepository repository;

    public ItemDeCompraService(ItemDeCompraRepository repository) {
        this.repository = repository;
    }

    /**
     * Retorna todos os itens da lista.
     * Equivalente ao método visualizarLista() da versão console.
     */
    public List<ItemDeCompra> listarTodos() {
        return repository.findAll();
    }

    /**
     * Busca um item pelo ID. Retorna Optional para forçar o tratamento
     * do caso em que o item não existe (evita NullPointerException).
     */
    public Optional<ItemDeCompra> buscarPorId(Long id) {
        return repository.findById(id);
    }

    /**
     * Adiciona um novo item à lista (salva no banco).
     * Equivalente ao método adicionarItem() da versão console.
     */
    public ItemDeCompra adicionar(ItemDeCompra item) {
        return repository.save(item);
    }

    /**
     * Remove um item pelo ID.
     * Equivalente ao método removerItem() da versão console.
     * Lança uma exceção se o item não for encontrado.
     */
    public void remover(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Item com ID " + id + " não encontrado.");
        }
        repository.deleteById(id);
    }

    /**
     * Calcula o custo total de todos os itens da lista.
     * Equivalente ao método calcularCustoTotal() da versão console.
     * Usa Stream API: uma forma moderna e elegante de processar coleções.
     */
    public double calcularCustoTotal() {
        return repository.findAll()
                .stream()
                // Para cada item, calcula quantidade * preço
                .mapToDouble(item -> item.getQuantidade() * item.getPreco())
                // Soma todos os valores
                .sum();
    }

    /**
     * Atualiza os dados de um item existente.
     * Operação nova — não existia na versão console.
     */
    public ItemDeCompra atualizar(Long id, ItemDeCompra dadosNovos) {
        ItemDeCompra itemExistente = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item com ID " + id + " não encontrado."));

        itemExistente.setNome(dadosNovos.getNome());
        itemExistente.setQuantidade(dadosNovos.getQuantidade());
        itemExistente.setPreco(dadosNovos.getPreco());

        return repository.save(itemExistente);
    }
}
