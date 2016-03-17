package br.cefetrj.sca.dominio.repositories;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.cefetrj.sca.dominio.PeriodoLetivo;
import br.cefetrj.sca.dominio.Turma;

public interface TurmaRepositorio extends JpaRepository<Turma, Serializable> {

	@Query("SELECT t from Turma t WHERE t.codigo = ?1 and t.periodo = ?2")
	Turma getByCodigoAndPeriodoLetivo(String codigo, PeriodoLetivo periodo);

	@Query("SELECT t from Turma t WHERE t.periodo.ano = ?1")
	List<Turma> getTurmasAbertasNoPeriodo(PeriodoLetivo periodo);

	@Query("SELECT t from Turma t JOIN t.inscricoes i JOIN i.aluno a "
			+ "WHERE t.periodo.ano = ? AND t.periodo.periodo = ? AND a.matricula = ?1")
	List<Turma> getTurmasCursadasPorAlunoNoPeriodo(String matricula, PeriodoLetivo periodo);

	@Query("SELECT t from Turma t JOIN t.inscricoes i JOIN i.aluno a "
			+ "WHERE a.matricula = ?1")
	List<Turma> getTurmasCursadasPorAluno(String matricula);
}
