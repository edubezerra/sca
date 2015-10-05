package misc.lojavirtual;

import java.util.Date;

public class Pagamento {

	private Float valor;
	private Date data;

	public Pagamento(Float valorParcela) {
		this.valor = valorParcela;
		this.data = new Date();
	}

	public Float getValor() {
		return valor;
	}
	
	public Date getData() {
		return data;
	}
}
