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

	public  Float getTotalPagamentos() {
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

}
