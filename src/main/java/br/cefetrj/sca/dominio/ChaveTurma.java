package br.cefetrj.sca.dominio;

public class ChaveTurma {

	private String codigoTurma;
	private String nomeDisciplina;
	private PeriodoLetivo periodoLetivo;

	public ChaveTurma(String codigoTurma, String nomeDisciplina,
			PeriodoLetivo periodoLetivo) {
		this.codigoTurma = codigoTurma;
		this.nomeDisciplina = nomeDisciplina;
		this.periodoLetivo = periodoLetivo;
	}

	public String getCodigoTurma() {
		return codigoTurma;
	}

	public String getNomeDisciplina() {
		return nomeDisciplina;
	}

	public PeriodoLetivo getPeriodoLetivo() {
		return periodoLetivo;
	}
}
