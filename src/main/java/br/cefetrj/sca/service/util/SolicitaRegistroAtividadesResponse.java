package br.cefetrj.sca.service.util;

import java.text.DateFormat;
import java.util.ArrayList;

import br.cefetrj.sca.dominio.atividadecomplementar.RegistroAtividadeComplementar;
import br.cefetrj.sca.dominio.inclusaodisciplina.Comprovante;

@SuppressWarnings("serial")
public class SolicitaRegistroAtividadesResponse extends
		ArrayList<SolicitaRegistroAtividadesResponse.Item> {

	public class Item {
		
		private Long idAtividade;
		private String codigoAtividade;
		private Long idRegistro;
		private String descricao;
		private Long cargaHoraria;
		private Comprovante documento;
		private String estado;
		private String dataSolicitacao;
		private boolean podeSerCancelado;
				
		public Item(Long idAtividade, String codigoAtividade, Long idRegistro, String descricao, Long cargaHoraria,
				Comprovante documento, String estado, String dataSolicitacao, boolean podeSerCancelado) {
			super();
			this.idAtividade = idAtividade;
			this.codigoAtividade = codigoAtividade;
			this.idRegistro = idRegistro;
			this.descricao = descricao;
			this.cargaHoraria = cargaHoraria;
			this.documento = documento;
			this.estado = estado;
			this.dataSolicitacao = dataSolicitacao;
			this.podeSerCancelado = podeSerCancelado;
		}

		public Long getIdAtividade() {
			return idAtividade;
		}

		public String getCodigoAtividade() {
			return codigoAtividade;
		}

		public Long getIdRegistro() {
			return idRegistro;
		}

		public String getDescricao() {
			return descricao;
		}

		public Long getCargaHoraria() {
			return cargaHoraria;
		}

		public Comprovante getDocumento() {
			return documento;
		}

		public String getEstado() {
			return estado;
		}
		
		public String getDataSolicitacao() {
			return dataSolicitacao;
		}

		public boolean getPodeSerCancelado() {
			return podeSerCancelado;
		}
	}

	public void add(RegistroAtividadeComplementar reg) {
		DateFormat f = DateFormat.getDateInstance(DateFormat.LONG); //Data COmpleta

		Item novo = this.new Item(reg.getAtividade().getId(),reg.getAtividade().getTipo().getCodigo(), reg.getId(), 
				reg.getDescricao(), reg.getCargaHoraria().toHours(), reg.getDocumento(), reg.getEstado().toString(),  
				f.format(reg.getDataSolicitacao()), reg.podeSerCancelado());
		super.add(novo);
	}
}
