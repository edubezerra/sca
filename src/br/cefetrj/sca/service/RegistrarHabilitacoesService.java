package br.cefetrj.sca.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.cefetrj.sca.dominio.Disciplina;
import br.cefetrj.sca.dominio.Professor;
import br.cefetrj.sca.dominio.repositorio.ProfessorRepositorio;

@Component
public class RegistrarHabilitacoesService {

	@Autowired
	private ProfessorRepositorio repo;

	private Professor professor;

	List<Professor> obterProfessores() {
		return repo.obterProfessores();
	}

	public List<String> obterHabilitacoes(String matricula) {
		professor = repo.getProfessor(matricula);
		List<String> nomesHabilitacoes = new ArrayList<String>();
		Set<Disciplina> disciplinas = professor.getHabilitacoes();
		for (Disciplina disciplina : disciplinas) {
			nomesHabilitacoes.add(disciplina.getNome());
		}
		return nomesHabilitacoes;
	}

	public void adicionarHabilitacao(String nome) {
		// TODO
	}

	public void removerHabilitacao(String nome) {
		// TODO
	}

	public void adicionarHabilitacoes(List<String> nomes) {
		// TODO
	}

	public void removerHabilitacoes(List<String> nomes) {
		// TODO
	}

	public void registrarHabilitacoes() {
		repo.atualizar(professor);
	}

	public Professor selecionarProfessor(String mariculaProfessor) {
		// TODO Auto-generated method stub
		return null;
	}
}
