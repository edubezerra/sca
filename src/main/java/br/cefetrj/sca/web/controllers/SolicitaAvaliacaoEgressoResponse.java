package br.cefetrj.sca.web.controllers;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class SolicitaAvaliacaoEgressoResponse extends ArrayList<SolicitaAvaliacaoEgressoResponse.Item> {
	public class Item {
		private String quesito;
		private List<AlternativaDto> alternativas;

		public Item(String quesito, List<AlternativaDto> alternativas) {
			this.quesito = quesito;
			this.alternativas = alternativas;
		}

		public String getQuesito() {
			return quesito;
		}

		public List<AlternativaDto> getAlternativas() {
			return alternativas;
		}
	}

	public class AlternativaDto {
		private Long id;
		private String alternativa;

		public AlternativaDto(Long id, String alternativa) {
			this.id = id;
			this.alternativa = alternativa;
		}

		public Long getId() {
			return id;
		}

		public String getAlternativa() {
			return alternativa;
		}
	}

}