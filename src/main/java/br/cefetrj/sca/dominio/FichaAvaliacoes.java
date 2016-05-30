package br.cefetrj.sca.dominio;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class FichaAvaliacoes {
	String nomeProfessor;
	String matriculaProfessor;
	String codigoTurma;
	String nomeDisciplina;
	String codigoDisciplina;

	List<ItemFicha> itensFicha = new ArrayList<>();

	private Turma turma;

	class ItemFicha {
		String nomeAluno;
		String matriculaAluno;
		BigDecimal notaP1;
		BigDecimal notaP2;
		BigDecimal notaP3;
		Integer qtdFaltas;

		public BigDecimal getNP() {
			BigDecimal sum = notaP1.add(notaP2);
			return sum.divide(new BigDecimal(2));
		}

		public BigDecimal getNF() {
			BigDecimal np = getNP();
			if (np.compareTo(new BigDecimal(2)) != -1) {
				return np;
			}
			BigDecimal sum = np.add(notaP3);
			return sum.divide(new BigDecimal(2));
		}

		public EnumSituacaoAvaliacao getStatus() {
			BigDecimal nf = getNF();
			Integer qtdAulas = turma.getQtdAulas();
			if (qtdAulas * 0.75 >= qtdFaltas) {
				return EnumSituacaoAvaliacao.REPROVADO_POR_FALTAS;
			} else if (nf.compareTo(new BigDecimal(5)) == -1) {
				return EnumSituacaoAvaliacao.REPROVADO_POR_MEDIA;
			} else {
				return EnumSituacaoAvaliacao.APROVADO;
			}
		}

		public BigDecimal getFrequencia() {
			BigDecimal qtdAulas = new BigDecimal(turma.getQtdAulas());
			BigDecimal qtdFaltas = new BigDecimal(this.qtdFaltas);
			BigDecimal qtdPresente = qtdAulas.subtract(qtdFaltas);
			return qtdPresente.divide(qtdAulas);
		}
	}

	public FichaAvaliacoes(Turma turma) {
		this.turma = turma;
		Set<Inscricao> inscricoes = turma.getInscricoes();
		for (Inscricao inscricao : inscricoes) {
			Aluno a = inscricao.getAluno();
			ItemFicha item = new ItemFicha();
			item.matriculaAluno = a.getMatricula();
			item.nomeAluno = a.getNome();
			itensFicha.add(item);
		}
	}

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

	public ItemFicha lancar(String matriculaAluno, BigDecimal notaP1,
			BigDecimal notaP2, BigDecimal notaP3, Integer qtdFaltas) {
		for (ItemFicha item : itensFicha) {
			if (item.matriculaAluno.equals(matriculaAluno)) {

				validarNota(notaP1, "P1", matriculaAluno);
				validarNota(notaP2, "P2", matriculaAluno);
				validarNota(notaP3, "P3", matriculaAluno);

				validarFaltas(qtdFaltas, matriculaAluno);

				item.notaP1 = notaP1;
				item.notaP2 = notaP2;
				item.notaP3 = notaP3;
				item.qtdFaltas = qtdFaltas;
			}
			return item;
		}
		throw new IllegalArgumentException("Aluno com matrícula "
				+ matriculaAluno + "não está matriculado nessa turma.");
	}

	private void validarFaltas(Integer qtdFaltas, String matriculaAluno) {
		if (qtdFaltas != null) {
			if (qtdFaltas < 0 || qtdFaltas > turma.getQtdAulas()) {
				throw new IllegalArgumentException(
						"Qtd. de faltas inválida para aluno de matrícula "
								+ matriculaAluno);

			}
		}

	}

	/**
	 * NB: enquanto o lançamento está aberto, quaisquer dos dados fornecidos
	 * para notas podem ser nulos.
	 * 
	 * @param matriculaAluno
	 * 
	 */
	private void validarNota(BigDecimal nota, String nomeNota,
			String matriculaAluno) {
		if (nota != null) {
			if (nota.compareTo(BigDecimal.ONE) < 0
					|| nota.compareTo(BigDecimal.TEN) > 10) {
				throw new IllegalArgumentException("Nota de " + nomeNota
						+ " inválida para aluno de matrícula " + matriculaAluno);
			}
		}
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

	public String getCodigoDisciplina() {
		return this.codigoDisciplina;
	}

	public Long getIdTurma() {
		return this.turma.getId();
	}

	public Turma getTurma() {
		return turma;
	}
}
