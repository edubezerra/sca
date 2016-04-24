package br.cefetrj.sca.dominio.repositories;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.cefetrj.sca.dominio.Aluno;
import br.cefetrj.sca.dominio.Departamento;
import br.cefetrj.sca.dominio.PeriodoLetivo;
import br.cefetrj.sca.dominio.PeriodoLetivo.EnumPeriodo;
import br.cefetrj.sca.dominio.Turma;
import br.cefetrj.sca.dominio.matriculaforaprazo.MatriculaForaPrazo;

public interface MatriculaForaPrazoRepositorio extends
		JpaRepository<MatriculaForaPrazo, Serializable> {

	@Query("SELECT s FROM MatriculaForaPrazo s WHERE s.aluno.id = ?1")
	public List<MatriculaForaPrazo> findMatriculasForaPrazoByAluno(Long idAluno);

	@Query("SELECT s FROM MatriculaForaPrazo s "
			+ "WHERE s.aluno = ?1 AND s.semestreLetivo = ?2 ")
	public MatriculaForaPrazo findMatriculaForaPrazoByAlunoAndSemestre(
			Aluno aluno, PeriodoLetivo periodo);

	@Query("SELECT t FROM ItemMatriculaForaPrazo i, MatriculaForaPrazo s, Turma t "
			+ "WHERE s.aluno.id = ?1 "
			+ "AND s.semestreLetivo.ano = ?2 "
			+ "AND s.semestreLetivo.periodo = ?3 "
			+ "AND t.codigo = ?4 "
			+ "AND t.id = i.turma.id")
	public Turma getTurmaSolicitada(Long idAluno, String codigoTurma, int ano,
			EnumPeriodo periodo);

	@Query("SELECT s FROM MatriculaForaPrazo s "
			+ "WHERE s.semestreLetivo.ano = ?1 "
			+ "AND s.semestreLetivo.periodo = ?2")
	public List<MatriculaForaPrazo> findMatriculasForaPrazoBySemestre(
			EnumPeriodo periodo, int ano);

	@Query("SELECT DISTINCT s FROM MatriculaForaPrazo s "
			+ "JOIN s.itensMatriculaForaPrazo i WHERE s.semestreLetivo = ?1 AND i.departamento = ?2")
	public List<MatriculaForaPrazo> findMatriculasForaPrazoByDepartamentoAndSemestre(
			PeriodoLetivo periodo, Departamento departamento);

	public MatriculaForaPrazo findMatriculaForaPrazoById(Long solicitacaoId);
}
