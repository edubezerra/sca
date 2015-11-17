package br.cefetrj.sca.dominio.repositorio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.cefetrj.sca.dominio.AvaliacaoEgresso;
import br.cefetrj.sca.infra.AvaliacaoEgressoDao;

@Component
public class AvaliacaoEgressoRepositorio {

	@Autowired
	private AvaliacaoEgressoDao avaliacaoDAO;

	private AvaliacaoEgressoRepositorio() {
	}

	public AvaliacaoEgresso getAvaliacaoEgresso(String cpf) {
		return avaliacaoDAO.getAvaliacaoEgresso(cpf);
	}

	public void adicionar(AvaliacaoEgresso avaliacao) {
		avaliacaoDAO.incluir(avaliacao);
	}
}
