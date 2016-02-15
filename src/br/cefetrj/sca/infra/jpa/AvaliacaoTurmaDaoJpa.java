package br.cefetrj.sca.infra.jpa;

import org.springframework.stereotype.Component;

import br.cefetrj.sca.dominio.avaliacaoturma.AvaliacaoTurma;
import br.cefetrj.sca.infra.AvaliacaoTurmaDao;

@Component
public class AvaliacaoTurmaDaoJpa extends GenericDaoJpa<AvaliacaoTurma>
		implements AvaliacaoTurmaDao {

	@Override
	public AvaliacaoTurma getAvaliacaoTurmaAluno(Long idTurma, String cpf) {
		String consulta = "SELECT a FROM AvaliacaoTurma a "
				+ "WHERE a.turmaAvaliada.id = ? AND a.alunoAvaliador.matricula = ?";
		Object array[] = { idTurma, cpf };
		return super.tentaObterEntidade(consulta, array);
	}
}
