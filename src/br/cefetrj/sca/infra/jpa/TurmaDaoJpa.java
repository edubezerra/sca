package br.cefetrj.sca.infra.jpa;

import java.util.List;

import org.springframework.stereotype.Component;

import br.cefetrj.sca.dominio.SemestreLetivo;
import br.cefetrj.sca.dominio.Turma;
import br.cefetrj.sca.infra.TurmaDao;

@Component
public class TurmaDaoJpa implements TurmaDao {
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
	public Turma getByCodigo(String codigo) {
		String consulta = "SELECT a from Turma a WHERE a.codigo = ?";
		Object array[] = { codigo };

		return genericDAO.obterEntidade(consulta, array);
	}

	@Override
	public List<Turma> getTurmasAbertas(SemestreLetivo semestreLetivo) {
		String consulta = "SELECT t from Turma t WHERE t.ano = ? AND t.periodo = ?";
		Object array[] = { semestreLetivo.getAno(), semestreLetivo.getPeriodo() };

		return genericDAO.obterEntidades(consulta, array);
	}

	@Override
	public List<Turma> getTurmasCursadas(String matricula,
			SemestreLetivo semestreLetivo) {
		String consulta = "SELECT t from Turma t JOIN t.inscricoes i JOIN i.aluno a WHERE t.semestreLetivo.ano = ? AND t.semestreLetivo.periodo = ? AND a.matricula = ?";
		Object array[] = { semestreLetivo.getAno(),
				semestreLetivo.getPeriodo(), matricula };

		return genericDAO.obterEntidades(consulta, array);
	}
}
