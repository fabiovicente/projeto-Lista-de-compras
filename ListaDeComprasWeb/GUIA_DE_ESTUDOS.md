# Guia de Estudos: Da Aplicação Console para a Web com Spring Boot

Este documento explica, passo a passo, como a aplicação de Lista de Compras foi transformada
de um programa de terminal em uma API REST web. Use-o como material de estudo.

---

## O que mudou e por quê

| Versão Console | Versão Web (Spring Boot) |
|---|---|
| Usuário interage pelo terminal | Usuário (ou app) faz requisições HTTP |
| Dados ficam no ArrayList (memória) | Dados ficam no banco de dados H2 |
| Dados salvos em arquivo `.txt` | Banco de dados persiste os dados |
| Tudo em uma classe (`ListaDeComprasApp`) | Responsabilidades separadas em camadas |

---

## A Arquitetura em Camadas

A versão web segue o padrão **MVC adaptado para APIs REST**, com 3 camadas principais:

```
Requisição HTTP
      ↓
  CONTROLLER         ← recebe a requisição, chama o Service, devolve a resposta
      ↓
   SERVICE           ← contém a lógica de negócio (regras da aplicação)
      ↓
  REPOSITORY         ← fala com o banco de dados
      ↓
  BANCO DE DADOS (H2)
```

Cada camada tem **uma responsabilidade única**. Isso facilita a manutenção e os testes.

---

## Arquivo por Arquivo

### 1. `pom.xml` — O Gerenciador de Dependências

O `pom.xml` é o arquivo de configuração do **Maven**, a ferramenta que gerencia as
dependências (bibliotecas externas) do projeto.

**Dependências adicionadas:**

```xml
<!-- Spring Web: cria endpoints HTTP -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>

<!-- Spring Data JPA: acesso ao banco de dados -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>

<!-- H2: banco de dados em memória para desenvolvimento -->
<dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
    <scope>runtime</scope>
</dependency>

<!-- Validation: validações com anotações -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-validation</artifactId>
</dependency>
```

**Analogia:** o `pom.xml` é como um carrinho de compras. Você lista o que precisa,
e o Maven vai buscar tudo automaticamente na internet.

---

### 2. `ListaDeComprasApplication.java` — O Ponto de Entrada

```java
@SpringBootApplication
public class ListaDeComprasApplication {
    public static void main(String[] args) {
        SpringApplication.run(ListaDeComprasApplication.class, args);
    }
}
```

O `main()` ainda existe, mas agora ele só **inicia o servidor**. Depois disso,
a aplicação fica rodando e esperando por requisições HTTP na porta 8080.

A anotação `@SpringBootApplication` faz 3 coisas de uma vez:
- Configura a classe como fonte de configuração Spring
- Ativa a configuração automática (Spring Boot detecta as dependências e configura tudo)
- Faz o Spring procurar por Controllers, Services e Repositories no projeto

---

### 3. `ItemDeCompra.java` (model) — A Entidade JPA

**Antes (versão console):** era um POJO simples (Plain Old Java Object), apenas
com atributos, construtor e getters.

**Agora:** continua sendo um POJO, mas com **anotações JPA** que mapeiam a classe
para uma tabela no banco de dados.

```java
@Entity                          // esta classe = uma tabela no banco
public class ItemDeCompra {

    @Id                          // este campo = chave primária da tabela
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // auto-incremento
    private Long id;             // NOVO: antes não precisávamos de ID

    @NotBlank(message = "O nome não pode ser vazio.")
    private String nome;

    @Min(value = 1, message = "Quantidade mínima: 1")
    private int quantidade;

    @Positive(message = "O preço deve ser positivo.")
    private double preco;

    public ItemDeCompra() {}    // NOVO: construtor vazio obrigatório para o JPA
    // ... getters e setters ...
}
```

**Por que precisa de ID agora?**
No ArrayList, usávamos o índice (0, 1, 2...) para identificar os itens.
No banco de dados, cada linha precisa de um identificador único permanente.

