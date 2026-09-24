# Lista de Compras

Projeto desenvolvido em Java com duas versões: uma para rodar no terminal e outra como aplicação web com API REST.

---

## Versões do Projeto

### v1 — Console (`src/`)

Aplicação de terminal interativa. O usuário navega por um menu para gerenciar os itens da lista.

**Funcionalidades:**
- Adicionar item (nome, quantidade, preço)
- Visualizar todos os itens
- Remover item
- Calcular custo total
- Salvar e carregar dados em arquivo `.txt`

**Como rodar:**
```bash
javac -d out src/ItemDeCompra.java src/ListaDeComprasApp.java
java -cp out ListaDeComprasApp
```

---

### v2 — Web (`ListaDeComprasWeb/`)

API REST construída com Spring Boot. Inclui um frontend em HTML + JavaScript acessível pelo navegador.

**Tecnologias utilizadas:**
- Java 17
- Spring Boot 3.2.5
- Spring Data JPA
- Banco de dados H2 (em memória)
- HTML + CSS + JavaScript (fetch API)

**Funcionalidades:**
- Adicionar item via formulário web
- Visualizar lista com custo por item
- Remover item
- Calcular e exibir o custo total
- Console do banco H2 disponível em `/h2-console`

**Endpoints da API:**

| Método | URL | Descrição |
|--------|-----|-----------|
| GET | `/itens` | Lista todos os itens |
| GET | `/itens/{id}` | Busca um item pelo ID |
| POST | `/itens` | Adiciona um novo item |
| PUT | `/itens/{id}` | Atualiza um item existente |
| DELETE | `/itens/{id}` | Remove um item |
| GET | `/itens/total` | Retorna o custo total |

**Como rodar:**
```bash
cd ListaDeComprasWeb
mvn spring-boot:run
```

Acesse no navegador: [http://localhost:8080](http://localhost:8080)

---

## Estrutura do Repositório

```
├── src/                          # Versão console
│   ├── ItemDeCompra.java
│   └── ListaDeComprasApp.java
├── lista_de_compras.txt          # Arquivo de dados da versão console
└── ListaDeComprasWeb/            # Versão web
    ├── pom.xml
    ├── GUIA_DE_ESTUDOS.md        # Documentação didática da migração
    └── src/main/java/com/listadecompras/
        ├── ListaDeComprasApplication.java
        ├── config/WebConfig.java
        ├── controller/ItemDeCompraController.java
        ├── model/ItemDeCompra.java
        ├── repository/ItemDeCompraRepository.java
        └── service/ItemDeCompraService.java
```

---

## Pré-requisitos

- Java 17 ou superior
- Maven (para a versão web)

---

## Próximos passos

- Substituir o banco H2 por PostgreSQL para persistência real
- Criar frontend em React
- Adicionar autenticação de usuário
