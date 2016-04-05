package br.cefetrj.sca.dominio;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Embeddable;

/**
 * Esta classe fornece funcionalidade para representar intervalos de tempo.
 * 
 * @author <a href="mailto:edubezerra@gmail.com">Eduardo Bezerra</a>
 */
@Embeddable
public class IntervaloTemporal implements Cloneable {
	/**
	 * Formatador usado para trasformar as strings passadas na construção do
	 * objeto em objeto da classe {@link Date} .
	 */
	private static DateFormat formatador;

	/** inicio do intervalo. */
	private Date inicio;

	/** fim do intervalo. */
	private Date fim;

	private IntervaloTemporal() {
		formatador = new SimpleDateFormat("HH:mm");
		formatador.setLenient(false);
	}

	/**
	 * 
	 * @param strInicio
	 *            início do intervalo (e.g., "10:30")
	 * @param strFim
	 *            fim do intervalo (e.g., "12:00")
	 * @throws IllegalArgumentException
	 *             se o início não for anterior ao fim.
	 */
	public IntervaloTemporal(final String strInicio, final String strFim) {
		this();
		try {
			this.inicio = (Date) formatador.parse(strInicio);
			this.fim = (Date) formatador.parse(strFim);
		} catch (ParseException e) {
			throw new IllegalArgumentException("Argumentos inválidos: (" + strInicio + ", " + strFim + ")", e);
		}
		if (inicio.after(fim)) {
			throw new IllegalArgumentException("Início do intervalo deve ser anterior ao fim.");
		}
	}

	/**
	 * 
	 * @param dtInicio
	 *            início do intervalo (e.g., "10:30")
	 * @param dtFim
	 *            fim do intervalo (e.g., "12:00")
	 */
	private IntervaloTemporal(final Date dtInicio, final Date dtFim) {
		if (dtInicio.after(dtFim)) {
			throw new IllegalArgumentException("Início deve ser anterior ao fim.");
		}
		this.inicio = dtInicio;
		this.fim = dtFim;
	}

	@Override
	public Object clone() {
		IntervaloTemporal copia = new IntervaloTemporal((Date) inicio.clone(), (Date) fim.clone());
		return copia;
	}

	/**
	 * Retorna o fim do intervalo.
	 * 
	 * @return o fim do intervalo, no formato hh:mm.
	 */
	public final String getFim() {
		return formatador.format(this.fim);
	}

	/**
	 * Retorna o in�cio do intervalo.
	 * 
	 * @return o in�cio do intervalo, no formato hh:mm
	 */
	public String getInicio() {
		return formatador.format(this.inicio);
	}

	/**
	 * Verifica se há colisão entre dois intervalos.
	 * 
	 * @param outro
	 *            o intervalo com o qual a comparação é feita.
	 * @return true se há colisão entre os intervalos; false em caso contrário.
	 */
	public Boolean colide(final IntervaloTemporal outro) {
		return (outro.inicio.before(this.inicio) && outro.fim.after(this.inicio))
				|| (outro.inicio.before(this.fim) && outro.fim.after(this.fim));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((fim == null) ? 0 : fim.hashCode());
		result = prime * result + ((inicio == null) ? 0 : inicio.hashCode());
		return result;
	}

	/**
	 * Verifica se há colisão dois intervalos são iguais. Dois intervalos são
	 * iguais se eles possuem mesmos início e fim.
	 * 
	 * @param outroIntervalo
	 *            o intervalo com o qual a comparação é feita.
	 * @return true se há colisão entre os intervalos; false em caso contr�rio.
	 */
	@Override
	public boolean equals(Object outroIntervalo) {
		if (this == outroIntervalo) {
			return true;
		}
		if (outroIntervalo == null) {
			return false;
		}
		if (getClass() != outroIntervalo.getClass()) {
			return false;
		}

		IntervaloTemporal other = (IntervaloTemporal) outroIntervalo;
		String inicioA = formatador.format(this.inicio);
		String inicioB = formatador.format(other.inicio);
		String fimA = formatador.format(this.fim);
		String fimB = formatador.format(other.fim);

		if (fim == null) {
			if (other.fim != null) {
				return false;
			}
		} else if (!fimA.equals(fimB)) {
			return false;
		}
		if (inicio == null) {
			if (other.inicio != null) {
				return false;
			}
		} else if (!inicioA.equals(inicioB)) {
			return false;
		}
		return true;
	}

	public IntervaloTemporal unir(IntervaloTemporal outro) {
		IntervaloTemporal primeiro, segundo;
		if (this.inicio.equals(outro.fim)) {
			primeiro = outro;
			segundo = this;
		} else if (outro.inicio.equals(this.fim)) {
			primeiro = this;
			segundo = outro;
		} else {
			throw new IllegalArgumentException(
					"Intervalos não são compatíveis para união.");
		}
		IntervaloTemporal unido = new IntervaloTemporal(primeiro.getInicio(),
				segundo.getFim());
		return unido;
	}

}