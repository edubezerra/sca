package br.cefetrj.sca.infra;

import br.cefetrj.sca.dominio.avaliacaoturma.AvaliacaoTurma;

public interface AvaliacaoTurmaDao {

	boolean incluir(AvaliacaoTurma avaliacao);
	AvaliacaoTurma getAvaliacaoTurmaAluno(Long idTurma, String matricula);
}
