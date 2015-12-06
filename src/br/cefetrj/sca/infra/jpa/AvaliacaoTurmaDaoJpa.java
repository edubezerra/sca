package br.cefetrj.sca.infra.jpa;

import org.springframework.stereotype.Component;

import br.cefetrj.sca.dominio.avaliacaoturma.AvaliacaoTurma;
import br.cefetrj.sca.infra.AvaliacaoTurmaDao;

@Component
public class AvaliacaoTurmaDaoJpa extends GenericDaoJpa<AvaliacaoTurma>
		implements AvaliacaoTurmaDao {

	@Override
	public AvaliacaoTurma getAvaliacaoTurmaAluno(String codigoTurma,
			String cpf) {
		String consulta = "SELECT a FROM AvaliacaoTurma a "
				+ "WHERE a.turmaAvaliada.codigo = ? AND a.alunoAvaliador.cpf = ?";
		Object array[] = { codigoTurma, cpf };
		return super.tentaObterEntidade(consulta, array);
	}
}
