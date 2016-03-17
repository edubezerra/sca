package br.cefetrj.sca.dominio.repositories;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.cefetrj.sca.dominio.PeriodoLetivo.EnumPeriodo;
import br.cefetrj.sca.dominio.Turma;
import br.cefetrj.sca.dominio.inclusaodisciplina.Comprovante;
import br.cefetrj.sca.dominio.inclusaodisciplina.SolicitacaoMatriculaForaPrazo;

public interface SolicitacaoMatriculaForaPrazoRepositorio extends
		JpaRepository<SolicitacaoMatriculaForaPrazo, Serializable> {

	@Query("SELECT s FROM SolicitacaoMatriculaForaPrazo s WHERE s.aluno.id = ?1")
	public List<SolicitacaoMatriculaForaPrazo> getSolicitacoesAluno(Long idAluno);

	@Query("SELECT s FROM SolicitacaoMatriculaForaPrazo s "
			+ "WHERE s.aluno.id = ?1 " + "AND s.semestreLetivo.ano = ?2 "
			+ "AND s.semestreLetivo.periodo = ?3")
	public SolicitacaoMatriculaForaPrazo getSolicitacaoByAlunoSemestre(
			Long alunoId, int ano, EnumPeriodo periodo);

	@Query("SELECT c FROM Comprovante c, ItemSolicitacaoMatriculaForaPrazo s "
			+ "WHERE s.id = ?1 " + "AND c.id = s.comprovante.id")
	public Comprovante getComprovante(Long solicitacaoId);

	@Query("SELECT t FROM ItemSolicitacaoMatriculaForaPrazo i, SolicitacaoMatriculaForaPrazo s, Turma t "
			+ "WHERE s.aluno.id = ?1 "
			+ "AND s.semestreLetivo.ano = ?2 "
			+ "AND s.semestreLetivo.periodo = ?3 "
			+ "AND t.codigo = ?4 "
			+ "AND t.id = i.turma.id")
	public Turma getTurmaSolicitada(Long idAluno, String codigoTurma, int ano,
			EnumPeriodo periodo);

	@Query("SELECT s FROM SolicitacaoMatriculaForaPrazo s "
			+ "WHERE s.semestreLetivo.ano = ?1 "
			+ "AND s.semestreLetivo.periodo = ?2")
	public List<SolicitacaoMatriculaForaPrazo> getTodasSolicitacoesBySemestre(
			EnumPeriodo periodo, int ano);

	@Query("SELECT DISTINCT s FROM SolicitacaoMatriculaForaPrazo s "
			+ "JOIN s.itensSolicitacao i WHERE s.semestreLetivo.ano = ?1 "
			+ "AND s.semestreLetivo.periodo = ?2 "
			+ "AND i.departamento.id = ?3")
	public List<SolicitacaoMatriculaForaPrazo> getTodasSolicitacoesByDepartamentoSemestre(
			EnumPeriodo periodo, int ano, Long departamentoId);
}
