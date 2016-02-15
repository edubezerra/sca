package br.cefetrj.sca.infra.jpa;

import java.util.List;

import org.springframework.stereotype.Component;

import br.cefetrj.sca.dominio.PeriodoLetivo;
import br.cefetrj.sca.dominio.Turma;
import br.cefetrj.sca.dominio.TurmaRepositorio;

@Component
public class TurmaRepositorioJpa implements TurmaRepositorio {
	private GenericDaoJpa<Turma> genericDAO = new GenericDaoJpa<Turma>();

	@Override
	public Boolean gravar(Turma t) {
		return genericDAO.incluir(t);
	}

	@Override
	public List<Turma> recuperarTodos() {
		return genericDAO.obterTodos(Turma.class);
	}

	@Override
	public Turma getByCodigoNoPeriodoLetivo(String codigo, PeriodoLetivo periodo) {
		String consulta = "SELECT t from Turma t WHERE t.codigo = ? and t.periodo.ano = ? AND t.periodo.periodo = ?";
		Object array[] = { codigo, periodo.getAno(), periodo.getPeriodo() };
		return genericDAO.obterEntidade(consulta, array);
	}

	@Override
	public List<Turma> getTurmasAbertas(PeriodoLetivo periodo) {
		String consulta = "SELECT t from Turma t WHERE t.periodo.ano = ? AND t.periodo.periodo = ?";
		Object array[] = { periodo.getAno(), periodo.getPeriodo() };

		return genericDAO.obterEntidades(consulta, array);
	}

	@Override
	public List<Turma> getTurmasCursadas(String matricula, PeriodoLetivo periodo) {
		String consulta = "SELECT t from Turma t JOIN t.inscricoes i JOIN i.aluno a "
				+ "WHERE t.periodo.ano = ? AND t.periodo.periodo = ? AND a.matricula = ?";
		Object array[] = { periodo.getAno(), periodo.getPeriodo(), matricula };

		return genericDAO.obterEntidades(consulta, array);
	}

	@Override
	public List<Turma> getTurmasCursadas(String matricula) {
		String consulta = "SELECT t from Turma t JOIN t.inscricoes i JOIN i.aluno a "
				+ "WHERE a.matricula = ?";
		Object array[] = { matricula };

		return genericDAO.obterEntidades(consulta, array);
	}

	@Override
	public Turma getById(Long idTurma) {
		return genericDAO.obterPorId(Turma.class, idTurma);
	}
}
