package br.cefetrj.sca.dominio;

import org.springframework.beans.factory.annotation.Autowired;

public class FichaLancamentoAvaliacaoFabrica {
	
	@Autowired
	private static TurmaRepositorio turmaRepositorio;

	public static FichaLancamentoAvaliacao criar(String codTurma) {
		Turma turma = turmaRepositorio.getByCodigoNoPeriodoLetivo(codTurma, PeriodoLetivo.PERIODO_CORRENTE);
		FichaLancamentoAvaliacao ficha = new FichaLancamentoAvaliacao(turma);
		return ficha;
	}
}
