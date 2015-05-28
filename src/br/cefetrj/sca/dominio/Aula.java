package br.cefetrj.sca.dominio;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Aula {

	@Id
	@GeneratedValue
	private Long id;

	@Enumerated(EnumType.ORDINAL)
	private EnumDiaSemana dia;

	@Embedded
	private Intervalo intervalo;

	@SuppressWarnings("unused")
	private Aula() {
	}

	public Aula(EnumDiaSemana dia, String strInicio, String strFim) {
		super();
		this.dia = dia;
		this.intervalo = new Intervalo(strInicio, strFim);
	}

	public EnumDiaSemana getDia() {
		return dia;
	}

	public String getHoraInicio() {
		return intervalo.getInicio();
	}

	public String getHoraTermino() {
		return intervalo.getFim();
	}

	public Long getId() {
		return id;
	}

}