package misc.lojavirtual;

public class Produto {
	Float preco;
	private String nome;

	public Produto(String nome, float preco) {
		this.preco = preco;
		this.nome = nome;
	}

	public Float getPreco() {
		return preco;
	}

	public String getNome() {
		return nome;
	}
}
