package br.cefetrj.sca.infra;

import br.cefetrj.sca.dominio.avaliacaoturma.AvaliacaoTurma;

public interface AvaliacaoDao {

	boolean incluir(AvaliacaoTurma avaliacao);

	AvaliacaoTurma getAvaliacaoTurmaAluno(String codigoTurma, String matricula);

}
