package br.cefetrj.sca.infra;

import java.util.List;

import br.cefetrj.sca.dominio.Disciplina;
import br.cefetrj.sca.dominio.inclusaodisciplina.Comprovante;
import br.cefetrj.sca.dominio.isencoes.SolicitacaoIsencaoDisciplinas;

public interface SolicitacaoIsencaoDisciplinasDao {
	
	public boolean adicionarSolicitacao(SolicitacaoIsencaoDisciplinas solicitacaoInclusao);
	SolicitacaoIsencaoDisciplinas getSolicitacaoByAluno(Long alunoId);
	Comprovante getComprovante(Long solicitacaoId);
	Disciplina getDisciplinaSolicitada(Long idAluno, String codigoDisciplina);
	List<SolicitacaoIsencaoDisciplinas> getTodasSolicitacoesByDepartamento(Long departamentoId);
}
