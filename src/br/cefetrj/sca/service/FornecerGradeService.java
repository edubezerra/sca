package br.cefetrj.sca.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.cefetrj.sca.dominio.Professor;
import br.cefetrj.sca.dominio.repositorio.ProfessorRepositorio;

@Component
public class FornecerGradeService {

	@Autowired
	private ProfessorRepositorio professorRepositorio;

	public Professor validarProfessor(String matriculaProfessor) {
		return professorRepositorio.obterProfessor(matriculaProfessor);
	}

	public void incluirProfessor(String matricula, String nome) {
		professorRepositorio.adicionar(matricula, nome);
	}
}
