package br.cefetrj.sca.service.util;

public class SituacaoAtividade {
		private String categoriaAtiv;
		private String descrAtiv;
		private Long cargaHorariaRestante;
		
		public SituacaoAtividade(String categoriaAtiv, String descrAtiv, Long cargaHorariaRestante) {
			super();
			this.categoriaAtiv = categoriaAtiv;
			this.descrAtiv = descrAtiv;
			this.cargaHorariaRestante = cargaHorariaRestante;
		}

		public String getCategoriaAtiv() {
			return categoriaAtiv;
		}

		public void setCategoriaAtiv(String categoriaAtiv) {
			this.categoriaAtiv = categoriaAtiv;
		}

		public String getDescrAtiv() {
			return descrAtiv;
		}

		public void setDescrAtiv(String descrAtiv) {
			this.descrAtiv = descrAtiv;
		}

		public Long getCargaHorariaRestante() {
			return cargaHorariaRestante;
		}

		public void setCargaHorariaRestante(Long cargaHorariaRestante) {
			this.cargaHorariaRestante = cargaHorariaRestante;
		}		
}
