package br.cefetrj.sca.dominio.atividadecomplementar;

public enum EnumEstadoAtividadeComplementar {
	SUBMETIDO, EM_ANÁLISE, DEFERIDO, INDEFERIDO;

	public static EnumEstadoAtividadeComplementar findByText(String abbr) {
		for (EnumEstadoAtividadeComplementar v : values()) {
			if (v.toString().equals(abbr)) {
				return v;
			}
		}
		return null;
	}
}