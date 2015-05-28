package br.cefetrj.sca.dominio;

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
}
