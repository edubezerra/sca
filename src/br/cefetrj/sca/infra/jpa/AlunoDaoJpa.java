package br.cefetrj.sca.infra.jpa;

import java.util.List;
import java.util.logging.Level;

import javax.persistence.NoResultException;

import org.springframework.stereotype.Component;

import br.cefetrj.sca.dominio.Aluno;
import br.cefetrj.sca.infra.AlunoDao;

@Component
public class AlunoDaoJpa extends GenericDaoJpa<Aluno> implements AlunoDao {

	@Override
	public Aluno getAlunoPorMatricula(String matricula) {
		String consulta = "SELECT a from Aluno a WHERE a.matricula = ?";
		Object array[] = { matricula };
		try {
			return super.obterEntidade(consulta, array);
		} catch (NoResultException ex) {
			logger.info(ex.getMessage());
			return null;
		}
	}

	@Override
	public boolean excluir(Aluno p) {
		return super.excluir(Aluno.class, p.getId());
	}

	@Override
	public List<Aluno> obterTodos() {
		return super.obterTodos(Aluno.class);
	}

	@Override
	public Aluno getAlunoPorCPF(String cpf) {
		String consulta = "SELECT a from Aluno a WHERE a.pessoa.cpf = ?";
		Object array[] = { cpf };
		try {
			return super.obterEntidade(consulta, array);
		} catch (NoResultException ex) {
			logger.log(Level.SEVERE, ex.getMessage() + ": " + cpf);
			return null;
		}
	}
}
