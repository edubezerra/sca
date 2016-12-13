package br.cefetrj.sca.dominio.isencoes;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import br.cefetrj.sca.dominio.Disciplina;
import br.cefetrj.sca.dominio.Professor;
import br.cefetrj.sca.dominio.matriculaforaprazo.Comprovante;

/**
 * Cada item de isenção pode se encontrar em um de três estados:
 * 
 * INDEFINIDO: quando o item ainda não foi analisado.
 * 
 * DEFERIDO: quando o resultado da análise foi deferir a isenção correspondente.
 * 
 * INDEFERIDO: quando o resultado da análise foi indeferir a isenção
 * correspondente.
 * 
 * @author Eduardo Bezerra
 *
 */
@Entity
public class ItemPedidoIsencaoDisciplina {
	public enum SituacaoItem {
		SUBMETIDO("SUBMETIDO"), DEFERIDO("DEFERIDO"), INDEFERIDO("INDEFERIDO");

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

	/**
	 * Situação em que se encontra este item.
	 */
	private String situacao;

	/*
	 * Data em que este item foi respondido.
	 */
	private Date dataAnalise;

	private String motivo;
	private String observacao;

	/**
	 * Descritor da disciplina externa proposta pelo aluno para usar neste item
	 * do pedido de isenção.
	 */
	private String descritorDisciplinaExterna;

	/**
	 * Disciplina do curso para a qual o aluno deseja obter isenção.
	 */
	@ManyToOne
	Disciplina disciplina;

	/**
	 * Professor responsável pela análise deste item de isenção.
	 */
	@ManyToOne
	Professor professor;

	@OneToOne(cascade = { CascadeType.ALL })
	Comprovante comprovante;

	private String motivoIndeferimento;

	private Date dataCriacao;

	private BigDecimal notaFinalDisciplinaExterna;

	private Duration cargaHorariaDisciplinaExterna;

	private ItemPedidoIsencaoDisciplina() {
	}

	public ItemPedidoIsencaoDisciplina(Disciplina disciplina) {
		this.disciplina = disciplina;
		this.situacao = "SUBMETIDO";
	}

	public ItemPedidoIsencaoDisciplina(Disciplina disciplina, String nomeDisciplinaExterna,
			String notaFinalDisciplinaExterna, String cargaHoraria, String observacao, Comprovante doc) {

		if (disciplina == null) {
			throw new IllegalArgumentException("Erro: disciplina a isentar deve ser fornecida.");
		}

		if (nomeDisciplinaExterna == null || nomeDisciplinaExterna.trim().isEmpty()) {
			throw new IllegalArgumentException("Erro: nome da disciplina externa deve ser fornecido.");
		}

		if (notaFinalDisciplinaExterna == null || notaFinalDisciplinaExterna.trim().isEmpty()) {
			throw new IllegalArgumentException("Erro: nota final obtida na disciplina externa deve ser fornecida.");
		}

		Long ch = 0l;
		try {
			ch = Long.parseLong(cargaHoraria);
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException(
					"Erro: Carga horária inválida. " + "Por favor, forneça um valor válido de carga horária.");
		}
		if (ch <= 0) {
			throw new IllegalArgumentException("Erro: Carga horária inválida. "
					+ "Por favor, forneça um valor de carga horária maior do que zero.");
		}

		if (doc == null) {
			throw new IllegalArgumentException("Erro: comprovante para a isenção deve ser fornecido.");
		}

		this.dataCriacao = Calendar.getInstance().getTime();
		this.disciplina = disciplina;
		this.descritorDisciplinaExterna = nomeDisciplinaExterna;
		this.notaFinalDisciplinaExterna = converterParaBigDecimal(notaFinalDisciplinaExterna);
		this.cargaHorariaDisciplinaExterna = Duration.ofHours(ch);
		this.observacao = observacao;
		this.comprovante = doc;
		this.situacao = "SUBMETIDO";
	}

	private BigDecimal converterParaBigDecimal(String notaFinalDisciplinaExterna) {
		BigDecimal notaFinal;
		try {
			notaFinal = new BigDecimal(notaFinalDisciplinaExterna.replace(',', '.'));
			if (notaFinal.compareTo(BigDecimal.ZERO) < 0 || notaFinal.compareTo(BigDecimal.TEN) > 0) {
				throw new IllegalArgumentException("Erro: Valor inválido para a nota da disciplina externa.");
			}
		} catch (NumberFormatException ex) {
			throw new IllegalArgumentException("Erro: Valor inválido para a nota da disciplina externa.", ex);
		}
		return notaFinal;
	}

	public static void main(String[] args) {
		System.out.println(new ItemPedidoIsencaoDisciplina().converterParaBigDecimal("10.00"));
	}

	public String getSituacao() {
		return situacao;
	}

	public void deferir(Professor professor) {
		if (professor == null) {
			throw new IllegalArgumentException("Professor responsável pela análise deve ser informado!");
		}
		if (!this.situacao.equals("SUBMETIDO"))
			throw new IllegalStateException("Apenas itens não analisados podem ser deferidos.");
		this.professor = professor;
		this.situacao = "DEFERIDO";
	}

	public void indeferir(Professor professor, String motivoIndeferimento) {
		if (professor == null) {
			throw new IllegalArgumentException("Professor responsável pela análise deve ser informado!");
		}
		if (!this.situacao.equals("SUBMETIDO"))
			throw new IllegalStateException("Apenas itens não analisados podem ser indeferidos.");
		this.professor = professor;
		this.situacao = "INDEFERIDO";
		this.motivoIndeferimento = motivoIndeferimento;
	}

	public Date getDataAnalise() {
		return dataAnalise;
	}

	public Disciplina getDisciplina() {
		return disciplina;
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

	public String getDescritorDisciplinaExterna() {
		return descritorDisciplinaExterna;
	}

	public void analisar(Professor professor, String valor, String motivoIndeferimento) {
		if (valor.equals("DEFERIDO")) {
			this.deferir(professor);
		} else if (valor.equals("INDEFERIDO")) {
			this.indeferir(professor, motivoIndeferimento);
		}
		this.dataAnalise = new Date();
	}

	Professor getProfessorResponsavel() {
		return professor;
	}

	public String getMotivoIndeferimento() {
		return motivoIndeferimento;
	}

	public Date getDataSolicitacao() {
		return dataCriacao;
	}

	public BigDecimal getNotaFinalDisciplinaExterna() {
		return notaFinalDisciplinaExterna;
	}

	public Long getCargaHorariaDisciplinaExterna() {
		if (cargaHorariaDisciplinaExterna != null) {
			return cargaHorariaDisciplinaExterna.toHours();
		} else {
			return 0L;
		}
	}
}
