package br.cefetrj.sca.dominio.repositorio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.cefetrj.sca.dominio.avaliacaoturma.AvaliacaoTurma;
import br.cefetrj.sca.infra.AvaliacaoTurmaDao;

@Component
public class AvaliacaoTurmaRepositorio {

	@Autowired
	private AvaliacaoTurmaDao avaliacaoDAO;

	private AvaliacaoTurmaRepositorio() {
	}

	public AvaliacaoTurma getAvaliacaoTurma(Long idTurma, String matricula) {
		return avaliacaoDAO.getAvaliacaoTurmaAluno(idTurma, matricula);
	}

	public void adicionar(AvaliacaoTurma avaliacao) {
		avaliacaoDAO.incluir(avaliacao);
	}
}
