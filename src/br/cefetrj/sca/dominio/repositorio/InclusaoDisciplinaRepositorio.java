package br.cefetrj.sca.dominio.repositorio;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.cefetrj.sca.dominio.SemestreLetivo.EnumPeriodo;
import br.cefetrj.sca.dominio.Turma;
import br.cefetrj.sca.dominio.inclusaodisciplina.Comprovante;
import br.cefetrj.sca.dominio.inclusaodisciplina.ItemSolicitacao;
import br.cefetrj.sca.dominio.inclusaodisciplina.SolicitacaoInclusao;
import br.cefetrj.sca.infra.InclusaoDisciplinaDao;

@Component
public class InclusaoDisciplinaRepositorio {
	
	@Autowired
	private InclusaoDisciplinaDao inclusaoDao;
	
	public void adicionarSolicitacaoInclusao(SolicitacaoInclusao solicitacaoInclusao){
		inclusaoDao.adicionarSolicitacaoInclusao(solicitacaoInclusao);
	}
	
	public void adicionarItemSolicitacao(ItemSolicitacao itemSolicitacao){
		inclusaoDao.adicionarItemSolicitacao(itemSolicitacao);
	}
	
	public List<SolicitacaoInclusao> getSolicitacoesAluno(Long idAluno) {
		return inclusaoDao.getSolicitacaoAluno(idAluno);
	}

	public SolicitacaoInclusao getSolicitacaoByAlunoSemestre(Long alunoId,
			int ano, EnumPeriodo periodo) {
		return inclusaoDao.getSolicitacaoByAlunoSemestre(alunoId, ano, periodo);
	}
	
	public Comprovante getComprovante(Long solicitacaoId){
		return inclusaoDao.getComprovante(solicitacaoId);
	}
	
	public Turma getTurmaSolicitada(Long idAluno, String codigoTurma, int ano, EnumPeriodo periodo){
		return inclusaoDao.getTurmaSolicitada(idAluno, codigoTurma, ano, periodo);
	}
	
	public List<SolicitacaoInclusao> getTodasSolicitacoesBySemestre(EnumPeriodo periodo, int ano){
		return inclusaoDao.getTodasSolicitacoesBySemestre(periodo, ano);
	}
	
	public List<SolicitacaoInclusao> getTodasSolicitacoesByDepartamentoSemestre(EnumPeriodo periodo, int ano, Long departamentoId){
		return inclusaoDao.getTodasSolicitacoesByDepartamentoSemestre(periodo, ano, departamentoId);
	}
	
	public List<SolicitacaoInclusao> getTodasSolicitacoes(){
		return inclusaoDao.getTodasSolicitacoes();
	}
	
	public ItemSolicitacao getItemSolicitacaoById(Long id){
		return inclusaoDao.getItemSolicitacaoById(id);
	}
	
	public boolean alterarItemSolcitacao(ItemSolicitacao itemSolicitacao){
		return inclusaoDao.alterarItemSolicitacao(itemSolicitacao);
	}

}
