import java.util.ArrayList;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;

public class ListaDeComprasApp {

    // Método para salvar a lista em um arquivo
    public static void salvarListaNoArquivo(ArrayList<ItemDeCompra> lista) {
        String nomeDoArquivo = "lista_de_compras.txt";

        try (PrintWriter writer = new PrintWriter(new FileWriter(nomeDoArquivo))) {
            // Itera sobre cada objeto ItemDeCompra da lista
            for (ItemDeCompra item : lista) {
                // Escreve os atributos do item no arquivo, separados por vírgula
                writer.println(item.getNome() + "," + item.getQuantidade() + "," + item.getPreco());
            }
            System.out.println("Lista de compras salva com sucesso em '" + nomeDoArquivo + "'.");
        } catch (IOException e) {
            System.out.println("Erro ao salvar a lista no arquivo: " + e.getMessage());
        }
    }

    // Método para carregar a lista de um arquivo
    public static void carregarListaDoArquivo(ArrayList<ItemDeCompra> lista) {
        String nomeDoArquivo = "lista_de_compras.txt";

        try (BufferedReader reader = new BufferedReader(new FileReader(nomeDoArquivo))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                // Divide a linha nos pontos onde há vírgula
                String[] partes = linha.split(",");
                // Verifica se a linha tem o formato esperado (nome,quantidade,preço)
                if (partes.length == 3) {
                    String nome = partes[0];
                    int quantidade = Integer.parseInt(partes[1]);
                    double preco = Double.parseDouble(partes[2]);

                    // Cria um novo objeto ItemDeCompra e adiciona à lista
                    ItemDeCompra item = new ItemDeCompra(nome, quantidade, preco);
                    lista.add(item);
                }
            }
            System.out.println("Lista de compras carregada com sucesso do arquivo '" + nomeDoArquivo + "'.");
        } catch (FileNotFoundException e) {
            System.out.println("Arquivo de lista de compras não encontrado. Criando uma nova lista.");
        } catch (IOException e) {
            System.out.println("Erro ao carregar a lista do arquivo: " + e.getMessage());
        } catch (NumberFormatException e) {
            // Adiciona um tratamento para caso o formato do número no arquivo esteja incorreto
            System.out.println("Erro de formato de número no arquivo: " + e.getMessage());
        }
    }

    // Método principal: O ponto de entrada do programa
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ArrayList<ItemDeCompra> listaDeCompras = new ArrayList<>();
        carregarListaDoArquivo(listaDeCompras);
        int opcao;


        do {
            exibirMenu();
            try {
                opcao = scanner.nextInt();
                scanner.nextLine();
            } catch (java.util.InputMismatchException e) {
                System.out.println("Opção inválida! Por favor, digite um número.");
                scanner.nextLine();
                opcao = -1;
                continue;
            }

            switch (opcao) {
                case 1:
                    adicionarItem(scanner, listaDeCompras);
                    break;
                case 2:
                    visualizarLista(listaDeCompras);
                    break;
                case 3:
                    removerItem(scanner, listaDeCompras);
                    break;
                case 4:
                    calcularCustoTotal(listaDeCompras);
                    break;

                case 0:
                    salvarListaNoArquivo(listaDeCompras); // Adicione esta linha
                    System.out.println("Saindo do programa. Até mais!");
                    break;
                default:
                    System.out.println("Opção inválida. Por favor, escolha uma opção válida.");
            }

        } while (opcao != 0);

        scanner.close();
    }

    // Método para calcular o custo total da lista de compras
    public static void calcularCustoTotal(ArrayList<ItemDeCompra> lista) {
        if (lista.isEmpty()) {
            System.out.println("A lista de compras está vazia. O custo total é R$0,00.");
            return;
        }

        double custoTotal = 0.0;

        // O loop 'for-each' é ideal para este caso
        for (ItemDeCompra item : lista) {
            // Multiplica a quantidade pelo preço e adiciona ao custo total
            custoTotal += item.getQuantidade() * item.getPreco();
        }

        // Formata e exibe o custo total
        System.out.printf("\nO custo total da sua lista de compras é de R$%.2f\n", custoTotal);
    }

    // --- Métodos de Organização do Código ---

    // Método para exibir o menu de opções
    public static void exibirMenu() {
        System.out.println("\n--- Lista de Compras ---");
        System.out.println("1. Adicionar item");
        System.out.println("2. Visualizar lista");
        System.out.println("3. Remover item");
        System.out.println("4. calcular custo total");
        System.out.println("0. Sair");
        System.out.print("Escolha uma opção: ");
    }

    // Método para adicionar um item à lista
    public static void adicionarItem(Scanner scanner, ArrayList<ItemDeCompra> lista) {
        System.out.print("Digite o nome do item a ser adicionado: ");
        String nome = scanner.nextLine();

        System.out.print("Digite a quantidade: ");
        int quantidade = 0;
        try {
            quantidade = scanner.nextInt();
        } catch (java.util.InputMismatchException e) {
            System.out.println("Entrada inválida. A quantidade deve ser um número inteiro.");
            scanner.nextLine(); // Limpa o buffer
            return; // Sai do método
        }

        System.out.print("Digite o preço (ex: 12,50): ");
        double preco = 0.0;
        try {
            preco = scanner.nextDouble();
        } catch (java.util.InputMismatchException e) {
            System.out.println("Entrada inválida. O preço deve ser um número decimal.");
            scanner.nextLine(); // Limpa o buffer
            return; // Sai do método
        }
        scanner.nextLine(); // Consome a quebra de linha

        // Criamos um novo objeto ItemDeCompra com os dados coletados
        ItemDeCompra novoItem = new ItemDeCompra(nome, quantidade, preco);
        // Adicionamos o novo objeto à lista
        lista.add(novoItem);

        System.out.println("'" + nome + "' foi adicionado à lista.");
    }

    // Método para visualizar todos os itens da lista
    public static void visualizarLista(ArrayList<ItemDeCompra> lista) {
        System.out.println("\n--- ITENS NA LISTA ---");
        if (lista.isEmpty()) {
            System.out.println("A lista de compras está vazia.");
        } else {
            // O loop 'for' percorre cada objeto ItemDeCompra da lista
            for (int i = 0; i < lista.size(); i++) {
                // Pegamos o objeto ItemDeCompra na posição 'i'
                ItemDeCompra item = lista.get(i);
                // Exibimos o número (i+1) e o objeto, que automaticamente chama o toString()
                System.out.println((i + 1) + ". " + item);
            }
        }
    }


    // Método para remover um item da lista
    public static void removerItem(Scanner scanner, ArrayList<ItemDeCompra> lista) {
        if (lista.isEmpty()) {
            System.out.println("A lista está vazia. Não há itens para remover.");
            return;
        }


        visualizarLista(lista);

        System.out.print("Digite o número do item a ser removido: ");
        try {
            int numeroDoItem = scanner.nextInt();
            scanner.nextLine();

            int indice = numeroDoItem - 1;

            if (indice >= 0 && indice < lista.size()) {
                ItemDeCompra itemRemovido = lista.remove(indice);
                System.out.println("'" + itemRemovido.getNome() + "' foi removido da lista.");
            } else {
                System.out.println("Número do item inválido. Por favor, digite um número da lista.");
            }
        } catch (java.util.InputMismatchException e) {
            System.out.println("Entrada inválida. Por favor, digite um número.");
            scanner.nextLine();
        }

    }

}


