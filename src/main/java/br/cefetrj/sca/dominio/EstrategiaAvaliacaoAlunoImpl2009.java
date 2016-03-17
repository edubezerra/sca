package br.cefetrj.sca.dominio;

import java.math.BigDecimal;

public class EstrategiaAvaliacaoAlunoImpl2009 implements
		EstrategiaAvaliacaoAluno {

	public EstrategiaAvaliacaoAlunoImpl2009() {
	}

	/**
	 * Determina a nota final do aluno na turma correspondente, obtida pela
	 * média simples entre as três notas obtidas.
	 */
	public BigDecimal getNotaFinal(NotaFinal avaliacao) {
		double notaFinal = 0.0;
		notaFinal = (avaliacao.getNotaP1().doubleValue()
				+ avaliacao.getNotaP2().doubleValue() + avaliacao.getNotaP3()
				.doubleValue()) / 3;
		return new BigDecimal(notaFinal);
	}

	/**
	 * Determina o conceito do aluno na turma correspondente. Os resultados
	 * possíveis são "A", "B", "C" e "I".
	 */
	@Override
	public String getConceito(NotaFinal avaliacao) {
		String grau;
		if (avaliacao.getFrequencia().doubleValue() < 0.75)
			grau = "I";
		else {
			BigDecimal notaFinal = getNotaFinal(avaliacao);
			if (notaFinal.doubleValue() >= 9.0)
				grau = "A";
			else if (notaFinal.doubleValue() >= 7.0)
				grau = "B";
			else if (notaFinal.doubleValue() >= 5.0)
				grau = "C";
			else
				grau = "I";
		}
		return grau;
	}

	/**
	 * Determina a situação final da avaliação do aluno na turma correspondente.
	 * Os resultados possíveis são os seguintes: RF (reprovado por faltas), AP
	 * (aprovado) ou RM (reprovado por média).
	 */
	@Override
	public EnumSituacaoAvaliacao getSituacaoFinal(NotaFinal avaliacao) {
		String grau = this.getConceito(avaliacao);
		if (avaliacao == null) {
			return EnumSituacaoAvaliacao.INDEFINIDA;
		} else if (avaliacao.getFrequencia().doubleValue() < 0.75) {
			return EnumSituacaoAvaliacao.REPROVADO_POR_FALTAS;
		} else if (grau.equals("A") || grau.equals("B") || grau.equals("C")) {
			return EnumSituacaoAvaliacao.APROVADO;
		} else {
			return EnumSituacaoAvaliacao.REPROVADO_POR_MEDIA;
		}
	}
}
