package br.cefetrj.sca.infra.jpa;

import java.util.List;

import br.cefetrj.sca.dominio.isencoes.ItemSolicitacaoIsencaoDisciplinas;

public interface ItemSolicitacaoDao {
	
	public abstract void adicionarItensDeSolicitacao(List<ItemSolicitacaoIsencaoDisciplinas> itensSolicitacao);
}
