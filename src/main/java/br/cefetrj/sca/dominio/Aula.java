package br.cefetrj.sca.dominio;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Aula {

	@Id
	@GeneratedValue
	private Long id;

	@Enumerated(EnumType.ORDINAL)
	private EnumDiaSemana dia;

	@Embedded
	private IntervaloTemporal intervalo;

	@ManyToOne
	private LocalAula local;

	@SuppressWarnings("unused")
	private Aula() {
	}

	public Aula(EnumDiaSemana dia, String strInicio, String strFim, LocalAula local) {
		super();
		this.dia = dia;
		this.intervalo = new IntervaloTemporal(strInicio, strFim);
		this.local = local;
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

	public LocalAula getLocal() {
		return this.local;
	}

}