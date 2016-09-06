package br.cefetrj.sca.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.cefetrj.sca.dominio.Aluno;
import br.cefetrj.sca.dominio.Disciplina;
import br.cefetrj.sca.dominio.ItemIsencao;
import br.cefetrj.sca.dominio.ProcessoIsencaoDisciplinas;
import br.cefetrj.sca.dominio.Professor;
import br.cefetrj.sca.dominio.repositories.AlunoRepositorio;
import br.cefetrj.sca.dominio.repositories.DisciplinaRepositorio;
import br.cefetrj.sca.dominio.repositories.ItemIsencaoRepositorio;
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
	private ProcessoIsencaoRepositorio procIsencaoRepo;

	@Autowired
	private ItemIsencaoRepositorio itemIsencaoRepo;

	public Aluno findAlunoByMatricula(String matricula) {

		Aluno aluno = alunoRepo.findAlunoByMatricula(matricula);
		if (aluno != null)
			return aluno;
		else
			System.out
					.println("IsencaoDisciplinaService - Aluno não encontrado!");
		return null;
	}

	public Professor findProfessorByMatricula(String matricula) {
		return professorRepo.findProfessorByMatricula(matricula);
	}

	public Disciplina getDisciplinaPorId(Long idDisciplina) {
		return disciplinaRepo.findDisciplinaById(idDisciplina);
	}

	public List<Disciplina> findDisciplinas(String siglaCurso) {
		List<Disciplina> disciplinas = disciplinaRepo.findBySigla(siglaCurso);
		if (disciplinas.isEmpty()) {
			System.out
					.println("IsencaoDisciplinaService  Lista de disciplinas está vazia!");
			return null;
		} else
			return disciplinas;
	}

	public List<ProcessoIsencaoDisciplinas> findProcessosIsencao() {
		List<ProcessoIsencaoDisciplinas> pi = procIsencaoRepo.findProcessoIsencao();
		if (pi.isEmpty()) {
			System.out
					.println("IsencaoDisciplinaService  Lista de processos está vazia!");
			return null;
		} else {
			return pi;
		}
	}

	public List<Aluno> getTodosOsAlunos() {
		List<Aluno> a = alunoRepo.findAll();
		if (a.isEmpty()) {
			System.out
					.println("IsencaoDisciplinaService  Lista de processos está vazia!");
			return null;
		} else {
			return a;
		}
	}

	public ItemIsencao findItemIsencaoById(Long solicitacaoId) {
		return itemIsencaoRepo.findItemIsencaoById(solicitacaoId);
	}

}