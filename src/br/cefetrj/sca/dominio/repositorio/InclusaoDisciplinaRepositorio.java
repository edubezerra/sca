package br.cefetrj.sca.dominio.repositorio;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.cefetrj.sca.dominio.PeriodoLetivo.EnumPeriodo;
import br.cefetrj.sca.dominio.Turma;
import br.cefetrj.sca.dominio.inclusaodisciplina.Comprovante;
import br.cefetrj.sca.dominio.inclusaodisciplina.ItemSolicitacaoMatriculaForaPrazo;
import br.cefetrj.sca.dominio.inclusaodisciplina.SolicitacaoMatriculaForaPrazo;
import br.cefetrj.sca.infra.InclusaoDisciplinaDao;

@Component
public class InclusaoDisciplinaRepositorio {
	
	@Autowired
	private InclusaoDisciplinaDao inclusaoDao;
	
	public void adicionarSolicitacaoInclusao(SolicitacaoMatriculaForaPrazo solicitacaoInclusao){
		inclusaoDao.adicionarSolicitacaoInclusao(solicitacaoInclusao);
	}
	
	public void adicionarItemSolicitacao(ItemSolicitacaoMatriculaForaPrazo itemSolicitacao){
		inclusaoDao.adicionarItemSolicitacao(itemSolicitacao);
	}
	
	public List<SolicitacaoMatriculaForaPrazo> getSolicitacoesAluno(Long idAluno) {
		return inclusaoDao.getSolicitacaoAluno(idAluno);
	}

	public SolicitacaoMatriculaForaPrazo getSolicitacaoByAlunoSemestre(Long alunoId,
			int ano, EnumPeriodo periodo) {
		return inclusaoDao.getSolicitacaoByAlunoSemestre(alunoId, ano, periodo);
	}
	
	public Comprovante getComprovante(Long solicitacaoId){
		return inclusaoDao.getComprovante(solicitacaoId);
	}
	
	public Turma getTurmaSolicitada(Long idAluno, String codigoTurma, int ano, EnumPeriodo periodo){
		return inclusaoDao.getTurmaSolicitada(idAluno, codigoTurma, ano, periodo);
	}
	
	public List<SolicitacaoMatriculaForaPrazo> getTodasSolicitacoesBySemestre(EnumPeriodo periodo, int ano){
		return inclusaoDao.getTodasSolicitacoesBySemestre(periodo, ano);
	}
	
	public List<SolicitacaoMatriculaForaPrazo> getTodasSolicitacoesByDepartamentoSemestre(EnumPeriodo periodo, int ano, Long departamentoId){
		return inclusaoDao.getTodasSolicitacoesByDepartamentoSemestre(periodo, ano, departamentoId);
	}
	
	public List<SolicitacaoMatriculaForaPrazo> getTodasSolicitacoes(){
		return inclusaoDao.getTodasSolicitacoes();
	}
	
	public ItemSolicitacaoMatriculaForaPrazo getItemSolicitacaoById(Long id){
		return inclusaoDao.getItemSolicitacaoById(id);
	}
	
	public boolean alterarItemSolcitacao(ItemSolicitacaoMatriculaForaPrazo itemSolicitacao){
		return inclusaoDao.alterarItemSolicitacao(itemSolicitacao);
	}

}
