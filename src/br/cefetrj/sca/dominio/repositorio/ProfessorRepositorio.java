package br.cefetrj.sca.dominio.repositorio;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.cefetrj.sca.dominio.Professor;
import br.cefetrj.sca.infra.ProfessorDao;

@Component
public class ProfessorRepositorio {

	@Autowired
	private ProfessorDao dao;

	private ProfessorRepositorio() {
	}

	public Professor getProfessor(String matricula) {
		return dao.getProfessorPorMatricula(matricula);
	}

	public void adicionar(String matricula, String nome) {
		Professor prof = new Professor(matricula, nome);
		dao.incluir(prof);
	}

	public List<Professor> obterProfessores() {
		return dao.obterTodos();
	}

	public void atualizar(Professor professor) {
		dao.alterar(professor);
	}

}
