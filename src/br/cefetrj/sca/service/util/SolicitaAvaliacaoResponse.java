package br.cefetrj.sca.service.util;

import java.util.ArrayList;

@SuppressWarnings("serial")
public class SolicitaAvaliacaoResponse extends
		ArrayList<SolicitaAvaliacaoResponse.Item> {

	public class Item {
		private String codigoTurma;
		private String nomeDisciplina;
		private boolean isAvaliada;

		public Item(String codigoTurma, String nomeDisciplina,
				boolean isAvaliada) {
			this.codigoTurma = codigoTurma;
			this.nomeDisciplina = nomeDisciplina;
			this.isAvaliada = isAvaliada;
		}

		public String getCodigoTurma() {
			return codigoTurma;
		}

		public String getNomeDisciplina() {
			return nomeDisciplina;
		}

		public boolean getIsAvaliada() {
			return isAvaliada;
		}
	}
}
