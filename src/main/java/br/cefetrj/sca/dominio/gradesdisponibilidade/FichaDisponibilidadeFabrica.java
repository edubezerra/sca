package br.cefetrj.sca.dominio.gradesdisponibilidade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.cefetrj.sca.dominio.Professor;
import br.cefetrj.sca.dominio.repositories.ProfessorRepositorio;

@Component
public class FichaDisponibilidadeFabrica {

	@Autowired
	ProfessorRepositorio professorRepositorio;

	/**
	 * Cria um objeto <code>FichaDisponibilidade</code>, que contém as
	 * informações necessárias para que um professor monte sua grade de
	 * disponibilidades para determinado período letivo.
	 * 
	 * @param professor
	 *            professor para o qual criar a ficha de disponibilidade.
	 * 
	 * @return ficha de disponibilidades recém construída.
	 */
	public FichaDisponibilidade criar(String matriculaProfessor) {
		Professor professor = professorRepositorio.findProfessorByMatricula(matriculaProfessor);
		return criar(professor);
	}

	public FichaDisponibilidade criar(Professor professor) {
		if (professor != null) {
			FichaDisponibilidade ficha = new FichaDisponibilidade(professor.getMatricula(), professor.getNome());
			ficha.definirHabilitacoes(professor.getHabilitacoes());
			return ficha;
		} else {
			return null;
		}
	}
}
