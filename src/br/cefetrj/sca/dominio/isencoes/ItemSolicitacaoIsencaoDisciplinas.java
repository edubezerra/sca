package br.cefetrj.sca.dominio.isencoes;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import br.cefetrj.sca.dominio.Disciplina;

@Entity
public class ItemSolicitacaoIsencaoDisciplinas {
	public enum SituacaoItem {
		INDEFINIDO("INDEFINIDO"), DEFERIDO("DEFERIDO"), INDEFERIDO("INDEFERIDO");

		private String value;

		private SituacaoItem(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}
	}

	@Id
	@GeneratedValue
	private long id;

	@ManyToOne
	@JoinColumn(name = "solicitacaoId", nullable = false)
	private SolicitacaoIsencaoDisciplinas solicitacao;

	@ManyToOne
	@JoinColumn(name = "disciplinaId", nullable = false)
	private Disciplina disciplina;

	private String nomeDisciplinaExterna;

	private String codigoDisciplinaExterna;

	@Enumerated(EnumType.STRING)
	private SituacaoItem situacao;

	public ItemSolicitacaoIsencaoDisciplinas(Disciplina disciplina) {
		this.disciplina  = disciplina;
		this.situacao = SituacaoItem.INDEFINIDO;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public SolicitacaoIsencaoDisciplinas getSolicitacao() {
		return solicitacao;
	}

	public void setSolicitacao(SolicitacaoIsencaoDisciplinas solicitacao) {
		this.solicitacao = solicitacao;
	}

	public Disciplina getDisciplina() {
		return disciplina;
	}

	public void setDisciplina(Disciplina disciplina) {
		this.disciplina = disciplina;
	}

	public String getNomeDisciplinaExterna() {
		return nomeDisciplinaExterna;
	}

	public void setNomeDisciplinaExterna(String nome) {
		this.nomeDisciplinaExterna = nome;
	}

	public String getCodDisciplinaExterna() {
		return codigoDisciplinaExterna;
	}

	public void setCodDisciplinaExterna(String codigo) {
		this.codigoDisciplinaExterna = codigo;
	}

	public SituacaoItem getSituacao() {
		return situacao;
	}

	public void setSituacao(SituacaoItem situacaoItem) {
		this.situacao = situacaoItem;
	}
}
