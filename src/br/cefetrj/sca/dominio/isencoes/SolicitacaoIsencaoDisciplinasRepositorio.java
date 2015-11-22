package br.cefetrj.sca.dominio.isencoes;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.cefetrj.sca.dominio.Disciplina;
import br.cefetrj.sca.dominio.inclusaodisciplina.Comprovante;
import br.cefetrj.sca.infra.SolicitacaoIsencaoDisciplinasDao;

@Component
public class SolicitacaoIsencaoDisciplinasRepositorio {

	@Autowired
	private SolicitacaoIsencaoDisciplinasDao inclusaoDao;

	public void adicionarSolicitacaoInclusao(
			SolicitacaoIsencaoDisciplinas solicitacaoInclusao) {
		inclusaoDao.adicionarSolicitacao(solicitacaoInclusao);
	}

	public SolicitacaoIsencaoDisciplinas getSolicitacaoByAluno(Long idAluno) {
		return inclusaoDao.getSolicitacaoByAluno(idAluno);
	}

	public Comprovante getComprovante(Long solicitacaoId) {
		return inclusaoDao.getComprovante(solicitacaoId);
	}

	public Disciplina getDisciplinaSolicitada(Long idAluno, String codigoDisciplina) {
		return inclusaoDao.getDisciplinaSolicitada(idAluno, codigoDisciplina);
	}

	public List<SolicitacaoIsencaoDisciplinas> getTodasSolicitacoesByDepartamento(
			Long departamentoId) {
		return inclusaoDao.getTodasSolicitacoesByDepartamento(departamentoId);
	}
}
