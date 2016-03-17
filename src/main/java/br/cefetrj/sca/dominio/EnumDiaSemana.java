package br.cefetrj.sca.dominio;

import java.util.ArrayList;
import java.util.List;

public enum EnumDiaSemana {
	DOMINGO, SEGUNDA, TERCA, QUARTA, QUINTA, SEXTA, SABADO;

	public static EnumDiaSemana findByText(String abbr) {
		for (EnumDiaSemana v : values()) {
			if (v.toString().equals(abbr)) {
				return v;
			}
		}
		return null;
	}

	public static List<String> dias() {
		List<String> dias = new ArrayList<>();
		for (EnumDiaSemana v : values()) {
			dias.add(v.toString());
		}
		return dias;
	}
}
