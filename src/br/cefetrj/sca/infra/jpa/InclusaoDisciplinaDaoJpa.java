package br.cefetrj.sca.infra.jpa;

import java.util.List;

import br.cefetrj.sca.dominio.SemestreLetivo.EnumPeriodo;
import br.cefetrj.sca.dominio.Turma;
import br.cefetrj.sca.dominio.inclusaodisciplina.Comprovante;
import br.cefetrj.sca.dominio.inclusaodisciplina.ItemSolicitacao;
import br.cefetrj.sca.dominio.inclusaodisciplina.SolicitacaoInclusao;
import br.cefetrj.sca.infra.InclusaoDisciplinaDao;

public class InclusaoDisciplinaDaoJpa extends GenericDaoJpa<SolicitacaoInclusao> implements InclusaoDisciplinaDao{
	
	private GenericDaoJpa<ItemSolicitacao> genericDAO = new GenericDaoJpa<>();
	
	private GenericDaoJpa<Comprovante> comprovanteGenericDAO = new GenericDaoJpa<>();
	
	private GenericDaoJpa<Turma> turmaGenericDAO = new GenericDaoJpa<>();
	
	@Override
	public boolean adicionarSolicitacaoInclusao(SolicitacaoInclusao solicitacaoInclusao){
		return	incluir(solicitacaoInclusao);
	}
	
	@Override
	public boolean adicionarItemSolicitacao(ItemSolicitacao itemSolicitacao){
		return	genericDAO.incluir(itemSolicitacao);
	}
	
	@Override
	public List<SolicitacaoInclusao> getSolicitacaoAluno(Long idAluno) {
		String consulta = "SELECT s FROM SolicitacaoInclusao s "
						+ "WHERE s.aluno.id = ?";
		Object array[] = { idAluno };
		return super.obterEntidades(consulta, array);
	}

	@Override
	public SolicitacaoInclusao getSolicitacaoByAlunoSemestre(Long alunoId, int ano, EnumPeriodo periodo) {
		String consulta = "SELECT s FROM SolicitacaoInclusao s " +
							"WHERE s.aluno.id = ? " +
							"AND s.semestreLetivo.ano = ? " + 
							"AND s.semestreLetivo.periodo = ?";
		Object array[] = {alunoId, ano, periodo};
		return super.tentaObterEntidade(consulta, array);
	}

	@Override
	public Comprovante getComprovante(Long solicitacaoId) {
		String consulta = "SELECT c FROM Comprovante c, ItemSolicitacao s "
						+ "WHERE s.id = ? "
						+ "AND c.id = s.comprovante.id";
		Object array[] = {solicitacaoId};
		return comprovanteGenericDAO.tentaObterEntidade(consulta, array);
	}

	@Override
	public Turma getTurmaSolicitada(Long idAluno, String codigoTurma, int ano, EnumPeriodo periodo) {
		String consulta = "SELECT t FROM ItemSolicitacao i, SolicitacaoInclusao s, Turma t "
						+ "WHERE s.aluno.id = ? "
						+ "AND s.semestreLetivo.ano = ? " 
						+ "AND s.semestreLetivo.periodo = ? "
						+ "AND t.codigo = ? "
						+ "AND t.id = i.turma.id";
		Object array[] = {idAluno, ano, periodo, codigoTurma};
		return turmaGenericDAO.tentaObterEntidade(consulta, array);
	}

	@Override
	public List<SolicitacaoInclusao> getTodasSolicitacoesBySemestre(
			EnumPeriodo periodo, int ano) {
		String consulta = "SELECT s FROM SolicitacaoInclusao s "+
						  "WHERE s.semestreLetivo.ano = ? "+
						  "AND s.semestreLetivo.periodo = ?";
		Object array[] = {ano, periodo};
		return obterEntidades(consulta, array);
	}
	
	@Override
	public List<SolicitacaoInclusao> getTodasSolicitacoesByDepartamentoSemestre(EnumPeriodo periodo, int ano, Long departamentoId) {
		String consulta = "SELECT DISTINCT s FROM SolicitacaoInclusao s "
						+ "JOIN s.itemSolicitacao i "
						+ "WHERE s.semestreLetivo.ano = ? "
						+ "AND s.semestreLetivo.periodo = ? "
						+ "AND i.departamento.id = ?";
		Object array[] = {ano, periodo, departamentoId};
		return obterEntidades(consulta, array);
	}
	
	@Override
	public List<SolicitacaoInclusao> getTodasSolicitacoes() {
		return obterTodos(SolicitacaoInclusao.class);
	}

	@Override
	public boolean alterarItemSolicitacao(ItemSolicitacao itemSolicitacao) {
		return genericDAO.alterar(itemSolicitacao);
	}

	@Override
	public ItemSolicitacao getItemSolicitacaoById(Long id) {
		return genericDAO.obterPorId(ItemSolicitacao.class, id);
	}

}
