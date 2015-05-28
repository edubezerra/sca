package br.cefetrj.sca.dominio;

import java.math.BigDecimal;

public interface EstrategiaAvaliacaoAluno {
	public String getConceito(Aproveitamento aproveitamento);

	public EnumSituacaoFinalAvaliacao getSituacaoFinal(
			Aproveitamento aproveitamento);

	public BigDecimal getNotaFinal(Aproveitamento aproveitamento);
}
