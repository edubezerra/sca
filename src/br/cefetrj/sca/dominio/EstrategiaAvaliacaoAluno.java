package br.cefetrj.sca.dominio;

import java.math.BigDecimal;

public interface EstrategiaAvaliacaoAluno {
	public String getConceito(NotaFinal aproveitamento);

	public EnumSituacaoFinalAvaliacao getSituacaoFinal(
			NotaFinal aproveitamento);

	public BigDecimal getNotaFinal(NotaFinal aproveitamento);
}
