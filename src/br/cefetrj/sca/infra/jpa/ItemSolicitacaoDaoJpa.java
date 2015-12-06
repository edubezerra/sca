package br.cefetrj.sca.infra.jpa;

import java.util.List;

import br.cefetrj.sca.dominio.isencoes.ItemSolicitacaoIsencaoDisciplinas;

public class ItemSolicitacaoDaoJpa extends GenericDaoJpa<ItemSolicitacaoIsencaoDisciplinas>
		implements ItemSolicitacaoDao {

	@Override
	public void adicionarItensDeSolicitacao(List<ItemSolicitacaoIsencaoDisciplinas> itensSolicitacao) {
		for (int i = 0; i < itensSolicitacao.size(); i++) {
			super.incluir(itensSolicitacao.get(i));
		}
	}
}
