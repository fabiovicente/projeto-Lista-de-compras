package com.listadecompras.controller;

import com.listadecompras.model.ItemDeCompra;
import com.listadecompras.service.ItemDeCompraService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * O Controller é a "porta de entrada" da aplicação.
 * Ele recebe as requisições HTTP, chama o Service e devolve a resposta.
 *
 * @RestController = @Controller + @ResponseBody
 *   Significa: "esta classe lida com requisições HTTP e retorna JSON automaticamente".
 *
 * @RequestMapping("/itens") define a URL base para todos os endpoints desta classe.
 * Todos os endpoints aqui começarão com /itens.
 *
 * Comparação com a versão console:
 * - Antes: o usuário interagia pelo terminal (Scanner lendo do System.in).
 * - Agora: o usuário (ou um app frontend) faz requisições HTTP para estes endpoints.
 */
@RestController
@RequestMapping("/itens")
public class ItemDeCompraController {

    private final ItemDeCompraService service;

    public ItemDeCompraController(ItemDeCompraService service) {
        this.service = service;
    }

    /**
     * GET /itens
     * Retorna todos os itens da lista em formato JSON.
     * Equivalente à opção "2. Visualizar lista" do menu console.
     *
     * HTTP 200 OK é retornado automaticamente pelo ResponseEntity.ok()
     */
    @GetMapping
    public ResponseEntity<List<ItemDeCompra>> listarTodos() {
        List<ItemDeCompra> itens = service.listarTodos();
        return ResponseEntity.ok(itens);
    }

    /**
     * GET /itens/{id}
     * Retorna um item específico pelo seu ID.
     * {id} é uma variável de caminho (path variable) — ex: GET /itens/1
     *
     * HTTP 200 OK se encontrado, HTTP 404 Not Found se não existir.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ItemDeCompra> buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)                          // se encontrou → 200 OK
                .orElse(ResponseEntity.notFound().build());       // se não achou → 404 Not Found
    }

    /**
     * POST /itens
     * Adiciona um novo item à lista.
     * Equivalente à opção "1. Adicionar item" do menu console.
     *
     * O corpo da requisição deve ser um JSON com nome, quantidade e preco.
     * @Valid ativa as validações definidas na entidade (@NotBlank, @Min, etc.)
     *
     * HTTP 201 Created indica que o recurso foi criado com sucesso.
     */
    @PostMapping
    public ResponseEntity<ItemDeCompra> adicionar(@Valid @RequestBody ItemDeCompra item) {
        ItemDeCompra salvo = service.adicionar(item);
        return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
    }

    /**
     * PUT /itens/{id}
     * Atualiza um item existente.
     * Operação nova — não existia na versão console.
     *
     * HTTP 200 OK se atualizado, HTTP 404 Not Found se não existir.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ItemDeCompra> atualizar(@PathVariable Long id,
                                                   @Valid @RequestBody ItemDeCompra dadosNovos) {
        try {
            ItemDeCompra atualizado = service.atualizar(id, dadosNovos);
            return ResponseEntity.ok(atualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * DELETE /itens/{id}
     * Remove um item pelo ID.
     * Equivalente à opção "3. Remover item" do menu console.
     *
     * HTTP 204 No Content: sucesso, mas sem corpo na resposta (padrão REST para DELETE).
     * HTTP 404 Not Found se o item não existir.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        try {
            service.remover(id);
            return ResponseEntity.noContent().build();  // 204 No Content
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();   // 404 Not Found
        }
    }

    /**
     * GET /itens/total
     * Retorna o custo total da lista de compras.
     * Equivalente à opção "4. Calcular custo total" do menu console.
     *
     * Retorna um JSON simples: { "total": 99.90 }
     */
    @GetMapping("/total")
    public ResponseEntity<Map<String, Double>> calcularTotal() {
        double total = service.calcularCustoTotal();
        return ResponseEntity.ok(Map.of("total", total));
    }
}
