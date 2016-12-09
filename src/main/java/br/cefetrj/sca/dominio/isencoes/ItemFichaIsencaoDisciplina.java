package br.cefetrj.sca.dominio.isencoes;

import java.text.DateFormat;

import br.cefetrj.sca.dominio.Disciplina;

public class ItemFichaIsencaoDisciplina {
	Disciplina disciplina;
	String estado;
	private boolean temDocumento;
	private Long idItem;
	private String dataSolicitacao;
	private String dataAnalise;
	private Long idAvaliador;
	private String matriculaAvaliador;
	private String nomeAvaliador;
	private String justificativa;

	public ItemFichaIsencaoDisciplina(ItemPedidoIsencaoDisciplina itemPedido) {
		this.disciplina = itemPedido.getDisciplina();
		this.estado = itemPedido.getSituacao();
		this.temDocumento = false;
		if (itemPedido.getComprovante() != null) {
			this.temDocumento = true;
		}
		idItem = itemPedido.getId();

		DateFormat f = DateFormat.getDateInstance(DateFormat.LONG);

		if (itemPedido.getDataSolicitacao() != null) {
			this.dataSolicitacao = f.format(itemPedido.getDataSolicitacao());
		}
		if (itemPedido.getDataAnalise() != null) {
			this.dataAnalise = f.format(itemPedido.getDataAnalise());
		}
		if (itemPedido.getProfessorResponsavel() != null) {
			this.idAvaliador = itemPedido.getProfessorResponsavel().getId();
			this.nomeAvaliador = itemPedido.getProfessorResponsavel().getNome();
			this.matriculaAvaliador = itemPedido.getProfessorResponsavel()
					.getMatricula();
		}

		this.justificativa = itemPedido.getMotivoIndeferimento();
	}

	public ItemFichaIsencaoDisciplina(Disciplina disciplina) {
		this.disciplina = disciplina;
	}

	public Disciplina getDisciplina() {
		return disciplina;
	}

	public String getEstado() {
		return estado;
	}

	public boolean isTemDocumento() {
		return temDocumento;
	}

	public Long getIdItem() {
		return idItem;
	}

	public boolean getPodeSerCancelado() {
		if(this.estado.equals("SUBMETIDO")) {
			return true;
		}
		return false;
	}

	public String getDataAnalise() {
		return dataAnalise;
	}

	public String getDataSolicitacao() {
		return dataSolicitacao;
	}

	public Long getIdAvaliador() {
		return idAvaliador;
	}

	public String getNomeAvaliador() {
		return nomeAvaliador;
	}

	public String getMatriculaAvaliador() {
		return matriculaAvaliador;
	}

	public String getJustificativa() {
		return justificativa;
	}
}
