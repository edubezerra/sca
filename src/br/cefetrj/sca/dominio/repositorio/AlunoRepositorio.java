package br.cefetrj.sca.dominio.repositorio;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.cefetrj.sca.dominio.Aluno;
import br.cefetrj.sca.infra.AlunoDao;

@Component
public class AlunoRepositorio {

	@Autowired
	private AlunoDao dao;

	private AlunoRepositorio() {
	}

	public void atualizar(Aluno aluno) {
		dao.alterar(aluno);
	}

	public List<Aluno> recuperarTodos() {
		return dao.obterTodos();
	}

	public Aluno getByMatricula(String matriculaAluno) {
		return dao.getAlunoPorMatricula(matriculaAluno);
	}
}
