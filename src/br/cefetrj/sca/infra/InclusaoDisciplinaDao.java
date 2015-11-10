package br.cefetrj.sca.infra;

import java.util.List;

import br.cefetrj.sca.dominio.SemestreLetivo.EnumPeriodo;
import br.cefetrj.sca.dominio.Turma;
import br.cefetrj.sca.dominio.inclusaodisciplina.Comprovante;
import br.cefetrj.sca.dominio.inclusaodisciplina.ItemSolicitacao;
import br.cefetrj.sca.dominio.inclusaodisciplina.SolicitacaoInclusao;

public interface InclusaoDisciplinaDao {
	
	boolean adicionarSolicitacaoInclusao(SolicitacaoInclusao solicitacaoInclusao);
	boolean adicionarItemSolicitacao(ItemSolicitacao itemSolicitacao);
	List<SolicitacaoInclusao> getSolicitacaoAluno(Long idAluno);
	SolicitacaoInclusao getSolicitacaoByAlunoSemestre(Long alunoId,int ano, EnumPeriodo periodo);
	Comprovante getComprovante(Long solicitacaoId);
	Turma getTurmaSolicitada(Long idAluno, String codigoTurma,int ano, EnumPeriodo periodo);
	List<SolicitacaoInclusao> getTodasSolicitacoesBySemestre(EnumPeriodo periodo, int ano);
	List<SolicitacaoInclusao> getTodasSolicitacoes();
	List<SolicitacaoInclusao> getTodasSolicitacoesByDepartamentoSemestre(EnumPeriodo periodo, int ano, Long departamentoId);
	boolean alterarItemSolicitacao(ItemSolicitacao itemSolicitacao);
	ItemSolicitacao getItemSolicitacaoById(Long id);
}
