package br.cefetrj.sca.service.util;

import java.time.Duration;
import java.util.ArrayList;

import br.cefetrj.sca.dominio.atividadecomplementar.AtividadeComplementar;

@SuppressWarnings("serial")
public class SolicitaSituacaoAtividadesResponse extends
		ArrayList<SolicitaSituacaoAtividadesResponse.Item> {

	public class Item {
		
		private Long idAtividade;
		private String descricao;		
		private Long cargaHorariaMax;
		private Long cargaHorariaMin;		
		private String categoria;
		private Long cargaHorariaCumprida;
		private boolean cargaHorariaSuficente;
		private boolean cargaHorariaMaxima;
		
		public Item(Long idAtividade, String descricao, Long cargaHorariaMax,
				Long cargaHorariaMin, String categoria, Long cargaHorariaCumprida,
				boolean cargaHorariaSuficente, boolean cargaHorariaMaxima) {
			super();
			this.idAtividade = idAtividade;
			this.descricao = descricao;
			this.cargaHorariaMax = cargaHorariaMax;
			this.cargaHorariaMin = cargaHorariaMin;
			this.categoria = categoria;
			this.cargaHorariaCumprida = cargaHorariaCumprida;
			this.cargaHorariaSuficente = cargaHorariaSuficente;
			this.cargaHorariaMaxima = cargaHorariaMaxima;
		}

		public Long getIdAtividade() {
			return idAtividade;
		}

		public String getDescricao() {
			return descricao;
		}

		public Long getCargaHorariaMax() {
			return cargaHorariaMax;
		}

		public Long getCargaHorariaMin() {
			return cargaHorariaMin;
		}

		public String getCategoria() {
			return categoria;
		}

		public Long getCargaHorariaCumprida() {
			return cargaHorariaCumprida;
		}

		public boolean isCargaHorariaSuficente() {
			return cargaHorariaSuficente;
		}
		
		public boolean isCargaHorariaMaxima() {
			return cargaHorariaMaxima;
		}
	}

	public void add(AtividadeComplementar ativ, Duration cargaHorariaCumprida, boolean cargaHorariaSuficente, boolean cargaHorariaMaxima) {
		String descricao = ativ.getTipo().getDescricao();
		descricao = descricao.charAt(0) + (descricao.substring(1).toLowerCase());
		
		Item novo = this.new Item(ativ.getId(), descricao, ativ.getCargaHorariaMax().toHours(), 
				ativ.getCargaHorariaMin().toHours(), ativ.getTipo().getCategoria().toString(), 
				cargaHorariaCumprida.toHours(), cargaHorariaSuficente, cargaHorariaMaxima);
		super.add(novo);
	}
}
