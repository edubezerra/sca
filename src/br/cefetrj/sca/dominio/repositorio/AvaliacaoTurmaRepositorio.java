package br.cefetrj.sca.dominio.repositorio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.cefetrj.sca.dominio.avaliacaoturma.AvaliacaoTurma;
import br.cefetrj.sca.infra.AvaliacaoDao;

@Component
public class AvaliacaoTurmaRepositorio {

	@Autowired
	private AvaliacaoDao avaliacaoDAO;

	private AvaliacaoTurmaRepositorio() {
	}

	public AvaliacaoTurma getAvaliacaoTurma(String codigoTurma, String matricula) {
		return avaliacaoDAO.getAvaliacaoTurmaAluno(codigoTurma, matricula);
	}

	public void adicionar(AvaliacaoTurma avaliacao) {
		avaliacaoDAO.incluir(avaliacao);
	}
}
