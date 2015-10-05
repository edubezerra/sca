package br.cefetrj.sca.dominio;

import java.util.Calendar;
import java.util.Date;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Transient;

@Embeddable
public final class SemestreLetivo {
	public enum EnumPeriodo {
		PRIMEIRO, SEGUNDO
	};

	final private Integer ano;

	@Enumerated(EnumType.ORDINAL)
	final private EnumPeriodo periodo;

	@Transient
	public static final SemestreLetivo SEMESTRE_LETIVO_CORRENTE;

	/**
	 * Lógica para obtenção do valor do semestre letivo corrente (atual).
	 */
	static {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		EnumPeriodo periodo;
		if (calendar.get(Calendar.MONTH) <= Calendar.JUNE)
			periodo = EnumPeriodo.PRIMEIRO;
		else
			periodo = EnumPeriodo.SEGUNDO;
		SEMESTRE_LETIVO_CORRENTE = new SemestreLetivo(
				calendar.get(Calendar.YEAR), periodo);
	}

	public Integer getAno() {
		return ano;
	}

	public EnumPeriodo getPeriodo() {
		return periodo;
	}

	@SuppressWarnings("unused")
	private SemestreLetivo() {
		this.ano = null;
		this.periodo = null;
	}

	public SemestreLetivo(Integer ano, EnumPeriodo periodo) {
		super();
		this.ano = ano;
		this.periodo = periodo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ano;
		int temp = periodo == EnumPeriodo.PRIMEIRO ? 1 : 2;
		result = prime * result + temp;
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
		SemestreLetivo other = (SemestreLetivo) obj;
		if (ano != other.ano)
			return false;
		if (periodo != other.periodo)
			return false;
		return true;
	}

	@Override
	protected Object clone() {
		return new SemestreLetivo(ano, periodo);
	}

	@Override
	public String toString() {
		return "" + ano + "/" + (periodo.ordinal() + 1);
	}

	public SemestreLetivo proximo() {
		SemestreLetivo outro = new SemestreLetivo(ano, periodo);
		EnumPeriodo outroPeriodo;
		int outroAno;
		if (outro.periodo == EnumPeriodo.PRIMEIRO) {
			outroPeriodo = EnumPeriodo.SEGUNDO;
		} else {
			outroPeriodo = EnumPeriodo.PRIMEIRO;
			outroAno = this.ano + 1;
		}
		return new SemestreLetivo(ano, outroPeriodo);
	}

	public static void main(String args[]) {
		System.out.println(SemestreLetivo.SEMESTRE_LETIVO_CORRENTE);
		System.out.println(SemestreLetivo.SEMESTRE_LETIVO_CORRENTE.proximo());
	}
}
