package br.cefetrj.sca.dominio.inclusaodisciplina;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import br.cefetrj.sca.dominio.Aluno;
import br.cefetrj.sca.dominio.SemestreLetivo;

@Entity
public class SolicitacaoInclusao {
	
	@Id
	@GeneratedValue
	private Long id;
	
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "SOLICITACAO_INCLUSAO_ID", referencedColumnName = "ID")
	private List<ItemSolicitacao> itemSolicitacao;
	
	@ManyToOne
	@JoinColumn(nullable = false)
	private Aluno aluno;

	private SemestreLetivo semestreLetivo;
	
	@SuppressWarnings("unused")
	private SolicitacaoInclusao(){
	}
	
	public SolicitacaoInclusao(List<ItemSolicitacao> itemSolicitacao,
			Aluno aluno, SemestreLetivo semestreLetivo) {
		if (itemSolicitacao == null || aluno == null || semestreLetivo == null) {
			throw new IllegalArgumentException(
					"Erro: argumentos inválidos para SolicitacaoInclusao().");
		}
		
		this.itemSolicitacao = new ArrayList<>();
		this.itemSolicitacao.addAll(itemSolicitacao);
		this.aluno = aluno;
		this.semestreLetivo = semestreLetivo;
	}

	public Long getId() {
		return id;
	}

	public List<ItemSolicitacao> getItemSolicitacao() {
		return itemSolicitacao;
	}

	public Aluno getAluno() {
		return aluno;
	}

	public SemestreLetivo getSemestreLetivo() {
		return semestreLetivo;
	}
	
	public void addItemSolicitacao(List<ItemSolicitacao> itens){
		this.itemSolicitacao.addAll(itens);
	}
	
	
	
	
	

}
