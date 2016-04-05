package br.cefetrj.sca.service.util;

public class RegistrosAtividadeSearchCriteria {
	
	String siglaCurso;
	String numeroVersao;
	String status;
	
	public String getSiglaCurso() {
		return siglaCurso;
	}
	public String getNumeroVersao() {
		return numeroVersao;
	}
	public String getStatus() {
		return status;
	}
	public void setSiglaCurso(String siglaCurso) {
		this.siglaCurso = siglaCurso;
	}
	public void setNumeroVersao(String numeroVersao) {
		this.numeroVersao = numeroVersao;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
}
