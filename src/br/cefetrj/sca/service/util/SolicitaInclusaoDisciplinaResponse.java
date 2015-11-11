package br.cefetrj.sca.service.util;

import java.util.ArrayList;

@SuppressWarnings("serial")
public class SolicitaInclusaoDisciplinaResponse extends ArrayList<SolicitaInclusaoDisciplinaResponse.Item>{
	public class Item {
		private String nomeDepartamento;
		private String codigoDepartamento;
		

		public Item(String nomeDepartamento, String codigoDepartamento) {
			this.codigoDepartamento = codigoDepartamento;
			this.nomeDepartamento = nomeDepartamento;
		}

		public String getNomeDepartamento() {
			return nomeDepartamento;
		}
		
		public String getCodigoDepartamento() {
			return codigoDepartamento;
		}
	}
}
