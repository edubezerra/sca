package br.cefetrj.sca.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.cefetrj.sca.dominio.Aluno;
import br.cefetrj.sca.dominio.Disciplina;
import br.cefetrj.sca.dominio.Turma;
import br.cefetrj.sca.dominio.repositories.AlunoRepositorio;
import br.cefetrj.sca.dominio.repositories.TurmaRepositorio;

@Component
public class RealizarInscricaoService {

	@Autowired
	private AlunoRepositorio alunoRepo;

	@Autowired
	private TurmaRepositorio turmaRepositorio;

	public Aluno findAlunoByMatricula(String matriculaAluno) {
		return alunoRepo.findAlunoByMatricula(matriculaAluno);
	}

	public Turma findTurmaById(Long idTurma) {
		if (idTurma == null) {
			throw new IllegalArgumentException("Turma inexistente!");
		}

		Turma turma;

		try {
			turma = turmaRepositorio.findTurmaById(idTurma);
		} catch (Exception exc) {
			turma = null;
		}
		return turma;
	}

	public String registrarInscricao(Turma t, Aluno aluno) {
		try {
			t.inscreverAluno(aluno);
			turmaRepositorio.save(t);
			return "Inscrição realizada com sucesso!";
		} catch (IllegalArgumentException e1) {
			return "Inscrição já realizada!";
		} catch (IllegalStateException e2) {
			return "Limite de vagas já alcançado!";
		}
	}

	public List<Turma> getTurmasPossiveis(Aluno aluno, List<Disciplina> dp) {
		List<Turma> turmasPossiveis = new ArrayList<Turma>();
		List<Turma> turmas = turmaRepositorio.findAll();

		for (Turma turma : turmas) {
			if (dp.contains(turma.getDisciplina())) {
				turmasPossiveis.add(turma);
			}
		}
		return turmasPossiveis;
	}
}
