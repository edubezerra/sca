package br.cefetrj.sca.service.util;

import br.cefetrj.sca.dominio.Curso;

public class SituacaoAlunoAtividades {
	private String nomeAluno;
	private Curso curso;
	private String versaoCurso;
	private boolean temAtividadesSuficientes;
	private String totalCH;

	public SituacaoAlunoAtividades(String nomeAluno, Curso curso,
			String versaoCurso, boolean temAtividadesSuficientes, String totalCH) {
		super();
		this.nomeAluno = nomeAluno;
		this.curso = curso;
		this.versaoCurso = versaoCurso;
		this.temAtividadesSuficientes = temAtividadesSuficientes;
		this.totalCH = totalCH;
	}

	public String getNomeAluno() {
		return nomeAluno;
	}

	public void setNomeAluno(String nomeAluno) {
		this.nomeAluno = nomeAluno;
	}

	public Curso getCurso() {
		return curso;
	}

	public void setCurso(Curso curso) {
		this.curso = curso;
	}

	public String getVersaoCurso() {
		return versaoCurso;
	}

	public void setVersaoCurso(String versaoCurso) {
		this.versaoCurso = versaoCurso;
	}

	public boolean isTemAtividadesSuficientes() {
		return temAtividadesSuficientes;
	}

	public void setTemAtividadesSuficientes(boolean temAtividadesSuficientes) {
		this.temAtividadesSuficientes = temAtividadesSuficientes;
	}

	public String getTotalCH() {
		return totalCH;
	}

	public void setTotalCH(String totalCH) {
		this.totalCH = totalCH;
	}
}
