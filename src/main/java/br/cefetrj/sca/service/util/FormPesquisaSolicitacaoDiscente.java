package br.cefetrj.sca.service.util;

import java.util.Set;

/**
 * Essa classe representa dados para a construção do componente de pesquisa
 * usado em formulários de solicitações realizadas por alunos. Dois exemplos de
 * solicitação discente são (1) pedidos de registro de atividades complementares
 * e (2) pedidos de isenção de disciplinas.
 */
public class FormPesquisaSolicitacaoDiscente {
	private String nomeProfessor;
	private String matriculaProfessor;
	private Set<String> siglaCursos;
	private EnumEstadoSolicitacao[] status;

	public FormPesquisaSolicitacaoDiscente(String nomeProfessor, String matriculaProfessor, Set<String> siglaCursos,
			EnumEstadoSolicitacao[] status) {
		super();
		this.nomeProfessor = nomeProfessor;
		this.matriculaProfessor = matriculaProfessor;
		this.siglaCursos = siglaCursos;
		this.status = status;
	}

	public String getNomeProfessor() {
		return nomeProfessor;
	}

	public void setNomeProfessor(String nomeProfessor) {
		this.nomeProfessor = nomeProfessor;
	}

	public String getMatriculaProfessor() {
		return matriculaProfessor;
	}

	public void setMatriculaProfessor(String matriculaProfessor) {
		this.matriculaProfessor = matriculaProfessor;
	}

	public Set<String> getSiglaCursos() {
		return siglaCursos;
	}

	public void setSiglaCursos(Set<String> siglaCursos) {
		this.siglaCursos = siglaCursos;
	}

	public EnumEstadoSolicitacao[] getStatus() {
		return status;
	}

	public void setStatus(EnumEstadoSolicitacao[] status) {
		this.status = status;
	}
}
