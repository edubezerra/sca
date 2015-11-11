package br.cefetrj.sca.infra;

import java.util.List;

import br.cefetrj.sca.dominio.SemestreLetivo.EnumPeriodo;
import br.cefetrj.sca.dominio.Turma;
import br.cefetrj.sca.dominio.inclusaodisciplina.Comprovante;
import br.cefetrj.sca.dominio.inclusaodisciplina.ItemSolicitacaoMatriculaForaPrazo;
import br.cefetrj.sca.dominio.inclusaodisciplina.SolicitacaoMatriculaForaPrazo;

public interface InclusaoDisciplinaDao {
	
	boolean adicionarSolicitacaoInclusao(SolicitacaoMatriculaForaPrazo solicitacaoInclusao);
	boolean adicionarItemSolicitacao(ItemSolicitacaoMatriculaForaPrazo itemSolicitacao);
	List<SolicitacaoMatriculaForaPrazo> getSolicitacaoAluno(Long idAluno);
	SolicitacaoMatriculaForaPrazo getSolicitacaoByAlunoSemestre(Long alunoId,int ano, EnumPeriodo periodo);
	Comprovante getComprovante(Long solicitacaoId);
	Turma getTurmaSolicitada(Long idAluno, String codigoTurma,int ano, EnumPeriodo periodo);
	List<SolicitacaoMatriculaForaPrazo> getTodasSolicitacoesBySemestre(EnumPeriodo periodo, int ano);
	List<SolicitacaoMatriculaForaPrazo> getTodasSolicitacoes();
	List<SolicitacaoMatriculaForaPrazo> getTodasSolicitacoesByDepartamentoSemestre(EnumPeriodo periodo, int ano, Long departamentoId);
	boolean alterarItemSolicitacao(ItemSolicitacaoMatriculaForaPrazo itemSolicitacao);
	ItemSolicitacaoMatriculaForaPrazo getItemSolicitacaoById(Long id);
}
