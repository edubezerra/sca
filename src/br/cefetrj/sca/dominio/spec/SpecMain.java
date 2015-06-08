package br.cefetrj.sca.dominio.spec;

import java.util.List;

import br.cefetrj.sca.LocalAulaRepositorio;
import br.cefetrj.sca.dominio.LocalAula;

public class SpecMain {

	public static void main(String[] args) {
		int TAMANHO_TURMA = 50;
		LocalAulaRepositorio mRepositorio = new LocalAulaRepositorio();
		Specification<LocalAula> desalocadoAndPossuiEspaco = new EstaDesalocado()
				.and(new PossuiEspaco(TAMANHO_TURMA));
		List<LocalAula> locais = mRepositorio
				.findAllBySpecification(desalocadoAndPossuiEspaco);
		for (LocalAula localAula : locais) {
			System.out.println(localAula.getId() + "\t"
					+ localAula.getCapacidade());
		}
	}

}
