package br.cefetrj.sca.dominio.spec;

import br.cefetrj.sca.dominio.LocalAula;
import br.cefetrj.sca.dominio.SemestreLetivo;

public class EstaDesalocado extends CompositeSpecification<LocalAula> {

	@Override
	public boolean isSatisfiedBy(LocalAula local, SemestreLetivo periodo) {
		// TODO: um local de aula está desalocado se 
		// não está associado a nenhuma aula.
		return true;
	}
}
