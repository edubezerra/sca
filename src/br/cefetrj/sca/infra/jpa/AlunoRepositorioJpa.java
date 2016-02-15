package br.cefetrj.sca.infra.jpa;

import java.util.List;
import java.util.logging.Level;

import javax.persistence.NoResultException;

import org.springframework.stereotype.Component;

import br.cefetrj.sca.dominio.Aluno;
import br.cefetrj.sca.dominio.AlunoRepositorio;

@Component
public class AlunoRepositorioJpa implements AlunoRepositorio {

	GenericDaoJpa<Aluno> genericDao = new GenericDaoJpa<>();
	
	@Override
	public Aluno getAlunoPorMatricula(String matricula) {
		String consulta = "SELECT a from Aluno a WHERE a.matricula = ?";
		Object array[] = { matricula };
		try {
			return genericDao.obterEntidade(consulta, array);
		} catch (NoResultException ex) {
			genericDao.logger.info(ex.getMessage());
			return null;
		}
	}

	@Override
	public boolean excluir(Aluno p) {
		return genericDao.excluir(Aluno.class, p.getId());
	}

	@Override
	public List<Aluno> obterTodos() {
		return genericDao.obterTodos(Aluno.class);
	}

	@Override
	public Aluno getAlunoPorCPF(String cpf) {
		String consulta = "SELECT a from Aluno a WHERE a.pessoa.cpf = ?";
		Object array[] = { cpf };
		try {
			return genericDao.obterEntidade(consulta, array);
		} catch (NoResultException ex) {
			genericDao.logger.log(Level.SEVERE, ex.getMessage() + ": " + cpf);
			return null;
		}
	}

	@Override
	public Aluno getAlunoPorId(String idAluno) {
		return genericDao.obterPorId(Aluno.class, Long.parseLong(idAluno));
	}

	@Override
	public boolean incluir(Aluno p) {
		return genericDao.incluir(p);
	}

	@Override
	public boolean alterar(Aluno p) {
		return genericDao.alterar(p);
	}

}
