package br.cefetrj.sca.infra;

import br.cefetrj.sca.dominio.AvaliacaoEgresso;

public interface AvaliacaoEgressoDao {
	AvaliacaoEgresso getAvaliacaoEgresso(String cpf);
	boolean incluir(AvaliacaoEgresso avaliacao);
}