**Por que precisa de construtor vazio?**
O JPA cria os objetos quando lê do banco. Ele precisa do construtor sem argumentos
para instanciar o objeto e depois preencher os campos um por um via setters.

---

### 4. `ItemDeCompraRepository.java` — O Acesso ao Banco

```java
@Repository
public interface ItemDeCompraRepository extends JpaRepository<ItemDeCompra, Long> {
    // Nenhum método para escrever!
}
```

Esta é uma das partes mais "mágicas" do Spring Data JPA.

Ao estender `JpaRepository<ItemDeCompra, Long>`, ganhamos automaticamente:

| Método | SQL equivalente |
|---|---|
| `save(item)` | `INSERT INTO ...` ou `UPDATE ...` |
| `findAll()` | `SELECT * FROM item_de_compra` |
| `findById(id)` | `SELECT * FROM item_de_compra WHERE id = ?` |
| `deleteById(id)` | `DELETE FROM item_de_compra WHERE id = ?` |
| `existsById(id)` | `SELECT COUNT(*) FROM ... WHERE id = ?` |

**Analogia:** é como contratar um assistente que já sabe fazer todas as operações
básicas de banco de dados. Você só precisa pedir.

O `<ItemDeCompra, Long>` diz:
- `ItemDeCompra` → qual entidade este repository gerencia
- `Long` → o tipo do ID daquela entidade

---

### 5. `ItemDeCompraService.java` — A Lógica de Negócio

O Service é onde vivem as **regras da aplicação**. Compare com a versão console:

| Versão Console (método estático) | Versão Web (método do Service) |
|---|---|
| `adicionarItem(scanner, lista)` | `adicionar(item)` |
| `visualizarLista(lista)` | `listarTodos()` |
| `removerItem(scanner, lista)` | `remover(id)` |
| `calcularCustoTotal(lista)` | `calcularCustoTotal()` |
| *(não existia)* | `atualizar(id, dadosNovos)` |

**Exemplo: calcularCustoTotal() com Stream API**

```java
// Versão console (loop tradicional):
double custoTotal = 0.0;
for (ItemDeCompra item : lista) {
    custoTotal += item.getQuantidade() * item.getPreco();
}

// Versão web (Stream API — forma moderna):
return repository.findAll()
    .stream()
    .mapToDouble(item -> item.getQuantidade() * item.getPreco())
    .sum();
```

Ambas fazem a mesma coisa. A Stream API é mais concisa e expressiva.

**Injeção de Dependência:**

```java
@Service
public class ItemDeCompraService {

    private final ItemDeCompraRepository repository;

    // Spring injeta o Repository automaticamente aqui
    public ItemDeCompraService(ItemDeCompraRepository repository) {
        this.repository = repository;
    }
}
```

O Spring cuida de criar e fornecer o `ItemDeCompraRepository`. Você não precisa
escrever `new ItemDeCompraRepository()` — o framework faz isso por você.
Isso é chamado de **Injeção de Dependência** (Dependency Injection).

---

### 6. `ItemDeCompraController.java` — Os Endpoints HTTP

O Controller substitui o menu interativo do terminal. Cada opção do menu virou
um **endpoint HTTP** com um método (verbo) e uma URL:

| Menu Console | Verbo HTTP | URL | Descrição |
|---|---|---|---|
| 1. Adicionar item | `POST` | `/itens` | Cria um novo item |
| 2. Visualizar lista | `GET` | `/itens` | Lista todos os itens |
| *(busca por ID)* | `GET` | `/itens/{id}` | Busca um item específico |
| 3. Remover item | `DELETE` | `/itens/{id}` | Remove um item pelo ID |
| 4. Custo total | `GET` | `/itens/total` | Retorna o custo total |
| *(não existia)* | `PUT` | `/itens/{id}` | Atualiza um item |

