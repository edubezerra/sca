package br.cefetrj.sca.dominio.spec;

import br.cefetrj.sca.dominio.LocalAula;

public class PossuiEspaco extends CompositeSpecification<LocalAula> {

	private Integer capacidadeReferencia;

	public PossuiEspaco(Integer capacidadeReferencia) {
		this.capacidadeReferencia = capacidadeReferencia;
	}

	@Override
	public boolean isSatisfiedBy(LocalAula local) {
		return local.getCapacidade() >= this.capacidadeReferencia;
	}
}
