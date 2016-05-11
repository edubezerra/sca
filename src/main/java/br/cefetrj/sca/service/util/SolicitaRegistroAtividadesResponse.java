package br.cefetrj.sca.service.util;

import java.text.DateFormat;
import java.util.ArrayList;

import br.cefetrj.sca.dominio.Aluno;
import br.cefetrj.sca.dominio.atividadecomplementar.RegistroAtividadeComplementar;

@SuppressWarnings("serial")
public class SolicitaRegistroAtividadesResponse extends
		ArrayList<SolicitaRegistroAtividadesResponse.Item> {

	public class Item {
		
		private Long idAluno;
		private String matriculaAluno;
		private String nomeAluno;
		private Long idAtividade;
		private String descrAtividade;
		private String categoria;
		private Long idRegistro;
		private String descricao;
		private Long cargaHoraria;
		private boolean temDocumento;
		private String estado;
		private String dataSolicitacao;
		private boolean podeSerCancelado;
		private String dataAnalise;
		private Long idAvaliador;
		private String matriculaAvaliador;
		private String nomeAvaliador;
		private String justificativa;
				
		public Item(Long idAluno, String matriculaAluno, String nomeAluno,
				Long idAtividade, String descrAtividade, String categoria, Long idRegistro, String descricao, 
				Long cargaHoraria, boolean temDocumento, String estado, String dataSolicitacao,
				boolean podeSerCancelado, String dataAnalise, Long idAvaliador, String matriculaAvaliador, 
				String nomeAvaliador, String justificativa) {
			super();
			this.idAluno = idAluno;
			this.matriculaAluno = matriculaAluno;
			this.nomeAluno = nomeAluno;
			this.idAtividade = idAtividade;
			this.descrAtividade = descrAtividade;
			this.categoria = categoria;
			this.idRegistro = idRegistro;
			this.descricao = descricao;
			this.cargaHoraria = cargaHoraria;
			this.temDocumento = temDocumento;
			this.estado = estado;
			this.dataSolicitacao = dataSolicitacao;
			this.podeSerCancelado = podeSerCancelado;
			this.dataAnalise = dataAnalise;
			this.idAvaliador = idAvaliador;
			this.matriculaAvaliador = matriculaAvaliador;
			this.nomeAvaliador = nomeAvaliador;
			this.justificativa = justificativa;
		}

		public Long getIdAtividade() {
			return idAtividade;
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

		public boolean getTemDocumento() {
			return temDocumento;
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
		
		public String getDescrAtividade() {
			return descrAtividade;
		}

		public String getCategoria() {
			return categoria;
		}

		public String getDataAnalise() {
			return dataAnalise;
		}

		public Long getIdAvaliador() {
			return idAvaliador;
		}

		public String getMatriculaAvaliador() {
			return matriculaAvaliador;
		}

		public String getNomeAvaliador() {
			return nomeAvaliador;
		}

		public String getJustificativa() {
			return justificativa;
		}

		public Long getIdAluno() {
			return idAluno;
		}

		public String getMatriculaAluno() {
			return matriculaAluno;
		}

		public String getNomeAluno() {
			return nomeAluno;
		}
	}

	public void add(RegistroAtividadeComplementar reg, Aluno aluno) {
		DateFormat f = DateFormat.getDateInstance(DateFormat.LONG); //Data Completa
		
		String descrAtividade = reg.getAtividade().getTipo().getDescricao();
		descrAtividade = descrAtividade.charAt(0) + (descrAtividade.substring(1).toLowerCase());
		
		Item novo;
		if(reg.podeSerCancelado()){
			novo = this.new Item(aluno.getId(), aluno.getMatricula(), aluno.getNome(),
					reg.getAtividade().getId(), descrAtividade, 
					reg.getAtividade().getTipo().getCategoria().toString(),	reg.getId(), 
					reg.getDescricao(), reg.getCargaHoraria().toHours(), reg.getDocumento()!=null, 
					reg.getEstado().toString(), f.format(reg.getDataSolicitacao()), reg.podeSerCancelado(),
					"", null, "", "", "");
		}
		else{
			novo = this.new Item(aluno.getId(), aluno.getMatricula(), aluno.getNome(),
					reg.getAtividade().getId(), descrAtividade,
					reg.getAtividade().getTipo().getCategoria().toString(),	reg.getId(),
					reg.getDescricao(), reg.getCargaHoraria().toHours(), reg.getDocumento()!=null, 
					reg.getEstado().toString(), f.format(reg.getDataSolicitacao()), reg.podeSerCancelado(),
					f.format(reg.getDataAnalise()), reg.getAvaliador().getId(), reg.getAvaliador().getMatricula(),
					reg.getAvaliador().getNome(), reg.getJustificativa());
		}
		
		super.add(novo);
	}
}
