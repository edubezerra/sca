package br.cefetrj.sca.infra;

import java.util.List;

import br.cefetrj.sca.dominio.Professor;

public interface ProfessorDao {

	boolean incluir(Professor p);

	boolean alterar(Professor p);

	void excluir(Professor p);

	Professor getProfessorPorMatricula(String matricula);

	List<Professor> obterTodos();

}