package br.cefetrj.sca.infra.cargadados;

import javax.persistence.NoResultException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.cefetrj.sca.dominio.Disciplina;
import br.cefetrj.sca.dominio.Professor;
import br.cefetrj.sca.dominio.repositories.DisciplinaRepositorio;
import br.cefetrj.sca.dominio.repositories.ProfessorRepositorio;

@Component
public class ImportadorHabilitacoesParaProfessor {

	@Autowired
	ProfessorRepositorio professorRepositorio;

	@Autowired
	DisciplinaRepositorio disciplinaRepositorio;

	public void run() {

		String matricula = "1506449";

		Professor professor;
		try {
			professor = professorRepositorio.findProfessorByMatricula(matricula);
		} catch (NoResultException e) {
			professor = null;
		}

		Disciplina d;
		d = findDisciplinaByCodigo("GCC1520");
		professor.habilitarPara(d);

		d = findDisciplinaByCodigo("GCC1208");
		professor.habilitarPara(d);

		d = findDisciplinaByCodigo("GCC1518");
		professor.habilitarPara(d);

		professorRepositorio.save(professor);

		System.out.println("Foram registradas 3 habilitações para o professor cuja matrícula é " + matricula);
	}

	private Disciplina findDisciplinaByCodigo(String codigo) {
		Disciplina d;
		try {
			d = disciplinaRepositorio.findDisciplinaByCodigo(codigo);
		} catch (NoResultException e) {
			d = null;
		}
		return d;
	}
}