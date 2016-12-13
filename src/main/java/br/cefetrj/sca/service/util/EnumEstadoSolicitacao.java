package br.cefetrj.sca.service.util;

public enum EnumEstadoSolicitacao {
	SUBMETIDO, EM_ANÁLISE, DEFERIDO, INDEFERIDO;

	public static EnumEstadoSolicitacao findByText(String abbr) {
		for (EnumEstadoSolicitacao v : values()) {
			if (v.toString().equals(abbr)) {
				return v;
			}
		}
		return null;
	}
}
