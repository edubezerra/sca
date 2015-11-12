package br.cefetrj.sca.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import br.cefetrj.sca.dominio.Disciplina;
import br.cefetrj.sca.dominio.Professor;
import br.cefetrj.sca.dominio.gradesdisponibilidade.FichaDisponibilidade;
import br.cefetrj.sca.dominio.gradesdisponibilidade.FichaDisponibilidadeFabrica;
import br.cefetrj.sca.dominio.gradesdisponibilidade.GradeDisponibilidade;
import br.cefetrj.sca.dominio.repositorio.DisciplinaRepositorio;
import br.cefetrj.sca.dominio.repositorio.ProfessorRepositorio;

@Service
@Scope("session")
public class FornecerGradeDisponibilidadeService {

	@Autowired
	private ProfessorRepositorio professorRepositorio;

	@Autowired
	private DisciplinaRepositorio disciplinaRepositorio;

	@Autowired
	FichaDisponibilidadeFabrica fdFabrica;

	GradeDisponibilidade gradeDisponibilidade = null;

	public FichaDisponibilidade validarProfessor(String matriculaProfessor) {
		Professor p = professorRepositorio.getProfessor(matriculaProfessor);
		if (p != null) {
			gradeDisponibilidade = new GradeDisponibilidade(p);
			FichaDisponibilidade fd = fdFabrica.criar(p);
			return fd;
		}
		return null;
	}

	public void adicionarDisciplina(String codigoDisciplina) {
		Disciplina d = disciplinaRepositorio
				.getDisciplinaPorCodigo(codigoDisciplina);
		gradeDisponibilidade.adicionarDisciplina(d);
	}

	private FichaDisponibilidade getFichaFake() {
		FichaDisponibilidade ficha = new FichaDisponibilidade("1506449",
				"Eduardo Bezerra");
		Set<Disciplina> habilitacoes = new HashSet<>();
		habilitacoes.add(new Disciplina("ESTATÍSTICA E PROBABILIDADE",
				"GCC1518", "4"));
		habilitacoes.add(new Disciplina("ARQUITETURA E PADRÕES DE SOFTWARE",
				"GCC1520", "4"));
		habilitacoes.add(new Disciplina("MATEMÁTICA DISCRETA", "GCC1208", "4"));

		ficha.definirHabilitacoes(habilitacoes);
		return ficha;
	}

}
