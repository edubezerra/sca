package br.cefetrj.sca.dominio;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;

import org.springframework.beans.factory.annotation.Autowired;

import br.cefetrj.sca.dominio.FichaAvaliacoes.ItemFicha;

@Entity
public class NotaFinal {

	@Id
	@GeneratedValue
	private Long id;

	private BigDecimal notaP1;
	private BigDecimal notaP2;
	private BigDecimal notaP3;
	private BigDecimal frequencia;

	@Transient
	@Autowired
	private EstrategiaAvaliacaoAluno estrategia;

	@SuppressWarnings("unused")
	private NotaFinal() {
	}

	public NotaFinal(ItemFicha item) {
		this.setNotaP1(item.notaP1);
		this.setNotaP2(item.notaP2);
		this.setNotaP3(item.notaP3);
		this.setFrequencia(item.getFrequencia());
	}

	public Long getId() {
		return id;
	}

	public BigDecimal getNotaP1() {
		return notaP1;
	}

	public void setNotaP1(BigDecimal nota) {
		validarSeNaoNulo(nota, "P1");
		this.notaP1 = nota;
	}

	private void validarSeNaoNulo(BigDecimal nota, String descritorNota) {
		if (nota != null && (nota.doubleValue() < 0.0 || nota.doubleValue() > 10.0)) {
			throw new IllegalArgumentException("Valor fornecido para " + descritorNota + " é inválido.");
		}
	}

	public BigDecimal getNotaP2() {
		return notaP2;
	}

	public void setNotaP2(BigDecimal nota) {
		validarSeNaoNulo(nota, "P2");
		this.notaP2 = nota;
	}

	public BigDecimal getNotaP3() {
		return notaP3;
	}

	public void setNotaP3(BigDecimal nota) {
		validarSeNaoNulo(nota, "P3");
		this.notaP3 = nota;
	}

	public BigDecimal getFrequencia() {
		return frequencia;
	}

	public void setFrequencia(BigDecimal frequencia) {
		this.frequencia = frequencia;
	}

	public EnumSituacaoAvaliacao getSituacaoFinal() {
		return estrategia.getSituacaoFinal(this);
	}

	public String getConceito() {
		return estrategia.getConceito(this);
	}

	public BigDecimal getNotaFinal() {
		return estrategia.getNotaFinal(this);
	}
}
