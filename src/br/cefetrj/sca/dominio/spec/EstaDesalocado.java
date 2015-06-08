package br.cefetrj.sca.dominio.spec;

import br.cefetrj.sca.dominio.LocalAula;

public class EstaDesalocado extends CompositeSpecification<LocalAula> {

	@Override
	public boolean isSatisfiedBy(LocalAula local) {
		// TODO: um local de aula est[a desalocado sem não está associado a nenhuma aula.
		return true;
	}
}