**Os verbos HTTP têm significado semântico:**
- `GET` → apenas lê, não modifica nada
- `POST` → cria um novo recurso
- `PUT` → atualiza um recurso existente
- `DELETE` → remove um recurso

**Exemplo de endpoint:**

```java
@PostMapping                          // responde a POST /itens
public ResponseEntity<ItemDeCompra> adicionar(
        @Valid @RequestBody ItemDeCompra item) {  // lê o JSON do corpo da requisição
    ItemDeCompra salvo = service.adicionar(item);
    return ResponseEntity.status(HttpStatus.CREATED).body(salvo);  // 201 Created
}
```

**Códigos de status HTTP comuns:**

| Código | Significado | Quando usar |
|---|---|---|
| 200 OK | Sucesso com corpo | GET, PUT bem-sucedidos |
| 201 Created | Recurso criado | POST bem-sucedido |
| 204 No Content | Sucesso sem corpo | DELETE bem-sucedido |
| 404 Not Found | Não encontrado | ID inválido |
| 400 Bad Request | Dados inválidos | Falha de validação |

---

### 7. `application.properties` — Configuração da Aplicação

```properties
# Banco H2 em memória
spring.datasource.url=jdbc:h2:mem:listadecompras

# Console web do H2 (acesse em http://localhost:8080/h2-console)
spring.h2.console.enabled=true

# Exibe as queries SQL no terminal (ótimo para aprender!)
spring.jpa.show-sql=true

# Recria o banco a cada reinicialização (bom para estudos)
spring.jpa.hibernate.ddl-auto=create-drop
```

**Dica de estudo:** ative `show-sql=true` e observe as queries que o JPA gera
automaticamente quando você faz requisições. Isso ajuda a entender o que está
acontecendo por baixo dos panos.

---

## Como Testar a API

### Com o console H2
Acesse `http://localhost:8080/h2-console` com a JDBC URL `jdbc:h2:mem:listadecompras`
para visualizar e consultar o banco diretamente pelo navegador.

### Com curl (terminal)

**Adicionar um item:**
```bash
curl -X POST http://localhost:8080/itens \
  -H "Content-Type: application/json" \
  -d '{"nome":"Arroz","quantidade":2,"preco":8.90}'
```

**Listar todos os itens:**
```bash
curl http://localhost:8080/itens
```

**Calcular custo total:**
```bash
curl http://localhost:8080/itens/total
```

**Remover item de ID 1:**
```bash
curl -X DELETE http://localhost:8080/itens/1
```

### Com o Postman ou Insomnia
Ferramentas com interface gráfica para testar APIs. Recomendadas para quem está
aprendendo — permitem visualizar requisições e respostas de forma clara.

---

## Como Rodar o Projeto

### Pré-requisitos
- Java 17 ou superior
- Maven instalado (`mvn -version` para verificar)

### Comandos

```bash
# Entrar na pasta do projeto
cd /home/desktop/IdeaProjects/ListaDeComprasWeb

# Compilar e baixar dependências
mvn clean install

# Rodar a aplicação
mvn spring-boot:run
```

A aplicação estará disponível em `http://localhost:8080`.

---

## Resumo: O que você aprendeu aqui

1. **Arquitetura em camadas** (Controller → Service → Repository)
2. **JPA e ORM**: mapear classes Java para tabelas do banco com anotações
3. **Spring Data JPA**: ganhar operações CRUD de graça com `JpaRepository`
4. **REST API**: usar verbos HTTP (GET, POST, PUT, DELETE) com significado semântico
5. **Injeção de Dependência**: o Spring cuida de criar e conectar os objetos
6. **Validação**: proteger a entrada de dados com `@NotBlank`, `@Min`, `@Positive`
7. **ResponseEntity**: controlar o código de status HTTP das respostas
8. **Stream API**: processar coleções de forma concisa e moderna

---

*Próximo passo sugerido: criar um frontend simples em HTML + JavaScript que consuma
esta API, ou evoluir para usar um banco de dados PostgreSQL em vez do H2.*
