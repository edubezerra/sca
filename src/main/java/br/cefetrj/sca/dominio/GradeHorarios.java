package br.cefetrj.sca.dominio;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GradeHorarios {

	private static List<IntervaloTemporal> intervalosList = new ArrayList<IntervaloTemporal>();

	public Iterator<IntervaloTemporal> iterator() {
		return new TempoAulaIterator();
	}

	private class TempoAulaIterator implements Iterator<IntervaloTemporal> {

		int indice = 0;

		@Override
		public boolean hasNext() {
			return indice < intervalosList.size();
		}

		@Override
		public IntervaloTemporal next() {
			return intervalosList.get(indice++);
		}

	}

	static {

		intervalosList.add(new IntervaloTemporal("07:00", "07:50"));
		intervalosList.add(new IntervaloTemporal("07:55", "08:45"));
		intervalosList.add(new IntervaloTemporal("08:50", "09:40"));
		intervalosList.add(new IntervaloTemporal("09:55", "10:45"));
		intervalosList.add(new IntervaloTemporal("10:50", "11:40"));
		intervalosList.add(new IntervaloTemporal("11:45", "12:35"));
		intervalosList.add(new IntervaloTemporal("12:40", "13:30"));
		intervalosList.add(new IntervaloTemporal("13:35", "14:25"));
		intervalosList.add(new IntervaloTemporal("14:30", "15:20"));
		intervalosList.add(new IntervaloTemporal("15:35", "16:25"));
		intervalosList.add(new IntervaloTemporal("16:30", "17:20"));
		intervalosList.add(new IntervaloTemporal("17:25", "18:15"));
		intervalosList.add(new IntervaloTemporal("18:20", "19:10"));
		intervalosList.add(new IntervaloTemporal("19:10", "20:00"));
		intervalosList.add(new IntervaloTemporal("20:00", "20:50"));
		intervalosList.add(new IntervaloTemporal("21:00", "21:50"));
		intervalosList.add(new IntervaloTemporal("21:50", "22:40"));
	}

	public static List<IntervaloTemporal> getTemposAula() {
		return intervalosList;
	}

}
