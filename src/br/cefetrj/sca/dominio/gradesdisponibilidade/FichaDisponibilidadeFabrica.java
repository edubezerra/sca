package br.cefetrj.sca.dominio.gradesdisponibilidade;

import org.springframework.beans.factory.annotation.Autowired;

import br.cefetrj.sca.dominio.EnumDiaSemana;
import br.cefetrj.sca.dominio.ItemHorario;
import br.cefetrj.sca.dominio.Professor;
import br.cefetrj.sca.dominio.repositorio.ProfessorRepositorio;

public class FichaDisponibilidadeFabrica {

	@Autowired
	ProfessorRepositorio profRepo;

	/**
	 * Cria um objeto <code>FichaDisponibilidade</code>, com todas as
	 * informações necessárias para que um professor monte sua grade de
	 * disponibilidades para determinado período letivo..
	 * 
	 * @param matriculaProfessor
	 *            matrícula do professor para o qual criar a ficha de
	 *            disponibilidade.
	 */
	public FichaDisponibilidade criar(String matriculaProfessor) {
		Professor professor = profRepo.obterProfessor(matriculaProfessor);
		FichaDisponibilidade ficha = new FichaDisponibilidade(professor.getMatricula(), professor.getNome());
		ficha.definirDisciplinas(professor.getHabilitacoes());
		ficha.definirTemposAula(ItemHorario.itens());
		ficha.definirDiasSemana(EnumDiaSemana.dias());
		return ficha;
	}
}
