package br.cefetrj.sca.dominio.repositorio;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.cefetrj.sca.dominio.SemestreLetivo;
import br.cefetrj.sca.dominio.Turma;
import br.cefetrj.sca.infra.TurmaDao;

@Component
public class TurmaRepositorio {

	@Autowired
	private TurmaDao turmaDAO;

	private TurmaRepositorio() {
	}

	public List<Turma> obterTodos() {
		return turmaDAO.recuperarTodos();
	}

	public void adicionar(Turma turma) {
		turmaDAO.gravar(turma);
	}

	public List<Turma> getTurmasAbertas(SemestreLetivo semestreLetivoCorrente) {
		return turmaDAO.getTurmasAbertas(semestreLetivoCorrente);
	}

	public List<Turma> getTurmasCursadas(String matricula,
			SemestreLetivo semestreLetivoCorrente) {
		return turmaDAO.getTurmasCursadas(matricula, semestreLetivoCorrente);
	}

	public Turma getByCodigo(String codigoTurma) {
		return turmaDAO.getByCodigo(codigoTurma);
	}
}
