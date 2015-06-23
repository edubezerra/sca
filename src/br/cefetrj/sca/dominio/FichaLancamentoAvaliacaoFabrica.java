package br.cefetrj.sca.dominio;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import br.cefetrj.sca.dominio.repositorio.TurmaRepositorio;

public class FichaLancamentoAvaliacaoFabrica {
	
	@Autowired
	private static TurmaRepositorio tr;

	public static FichaLancamentoAvaliacao criar(String codTurma) {
		Turma turma = tr.getByCodigo(codTurma);
		FichaLancamentoAvaliacao ficha = new FichaLancamentoAvaliacao(turma);
		return null;
	}
}
