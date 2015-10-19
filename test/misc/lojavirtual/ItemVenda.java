package misc.lojavirtual;

public class ItemVenda {
	int quantidade;
	private Produto produto;

	public ItemVenda(Produto p, int quantidade) {
		this.produto = p;
		this.quantidade = quantidade;
	}

	public Float getTotal() {
		return produto.getPreco() * quantidade;
	}

	public Produto getProduto() {
		return produto;
	}

}
