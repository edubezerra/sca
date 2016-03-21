package br.cefetrj.sca.dominio;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class ItemHorario {
	@Id
	@GeneratedValue
	private Long id;

	@Embedded
	private Intervalo tempoAula;

	@Enumerated(EnumType.ORDINAL)
	EnumDiaSemana dia;

	@SuppressWarnings("unused")
	private ItemHorario() {
	}

	public ItemHorario(EnumDiaSemana dia, String fim, String inicio) {
		tempoAula = new Intervalo(inicio, fim);
		this.dia = dia;
	}

	public Long getId() {
		return id;
	}

	public String getFim() {
		return this.tempoAula.getFim();
	}

	public String getInicio() {
		return this.tempoAula.getInicio();
	}

	public boolean colide(ItemHorario item) {
		return this.tempoAula.colide(item.tempoAula);
	}

	public static List<Intervalo> temposAula() {
		List<Intervalo> intervalosList = new ArrayList<Intervalo>();

		intervalosList.add(new Intervalo("07:00", "07:50"));
		intervalosList.add(new Intervalo("07:55", "08:45"));
		intervalosList.add(new Intervalo("08:50", "09:40"));
		intervalosList.add(new Intervalo("09:55", "10:45"));
		intervalosList.add(new Intervalo("10:50", "11:40"));
		intervalosList.add(new Intervalo("11:45", "12:35"));
		intervalosList.add(new Intervalo("12:40", "13:30"));
		intervalosList.add(new Intervalo("13:35", "14:25"));
		intervalosList.add(new Intervalo("14:30", "15:20"));
		intervalosList.add(new Intervalo("15:35", "16:25"));
		intervalosList.add(new Intervalo("16:30", "17:20"));
		intervalosList.add(new Intervalo("17:25", "18:15"));
		intervalosList.add(new Intervalo("18:20", "19:10"));
		intervalosList.add(new Intervalo("19:10", "20:00"));
		intervalosList.add(new Intervalo("20:00", "20:50"));
		intervalosList.add(new Intervalo("21:00", "21:50"));
		intervalosList.add(new Intervalo("21:50", "22:40"));

		return intervalosList;
	}

}
