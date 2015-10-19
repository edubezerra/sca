package misc.lojavirtual;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class Venda {
	Long id;

	Date dataRealizacao;

	Set<ItemVenda> itensVenda = new HashSet<ItemVenda>();

	Set<Pagamento> pagamentos = new HashSet<Pagamento>();

	public Venda() {
		dataRealizacao = new Date();
	}

	// Float getTotal() {
	// Float total = 0;
	// for(item: itensVenda) {
	// total += item.getProduto().getPreco() * item.getQuantidade();
	// }
	// return total;
	// }

	Float getTotal() {
		Float total = 0.0f;
		for (ItemVenda item : itensVenda) {
			total += item.getTotal();
		}
		return total;
	}

	/**
	 * 
	 * @param valorParcela
	 * @return
	 */
	Float registrarPagamento(Float valorParcela) {
		Pagamento pagamento = new Pagamento(valorParcela);
		this.pagamentos.add(pagamento);

		return this.getTotal() - this.getTotalPagamentos();

	}

	public Float getTotalPagamentos() {
		Float total = 0.0f;
		for (Pagamento pagamento : pagamentos) {
			total += pagamento.getValor();
		}
		return total;
	}

	public Boolean estaQuitada() {
		return this.getTotal() <= this.getTotalPagamentos();
	}

	public void registrarItem(Produto p, int quantidade) {
		ItemVenda item = new ItemVenda(p, quantidade);
		this.itensVenda.add(item);
	}

	/**
	 * @throws IllegalStateException
	 *             quando o pedido já possui um item com o mesmo produto
	 * 
	 * @throws IllegalArgumentException
	 *             quando a quantidade não é positiva
	 */
	public void adicionarItem(Produto p, Integer qtd) {
		if (p == null) {
			throw new IllegalArgumentException("Produto não informado!");
		}
		if (qtd <= 0) {
			throw new IllegalArgumentException("Quantidade não é positiva!");
		}
		ItemVenda iv = new ItemVenda(p, qtd);
		boolean jaPossui = false;
		for (ItemVenda itemVenda : itensVenda) {
			if (itemVenda.equals(iv)) {
				jaPossui = true;
				break;
			}
		}
		if (jaPossui) {
			throw new IllegalStateException("Pedido já possui um item com o mesmo produto!");
		}
		this.itensVenda.add(iv);
	}

	/**
	 * @throws IllegalStateException
	 *             quando o pedido já possui um item com o mesmo produto
	 * 
	 * @throws IllegalArgumentException
	 *             quando a quantidade não é positiva
	 */
	public void removerItem(Produto p) {
		if (p == null) {
			throw new IllegalArgumentException("Produto não informado!");
		}
		boolean contemProduto = false;
		ItemVenda iv = null;
		for (ItemVenda itemVenda : itensVenda) {
			if (itemVenda.getProduto().equals(p)) {
				contemProduto = true;
				iv = itemVenda;
				break;
			}
		}
		if (!contemProduto) {
			throw new IllegalStateException("Produto informado não consta na venda!");
		}
		this.itensVenda.remove(iv);
	}
}
