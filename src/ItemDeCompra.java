public class ItemDeCompra {
    // Atributos (características) de um item de compra
    private String nome;
    private int quantidade;
    private double preco;

    // Construtor: usado para criar um novo objeto ItemDeCompra
    public ItemDeCompra(String nome, int quantidade, double preco) {
        this.nome = nome;
        this.quantidade = quantidade;
        this.preco = preco;
    }

    // Métodos Getters: permitem acessar o valor dos atributos
    public String getNome() {
        return nome;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public double getPreco() {
        return preco;
    }

    // Método toString: retorna uma representação em String do objeto
    @Override
    public String toString() {
        return String.format("%s (Quantidade: %d, Preço: R$%.2f)", nome, quantidade, preco);
    }
}

