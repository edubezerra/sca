package br.cefetrj.sca.service.util;

public class AtualizaStatusAtividadeSearchCriteria {
	
	String siglaCurso;
	String numeroVersao;
	String status;
	String novoStatus;
	String idRegistro;
	String matriculaAluno;
	String justificativa;
	
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
	public String getNovoStatus() {
		return novoStatus;
	}
	public void setNovoStatus(String novoStatus) {
		this.novoStatus = novoStatus;
	}
	public String getIdRegistro() {
		return idRegistro;
	}
	public void setIdRegistro(String idRegistro) {
		this.idRegistro = idRegistro;
	}
	public String getMatriculaAluno() {
		return matriculaAluno;
	}
	public void setMatriculaAluno(String matriculaAluno) {
		this.matriculaAluno = matriculaAluno;
	}
	public String getJustificativa() {
		return justificativa;
	}
	public void setJustificativa(String justificativa) {
		this.justificativa = justificativa;
	}
	
}
