package br.cefetrj.sca.service.util;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class SolicitaAvaliacaoTurmaResponse extends
		ArrayList<SolicitaAvaliacaoTurmaResponse.Item> {

	public class Item {
		private String quesito;
		private List<String> alternativas;

		public Item(String quesito, List<String> alternativas) {
			this.quesito = quesito;
			this.alternativas = alternativas;
		}

		public String getQuesito() {
			return quesito;
		}

		public List<String> getAlternativas() {
			return alternativas;
		}
	}

	private String codigoTurma;
	private String nomeDisciplina;

	public SolicitaAvaliacaoTurmaResponse(String codigoTurma,
			String nomeDisciplina) {
		this.codigoTurma = codigoTurma;
		this.nomeDisciplina = nomeDisciplina;
	}

	public String getCodigoTurma() {
		return codigoTurma;
	}

	public String getNomeDisciplina() {
		return nomeDisciplina;
	}
	public void setNomeDisciplina(String nomeDisciplina) {
		this.nomeDisciplina = nomeDisciplina;
	}
}
