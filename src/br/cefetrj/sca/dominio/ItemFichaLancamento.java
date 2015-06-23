package br.cefetrj.sca.dominio;

public class ItemFichaLancamento {

	private String nomeAluno;
	private String matriculaAluno;
	private Aproveitamento aproveitamento;

	public ItemFichaLancamento(String nome, String matricula) {
		this.nomeAluno = nome;
		this.matriculaAluno = matricula;
	}

	public String getNomeAluno() {
		return nomeAluno;
	}

	public String getMatriculaAluno() {
		return matriculaAluno;
	}
}
