package br.cefetrj.sca.infra.jpa;

import org.springframework.stereotype.Component;

import br.cefetrj.sca.dominio.AvaliacaoEgresso;
import br.cefetrj.sca.infra.AvaliacaoEgressoDao;

@Component
public class AvaliacaoEgressoDaoJpa extends GenericDaoJpa<AvaliacaoEgresso>
		implements AvaliacaoEgressoDao {

	@Override
	public AvaliacaoEgresso getAvaliacaoEgresso(String cpf) {
		String consulta = "SELECT a FROM AvaliacaoEgresso a "
				+ "WHERE a.alunoAvaliador.cpf = ?";
		Object array[] = { cpf };
		return super.tentaObterEntidade(consulta, array);
	}

}
