package br.cefetrj.sca.infra;

import java.util.List;

import br.cefetrj.sca.dominio.SemestreLetivo;
import br.cefetrj.sca.dominio.Turma;

public interface TurmaDao {

	Boolean gravar(Turma t);

	List<Turma> recuperarTodos();

	Turma getByCodigo(String codigo);

	List<Turma> getTurmasAbertas(SemestreLetivo semestreLetivoCorrente);

	List<Turma> getTurmasCursadas(String matricula,
			SemestreLetivo semestreLetivoCorrente);
}
