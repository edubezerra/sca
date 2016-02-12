package br.cefetrj.sca.dominio;

import java.util.List;

public interface TurmaRepositorio {

	Boolean gravar(Turma t);

	List<Turma> recuperarTodos();

	Turma getByCodigoNoPeriodoLetivo(String codigo, PeriodoLetivo periodo);

	List<Turma> getTurmasAbertas(PeriodoLetivo periodo);

	List<Turma> getTurmasCursadas(String matricula,
			PeriodoLetivo semestreLetivoCorrente);
}
