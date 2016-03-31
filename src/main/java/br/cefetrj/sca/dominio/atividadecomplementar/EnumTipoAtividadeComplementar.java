package br.cefetrj.sca.dominio.atividadecomplementar;

public enum EnumTipoAtividadeComplementar {
	PESQUISA, EXTENSÃO, ENSINO, CONSCIENTIZAÇÃO;

	public static EnumTipoAtividadeComplementar findByText(String abbr) {
		for (EnumTipoAtividadeComplementar v : values()) {
			if (v.toString().equals(abbr)) {
				return v;
			}
		}
		return null;
	}
}
