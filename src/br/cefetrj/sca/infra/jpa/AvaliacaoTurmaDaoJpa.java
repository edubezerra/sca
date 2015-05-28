package br.cefetrj.sca.infra.jpa;

import org.springframework.stereotype.Component;

import br.cefetrj.sca.dominio.avaliacaoturma.AvaliacaoTurma;
import br.cefetrj.sca.infra.AvaliacaoDao;

@Component
public class AvaliacaoTurmaDaoJpa extends GenericDaoJpa<AvaliacaoTurma>
		implements AvaliacaoDao {

	@Override
	public AvaliacaoTurma getAvaliacaoTurmaAluno(String codigoTurma,
			String matricula) {
		String consulta = "SELECT a from AvaliacaoTurma a WHERE a.turmaAvaliada.codigo = ? AND a.alunoAvaliador.matricula = ?";
		Object array[] = { codigoTurma, matricula };
		return super.tentaObterEntidade(consulta, array);
	}
}
