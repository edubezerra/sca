package br.cefetrj.sca.dominio.isencoes;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import br.cefetrj.sca.dominio.Aluno;

@Entity
public class SolicitacaoIsencaoDisciplinas {
	public enum SituacaoSolicitacao {
		EMANALISE("Em análise"), ANALISADA("Analisada");

		private String value;

		private SituacaoSolicitacao(String situacao) {
			this.value = situacao;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long solicitacaoId;

	private Date dataRealizacao;

	@Enumerated(EnumType.STRING)
	private SituacaoSolicitacao situacao;

	@ManyToOne
	@JoinColumn(name = "alunoId", nullable = false)
	private Aluno aluno;

	@OneToMany(targetEntity = ItemSolicitacaoIsencaoDisciplinas.class)
	private List<ItemSolicitacaoIsencaoDisciplinas> itensSolicitacao;

	public long getSolicitacaoId() {
		return solicitacaoId;
	}

	public void setSolicitacaoId(long solicitacaoId) {
		this.solicitacaoId = solicitacaoId;
	}

	public Date getDataRealizacao() {
		return dataRealizacao;
	}

	public void setDataRealizacao(Date dataRealizacao) {
		this.dataRealizacao = dataRealizacao;
	}

	public Aluno getAluno() {
		return aluno;
	}

	public void setAluno(Aluno aluno) {
		this.aluno = aluno;
	}

	public List<ItemSolicitacaoIsencaoDisciplinas> getItensSolicitacao() {
		return itensSolicitacao;
	}

	public void setItensSolicitacao(List<ItemSolicitacaoIsencaoDisciplinas> itensSolicitacao) {
		this.itensSolicitacao = itensSolicitacao;
	}

	public SituacaoSolicitacao getSituacao() {
		return situacao;
	}

	public void setSituacao(SituacaoSolicitacao situacao) {
		this.situacao = situacao;
	}

	public void adicionarItens(List<ItemSolicitacaoIsencaoDisciplinas> itensSolicitacao) {
		this.itensSolicitacao.addAll(itensSolicitacao);
	}
}
