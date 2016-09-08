package br.cefetrj.sca.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.cefetrj.sca.dominio.Aluno;
import br.cefetrj.sca.dominio.Disciplina;
import br.cefetrj.sca.dominio.Professor;
import br.cefetrj.sca.dominio.isencoes.ItemIsencaoDisciplina;
import br.cefetrj.sca.dominio.isencoes.ProcessoIsencaoDisciplinas;
import br.cefetrj.sca.dominio.repositories.AlunoRepositorio;
import br.cefetrj.sca.dominio.repositories.DisciplinaRepositorio;
import br.cefetrj.sca.dominio.repositories.ProcessoIsencaoRepositorio;
import br.cefetrj.sca.dominio.repositories.ProfessorRepositorio;

@Service
public class IsencaoDisciplinaService {

	@Autowired
	private AlunoRepositorio alunoRepo;

	@Autowired
	private ProfessorRepositorio professorRepo;

	@Autowired
	private DisciplinaRepositorio disciplinaRepo;

	@Autowired
	private ProcessoIsencaoRepositorio processoIsencaoDisciplinasRepo;

	public Aluno findAlunoByMatricula(String matricula) {
		return alunoRepo.findAlunoByMatricula(matricula);
	}

	public Professor findProfessorByMatricula(String matricula) {
		return professorRepo.findProfessorByMatricula(matricula);
	}

	public Disciplina getDisciplinaPorId(Long idDisciplina) {
		return disciplinaRepo.findDisciplinaById(idDisciplina);
	}

	public List<Disciplina> findDisciplinas(String siglaCurso) {
		return disciplinaRepo.findBySigla(siglaCurso);
	}

	public List<ProcessoIsencaoDisciplinas> findProcessosIsencao() {
		return processoIsencaoDisciplinasRepo.findAll();
	}

	public List<Aluno> getTodosOsAlunos() {
		return alunoRepo.findAll();
	}

	public ItemIsencaoDisciplina findItemIsencaoById(Long solicitacaoId) {
		return processoIsencaoDisciplinasRepo
				.findItemIsencaoById(solicitacaoId);
	}
}