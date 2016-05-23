package br.cefetrj.sca.dominio.gradesdisponibilidade;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import br.cefetrj.sca.dominio.Disciplina;
import br.cefetrj.sca.dominio.EnumDiaSemana;
import br.cefetrj.sca.dominio.GradeHorarios;
import br.cefetrj.sca.dominio.IntervaloTemporal;

public class FichaDisponibilidade {
	String nomeProfessor;
	String matriculaProfessor;
	List<Disciplina> habilitacoes = new ArrayList<>();
	List<IntervaloTemporal> temposAula = new ArrayList<>();
	List<String> diasSemana = new ArrayList<>();

	public FichaDisponibilidade(String matricula, String nome) {
		this.matriculaProfessor = matricula;
		this.nomeProfessor = nome;
		this.definirTemposAula();
		this.definirDiasSemana(EnumDiaSemana.dias());
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

	public List<IntervaloTemporal> getIntervalos() {
		return java.util.Collections.unmodifiableList(temposAula);
	}

	public void definirHabilitacoes(Set<Disciplina> habilitacoes) {
		this.habilitacoes.addAll(habilitacoes);
	}

	private void definirTemposAula() {
		this.temposAula.addAll(GradeHorarios.getTemposAula());
	}

	public void definirDiasSemana(List<String> dias) {
		diasSemana.addAll(dias);
	}
}
