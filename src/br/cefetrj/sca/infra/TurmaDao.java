package br.cefetrj.sca.infra;

import java.util.List;

import br.cefetrj.sca.dominio.PeriodoLetivo;
import br.cefetrj.sca.dominio.Turma;

public interface TurmaDao {

	Boolean gravar(Turma t);

	List<Turma> recuperarTodos();

	Turma getByCodigo(String codigo);

	List<Turma> getTurmasAbertas(PeriodoLetivo semestreLetivoCorrente);

	List<Turma> getTurmasCursadas(String matricula,
			PeriodoLetivo semestreLetivoCorrente);
}
