package br.cefetrj.sca.dominio.repositories;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.cefetrj.sca.dominio.Disciplina;
import br.cefetrj.sca.dominio.inclusaodisciplina.Comprovante;
import br.cefetrj.sca.dominio.isencoes.SolicitacaoIsencaoDisciplinas;

public interface SolicitacaoIsencaoDisciplinasRepositorio extends
		JpaRepository<SolicitacaoIsencaoDisciplinas, Serializable> {
	@Query("SELECT s FROM SolicitacaoIsencaoDisciplinas s WHERE s.aluno.id = ?1")
	public SolicitacaoIsencaoDisciplinas getSolicitacaoByAluno(Long idAluno);

	@Query("SELECT c FROM Comprovante c, ItemSolicitacaoIsencaoDisciplinas s WHERE s.id = ?1 "
			+ "AND c.id = s.comprovante.id")
	public Comprovante getComprovante(Long solicitacaoId);

	@Query("SELECT t FROM ItemSolicitacaoIsencaoDisciplinas i, SolicitacaoIsencaoDisciplinas s, Disciplina t "
			+ "WHERE s.aluno.id = ?1 "
			+ "AND t.codigo = ?2 "
			+ "AND t.id = i.turma.id")
	public Disciplina getDisciplinaSolicitada(Long idAluno,
			String codigoDisciplina);

	@Query("SELECT DISTINCT s FROM SolicitacaoIsencaoDisciplinas s "
			+ "JOIN s.itemSolicitacao i " + "AND i.departamento.id = ?1")
	public List<SolicitacaoIsencaoDisciplinas> getTodasSolicitacoesByDepartamento(
			Long departamentoId);
}
