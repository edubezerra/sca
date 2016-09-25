package br.cefetrj.sca.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.cefetrj.sca.dominio.Disciplina;
import br.cefetrj.sca.dominio.Professor;
import br.cefetrj.sca.dominio.gradesdisponibilidade.FichaDisponibilidade;
import br.cefetrj.sca.dominio.gradesdisponibilidade.FichaDisponibilidadeFabrica;
import br.cefetrj.sca.dominio.gradesdisponibilidade.GradeDisponibilidade;
import br.cefetrj.sca.dominio.repositories.DisciplinaRepositorio;
import br.cefetrj.sca.dominio.repositories.ProfessorRepositorio;

@Service
//@Scope("session")
public class FornecerGradeDisponibilidadeService {

	@Autowired
	private ProfessorRepositorio professorRepositorio;

	@Autowired
	private DisciplinaRepositorio disciplinaRepositorio;

	@Autowired
	FichaDisponibilidadeFabrica fichaDisponibilidadeFabrica;

	GradeDisponibilidade gradeDisponibilidade = null;

	public FichaDisponibilidade validarProfessor(String matriculaProfessor) {
		Professor p = professorRepositorio.findProfessorByMatricula(matriculaProfessor);
		if (p != null) {
			gradeDisponibilidade = new GradeDisponibilidade(p);
			FichaDisponibilidade fd = fichaDisponibilidadeFabrica.criar(p);
			return fd;
		}
		return null;
	}

	public void adicionarDisciplina(String idDisciplina) {
		Disciplina d = disciplinaRepositorio.findDisciplinaById(Long.parseLong(idDisciplina));
		gradeDisponibilidade.adicionarDisciplina(d);
	}

}
