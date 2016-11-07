package br.cefetrj.sca.service.util;

import br.cefetrj.sca.dominio.Disciplina;
import br.cefetrj.sca.dominio.isencoes.ItemPedidoIsencaoDisciplina;

public class ItemFichaIsencaoDisciplina {
	Disciplina disciplina;
	boolean isSolicitada;

	public ItemFichaIsencaoDisciplina(ItemPedidoIsencaoDisciplina itemPedido) {
		this.disciplina = itemPedido.getDisciplina();
		this.isSolicitada = true;
	}

	public ItemFichaIsencaoDisciplina(Disciplina disciplina) {
		this.disciplina = disciplina;
		this.isSolicitada = false;
	}

	public Disciplina getDisciplina() {
		return disciplina;
	}
	
	public boolean isSolicitada() {
		return isSolicitada;
	}}
