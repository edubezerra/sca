package br.cefetrj.sca.infra.jpa;

import java.util.List;

import javax.persistence.NoResultException;

import br.cefetrj.sca.dominio.Disciplina;
import br.cefetrj.sca.dominio.inclusaodisciplina.Comprovante;
import br.cefetrj.sca.dominio.isencoes.SolicitacaoIsencaoDisciplinas;
import br.cefetrj.sca.infra.SolicitacaoIsencaoDisciplinasDao;

public class SolicitacaoIsencaoDisciplinasDaoJpa implements
		SolicitacaoIsencaoDisciplinasDao {

	private GenericDaoJpa<SolicitacaoIsencaoDisciplinas> genericDao = new GenericDaoJpa<>();
	private GenericDaoJpa<Comprovante> comprovanteGenericDAO = new GenericDaoJpa<>();
	private GenericDaoJpa<Disciplina> turmaGenericDAO = new GenericDaoJpa<>();

	@Override
	public boolean adicionarSolicitacao(
			SolicitacaoIsencaoDisciplinas solicitacaoInclusao) {
		return genericDao.incluir(solicitacaoInclusao);
	}

	@Override
	public SolicitacaoIsencaoDisciplinas getSolicitacaoByAluno(Long idAluno) {
		String consulta = "SELECT s FROM SolicitacaoIsencaoDisciplinas s WHERE s.aluno.id = ?";
		Object array[] = { idAluno };
		try {
			return genericDao.obterEntidade(consulta, array);
		} catch (NoResultException e) {
//			e.printStackTrace();
			return null;
		}
	}

	@Override
	public Comprovante getComprovante(Long solicitacaoId) {
		String consulta = "SELECT c FROM Comprovante c, ItemSolicitacaoIsencaoDisciplinas s "
				+ "WHERE s.id = ? " + "AND c.id = s.comprovante.id";
		Object array[] = { solicitacaoId };
		return comprovanteGenericDAO.tentaObterEntidade(consulta, array);
	}

	@Override
	public Disciplina getDisciplinaSolicitada(Long idAluno,
			String codigoDisciplina) {
		String consulta = "SELECT t FROM ItemSolicitacaoIsencaoDisciplinas i, SolicitacaoIsencaoDisciplinas s, Disciplina t "
				+ "WHERE s.aluno.id = ? "
				+ "AND t.codigo = ? "
				+ "AND t.id = i.turma.id";
		Object array[] = { idAluno, codigoDisciplina };
		return turmaGenericDAO.tentaObterEntidade(consulta, array);
	}

	@Override
	public List<SolicitacaoIsencaoDisciplinas> getTodasSolicitacoesByDepartamento(
			Long departamentoId) {
		String consulta = "SELECT DISTINCT s FROM SolicitacaoIsencaoDisciplinas s "
				+ "JOIN s.itemSolicitacao i " + "AND i.departamento.id = ?";
		Object array[] = { departamentoId };
		return genericDao.obterEntidades(consulta, array);
	}
}
