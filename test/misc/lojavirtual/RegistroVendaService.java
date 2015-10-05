package misc.lojavirtual;

public class RegistroVendaService {
	private RepositorioVenda repositorioVenda;

	Float registrarPagamentoVenda(Long idVenda, Float valorPagamento) {
		Venda venda = repositorioVenda.getPorId(idVenda);
		Float saldo = venda.registrarPagamento(valorPagamento);
		return saldo;
	}
}
