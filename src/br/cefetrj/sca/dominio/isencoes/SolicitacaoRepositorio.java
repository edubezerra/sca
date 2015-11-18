package br.cefetrj.sca.dominio.isencoes;

import br.cefetrj.sca.infra.jpa.SolicitacaoDao;
import br.cefetrj.sca.infra.jpa.SolicitacaoDaoJpa;

public class SolicitacaoRepositorio {

	private SolicitacaoDao dao = new SolicitacaoDaoJpa();

	public SolicitacaoIsencaoDisciplinas adicionarSolicitacao(SolicitacaoIsencaoDisciplinas solicitacao) {
		return dao.adicionarSolicitacao(solicitacao);
	}
	
	public SolicitacaoIsencaoDisciplinas obterSolicitacaoPorId(long solicitacaoId) {
		return dao.obterSolicitacaoPorId(solicitacaoId);
	}
}
