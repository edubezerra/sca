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
		String consulta = "SELECT a from Turma a WHERE a.codigo = ? and a.periodo = ?";
		Object array[] = { codigo, periodo };
		return genericDAO.obterEntidade(consulta, array);
	}

	@Override
	public List<Turma> getTurmasAbertas(PeriodoLetivo semestreLetivo) {
		String consulta = "SELECT t from Turma t WHERE t.ano = ? AND t.periodo = ?";
		Object array[] = { semestreLetivo.getAno(), semestreLetivo.getPeriodo() };

		return genericDAO.obterEntidades(consulta, array);
	}

	@Override
	public List<Turma> getTurmasCursadas(String cpf,
			PeriodoLetivo semestreLetivo) {
		String consulta = "SELECT t from Turma t JOIN t.inscricoes i JOIN i.aluno a "
				+ "WHERE t.semestreLetivo.ano = ? AND t.semestreLetivo.periodo = ? AND a.pessoa.cpf = ?";
		Object array[] = { semestreLetivo.getAno(),
				semestreLetivo.getPeriodo(), cpf };

		return genericDAO.obterEntidades(consulta, array);
	}
}
