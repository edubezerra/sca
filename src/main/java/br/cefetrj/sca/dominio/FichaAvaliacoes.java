package br.cefetrj.sca.dominio;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class FichaAvaliacoes {
	String nomeProfessor;
	String matriculaProfessor;
	String codigoTurma;
	String nomeDisciplina;

	class ItemFicha {
		String nomeAluno;
		String matriculaAluno;
		BigDecimal notaP1;
		BigDecimal notaP2;
		BigDecimal notaP3;
		Integer qtdFaltas;
	}

	List<ItemFicha> itensFicha = new ArrayList<>();

	public String getNomeDisciplina() {
		return nomeDisciplina;
	}

	public String getNomeProfessor() {
		return nomeProfessor;
	}

	public String getMatricularProfessor() {
		return matriculaProfessor;
	}

	public String getCodigoTurma() {
		return codigoTurma;
	}

	public void lancar(String matriculaAluno, BigDecimal notaP1, BigDecimal notaP2, BigDecimal notaP3,
			Integer qtdFaltas) {
	}

	public List<ItemFicha> getItensFicha() {
		return itensFicha;
	}

	public void lancarAvaliacoesEmTurma(Turma turma) {
		if (turma != null) {
			for (FichaAvaliacoes.ItemFicha item : this.getItensFicha()) {
				turma.lancarAvaliacao(item);
			}
		}
	}
}
