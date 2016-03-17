package br.cefetrj.sca.dominio.gradesdisponibilidade;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import br.cefetrj.sca.dominio.Disciplina;
import br.cefetrj.sca.dominio.Intervalo;

public class FichaDisponibilidade {
	String nomeProfessor;
	String matriculaProfessor;
	List<Disciplina> habilitacoes = new ArrayList<>();
	List<Intervalo> temposAula = new ArrayList<>();
	List<String> diasSemana = new ArrayList<>();

	public FichaDisponibilidade(String matricula, String nome) {
		this.matriculaProfessor = matricula;
		this.nomeProfessor = nome;
	}

	public String getNomeProfessor() {
		return nomeProfessor;
	}

	public String getMatriculaProfessor() {
		return matriculaProfessor;
	}

	public List<Disciplina> getHabilitacoes() {
		return java.util.Collections.unmodifiableList(habilitacoes);
	}

	public List<Intervalo> getIntervalos() {
		return java.util.Collections.unmodifiableList(temposAula);
	}

	public void definirHabilitacoes(Set<Disciplina> habilitacoes) {
		this.habilitacoes.addAll(habilitacoes);
	}

	public void definirTemposAula(List<Intervalo> itens) {
		this.temposAula.addAll(itens);
	}

	public void definirDiasSemana(List<String> dias) {
		diasSemana.addAll(dias);
	}
}
