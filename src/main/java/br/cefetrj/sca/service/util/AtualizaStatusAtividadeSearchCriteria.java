package br.cefetrj.sca.service.util;

public class AtualizaStatusAtividadeSearchCriteria {
	
	String matriculaProf;
	String siglaCurso;
	String numeroVersao;
	String status;
	String novoStatus;
	String idRegistro;
	String matriculaAluno;
	String justificativa;
	String startDate;
	String endDate;
	
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
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getMatriculaProf() {
		return matriculaProf;
	}
	public void setMatriculaProf(String matriculaProf) {
		this.matriculaProf = matriculaProf;
	}
}
