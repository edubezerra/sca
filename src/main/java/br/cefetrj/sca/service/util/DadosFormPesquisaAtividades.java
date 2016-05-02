package br.cefetrj.sca.service.util;

import java.util.Set;

import br.cefetrj.sca.dominio.atividadecomplementar.EnumEstadoAtividadeComplementar;

public class DadosFormPesquisaAtividades {
		private String nomeProfessor;
		private String matriculaProfessor;
		private Set<String> siglaCursos;
		private EnumEstadoAtividadeComplementar[] status;
		
		public DadosFormPesquisaAtividades(String nomeProfessor, String matriculaProfessor, Set<String> siglaCursos,
				EnumEstadoAtividadeComplementar[] status) {
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

		public EnumEstadoAtividadeComplementar[] getStatus() {
			return status;
		}

		public void setStatus(EnumEstadoAtividadeComplementar[] status) {
			this.status = status;
		}				
}
