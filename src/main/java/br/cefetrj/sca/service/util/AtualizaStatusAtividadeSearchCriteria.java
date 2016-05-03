package br.cefetrj.sca.service.util;

public class AtualizaStatusAtividadeSearchCriteria {
	
	String siglaCurso;
	String numeroVersao;
	String status;
	String novoStatus;
	String IdReg;
	String matriculaAluno;
	
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
	public String getIdReg() {
		return IdReg;
	}
	public void setIdReg(String idReg) {
		IdReg = idReg;
	}
	public String getMatriculaAluno() {
		return matriculaAluno;
	}
	public void setMatriculaAluno(String matriculaAluno) {
		this.matriculaAluno = matriculaAluno;
	}
	
}
