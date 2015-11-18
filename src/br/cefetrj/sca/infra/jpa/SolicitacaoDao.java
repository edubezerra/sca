package br.cefetrj.sca.infra.jpa;

import br.cefetrj.sca.dominio.isencoes.SolicitacaoIsencaoDisciplinas;

public interface SolicitacaoDao {
	public SolicitacaoIsencaoDisciplinas adicionarSolicitacao(SolicitacaoIsencaoDisciplinas solicitacao);
	
	public SolicitacaoIsencaoDisciplinas obterSolicitacaoPorId(long solicitacaoId);
}
