package br.cefetrj.sca.dominio.inscricoes;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import br.cefetrj.sca.dominio.Aluno;
import br.cefetrj.sca.dominio.Disciplina;
import br.cefetrj.sca.dominio.GradeHorarios;
import br.cefetrj.sca.dominio.Turma;
import br.cefetrj.sca.dominio.repositories.AlunoRepositorio;

public class TurmasPossiveisServico {

	@Autowired
	AlunoRepositorio alunoRepositorio;

	@Autowired
	GradeHorarios gradeHorarios;

	public List<Turma> getTurmasPossiveis(String matrAluno) {
		Aluno aluno = alunoRepositorio.findAlunoByMatricula(matrAluno);
		List<Disciplina> disciplinas = aluno.getDisciplinasPossiveis();
		return gradeHorarios.getTurmasAbertasPara(disciplinas);
	}

}
