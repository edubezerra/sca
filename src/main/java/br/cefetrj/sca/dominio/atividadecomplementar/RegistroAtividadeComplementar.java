package br.cefetrj.sca.dominio.atividadecomplementar;

import java.time.Duration;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.cefetrj.sca.dominio.Professor;
import br.cefetrj.sca.dominio.matriculaforaprazo.Comprovante;
import br.cefetrj.sca.service.util.EnumEstadoSolicitacao;


/**
 * Representa o registro de uma atividade complementar de um aluno.
 * 
 * @author Rebecca Salles
 * 
 */
@Entity
public class RegistroAtividadeComplementar {

	@Id
	@GeneratedValue
	private Long id;

	@Enumerated(EnumType.ORDINAL)
	EnumEstadoSolicitacao estado;
	
	@OneToOne(cascade = CascadeType.ALL)
	Comprovante documento;
	
	private String descricao;
	
	private Duration cargaHoraria;
	
	@ManyToOne
	private AtividadeComplementar atividade;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataSolicitacao;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataAnalise = null;
	
	@ManyToOne
	private Professor avaliador = null;
	
	private String justificativa = null;

	@SuppressWarnings("unused")
	private RegistroAtividadeComplementar() {
	}

	public RegistroAtividadeComplementar(Date dataSolicitacao, AtividadeComplementar atividade, Duration ch, Comprovante doc) {
		
		if (dataSolicitacao == null)
			throw new IllegalArgumentException("Data da solicitação não pode ser nula.");
		if (atividade == null)
			throw new IllegalArgumentException("Atividade Complementar não pode ser nula.");
		if (ch == null || ch.isZero() || ch.isNegative())
			throw new IllegalArgumentException("Carga horária não pode ser nula e deve ser maior do que zero.");
		if (doc == null)
			throw new IllegalArgumentException("Comprovante de atividade complementar não pode ser nulo.");
		this.dataSolicitacao = dataSolicitacao;
		this.atividade = atividade;
		this.cargaHoraria = ch;
		this.documento = doc;
		this.estado = EnumEstadoSolicitacao.SUBMETIDO;
	}
	
	public RegistroAtividadeComplementar(Date dataSolicitacao, AtividadeComplementar atividade, String descr, Duration ch, Comprovante doc) {
		this(dataSolicitacao,atividade,ch,doc);
		this.descricao = descr;
	}

	public AtividadeComplementar getAtividade() {
		return atividade;
	}

	public Long getId() {
		return id;
	}

	public EnumEstadoSolicitacao getEstado() {
		return estado;
	}
	
	public void setEstado(EnumEstadoSolicitacao estado) {
		this.estado = estado;
	}

	public Comprovante getDocumento() {
		return documento;
	}

	public String getDescricao() {
		return descricao;
	}

	public Duration getCargaHoraria() {
		return cargaHoraria;
	}
	
	public Date getDataSolicitacao() {
		return dataSolicitacao;
	}
	
	public Date getDataAnalise() {
		return dataAnalise;
	}

	public void setDataAnalise(Date dataAnalise) {
		this.dataAnalise = dataAnalise;
	}

	public Professor getAvaliador() {
		return avaliador;
	}

	public void setAvaliador(Professor avaliador) {
		this.avaliador = avaliador;
	}

	public String getJustificativa() {
		return justificativa;
	}

	public void setJustificativa(String justificativa) {
		this.justificativa = justificativa;
	}
	
	/**
	 * Checa se o registro de atividade complementar pode ter a submissão cancelada.
	 * 
	 * RN08: A submissão do registro só pode ser cancelada se ele estiver com o estado "SUBMETIDO"
	 */
	public boolean podeSerCancelado(){
		if(this.estado != EnumEstadoSolicitacao.SUBMETIDO){
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((atividade == null) ? 0 : atividade.hashCode());
		result = prime * result + ((documento == null) ? 0 : documento.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RegistroAtividadeComplementar other = (RegistroAtividadeComplementar) obj;
		if (atividade == null) {
			if (other.atividade != null)
				return false;
		} else if (!atividade.equals(other.atividade))
			return false;
		if (documento == null) {
			if (other.documento != null)
				return false;
		} else if (!documento.equals(other.documento))
			return false;
		return true;
	}
}
