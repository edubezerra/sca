package br.cefetrj.sca.infra.jpa;

import javax.persistence.EntityTransaction;

import br.cefetrj.sca.dominio.isencoes.SolicitacaoIsencaoDisciplinas;
import br.cefetrj.sca.infra.DAOException;

public class SolicitacaoDaoJpa extends GenericDaoJpa<SolicitacaoIsencaoDisciplinas> implements SolicitacaoDao {

	public SolicitacaoIsencaoDisciplinas incluirComRetorno(SolicitacaoIsencaoDisciplinas entidade) {
		EntityTransaction tx = null;
		try {
			tx = entityManager.getTransaction();
			tx.begin();
			entityManager.persist(entidade);
			tx.commit();
		} catch (Exception ex) {
			if (tx != null && tx.isActive())
				tx.rollback();
			throw new DAOException("Erro na inclusão de objeto.", ex);
		}
		return entidade;
	}

	@Override
	public SolicitacaoIsencaoDisciplinas adicionarSolicitacao(SolicitacaoIsencaoDisciplinas solicitacao) {
		return incluirComRetorno(solicitacao);
	}

	@Override
	public SolicitacaoIsencaoDisciplinas obterSolicitacaoPorId(long solicitacaoId) {
		String consulta = "SELECT a from Solicitacao a WHERE a.solicitacaoId = ?";
		Object array[] = { solicitacaoId };
		return super.obterEntidade(consulta, array);
	}

}
