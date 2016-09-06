package br.cefetrj.sca.dominio.isencoes;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import br.cefetrj.sca.dominio.Disciplina;
import br.cefetrj.sca.dominio.matriculaforaprazo.Comprovante;

@Entity
public class ItemIsencaoDisciplina {
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
	private Long id;

	private String situacao;
	private Date dataAnalise;
	private String motivo;
	private String observacao;
	private String disciplinaAssociada;

	@ManyToOne
	Disciplina disciplina;

	@OneToOne(cascade = { CascadeType.ALL })
	Comprovante comprovante;

	@SuppressWarnings("unused")
	private ItemIsencaoDisciplina() {
	}

	public ItemIsencaoDisciplina(Disciplina disciplina) {
		this.disciplina = disciplina;
	}

	public String getSituacao() {
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	public Date getDataAnalise() {
		return dataAnalise;
	}

	public void setDataAnalise(Date dataAnalise) {
		this.dataAnalise = dataAnalise;
	}

	public Disciplina getDisciplina() {
		return disciplina;
	}

	public void setDisciplina(Disciplina disciplina) {
		this.disciplina = disciplina;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Comprovante getComprovante() {
		return comprovante;
	}

	public void setComprovante(String contentType, byte[] data, String nome) {
		this.comprovante = new Comprovante(contentType, data, nome);
	}

	public String getMotivo() {
		return motivo;
	}

	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public String getDisciplinaAssociada() {
		return disciplinaAssociada;
	}

	public void setDisciplinaAssociada(String disciplinaAssociada) {
		this.disciplinaAssociada = disciplinaAssociada;
	}

}
