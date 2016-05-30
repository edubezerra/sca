package br.cefetrj.sca.dominio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.cefetrj.sca.dominio.repositories.TurmaRepositorio;

@Component
public class FichaAvaliacoesFabrica {

	@Autowired
	private TurmaRepositorio turmaRepositorio;

	public FichaAvaliacoes criar(Long idTurma) {
		Turma turma = turmaRepositorio
				.findTurmaById(idTurma);
		FichaAvaliacoes ficha = new FichaAvaliacoes(turma);
		return ficha;
	}
}
