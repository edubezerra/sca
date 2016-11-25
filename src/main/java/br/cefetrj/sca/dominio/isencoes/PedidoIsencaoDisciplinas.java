package br.cefetrj.sca.dominio.isencoes;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import br.cefetrj.sca.dominio.Aluno;
import br.cefetrj.sca.dominio.Disciplina;
import br.cefetrj.sca.dominio.Professor;
import br.cefetrj.sca.dominio.matriculaforaprazo.Comprovante;

@Entity
public class PedidoIsencaoDisciplinas {
	public enum Situacao {
		EM_PREPARACAO("EM PREPARAÇÃO"), SUBMETIDO("SUBMETIDO"), ANALISADO(
				"ANALISADO");

		private String value;

		private Situacao(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}
	}

	@Id
	@GeneratedValue
	private Long id;

	/**
	 * O aluno solicitante.
	 */
	@OneToOne
	Aluno aluno;

	private String situacao;

	private Date dataRegistro;

	@OneToMany(cascade = CascadeType.ALL)
	List<ItemPedidoIsencaoDisciplina> itens = new ArrayList<>();

	@SuppressWarnings("unused")
	private PedidoIsencaoDisciplinas() {
	}

	public PedidoIsencaoDisciplinas(Aluno aluno) {
		this.aluno = aluno;
		this.situacao = "EM PREPARAÇÃO";
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getDataRegistro() {
		return dataRegistro;
	}

	// public void setDataRegistro(Date dataRegistro) {
	// this.dataRegistro = dataRegistro;
	// }

	public List<ItemPedidoIsencaoDisciplina> getItens() {
		return itens;
	}

	/**
	 * 
	 * @return situação do processo de isenção ("EM PREPARAÇÃO", "SUBMETIDO" ou
	 *         "ANALISADO")
	 */
	public String getSituacao() {
		if (this.situacao.equals("EM PREPARAÇÃO")) {
			return this.situacao;
		}
		for (int i = 0; i < this.getItens().size(); i++) {
			if (this.getItens().get(i).getSituacao().equals("INDEFINIDO")) {
				return "SUBMETIDO";
			}
		}
		return "ANALISADO";
	}

	// public void setSituacao(String situacao) {
	// this.situacao = situacao;
	// }

	public void submeterParaAnalise() {
		if (!this.situacao.equals("EM PREPARAÇÃO"))
			throw new IllegalStateException(
					"Apenas pedidos em preparação podem ser submetidos para análise.");
		this.situacao = "SUBMETIDO";
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			this.dataRegistro = sdf.parse(sdf.format(new Date()));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Aluno getAluno() {
		return aluno;
	}

	public void comMaisUmItem(Disciplina disciplina) {
		ItemPedidoIsencaoDisciplina item = new ItemPedidoIsencaoDisciplina(
				disciplina);
		this.itens.add(item);
	}

	public void deferirItem(Long idItem, Professor professor) {
		for (int i = 0; i < this.getItens().size(); i++) {
			if (this.getItens().get(i).getId().equals(idItem)) {
				this.getItens().get(i).deferir(professor);
				return;
			}
		}
	}

	public void indeferirItem(Long idItem, Professor professor,
			String observacao) {
		for (int i = 0; i < this.getItens().size(); i++) {
			if (this.getItens().get(i).getId().equals(idItem)) {
				this.getItens().get(i).indeferir(professor, observacao);
				return;
			}
		}
	}

	public void analisarItem(String idItemPedidoIsencao,
			Professor professorResponsavel, String novaSituacao,
			String observacao) {
		if (novaSituacao == null || novaSituacao.isEmpty()) {
			throw new IllegalArgumentException(
					"Nova situação do item de isenção deve ser informada.");
		}
		if (idItemPedidoIsencao == null || idItemPedidoIsencao.isEmpty()) {
			throw new IllegalArgumentException(
					"Idenfiticação do item de isenção deve ser informada.");
		}
		if (professorResponsavel == null) {
			throw new IllegalArgumentException(
					"Professor responsável pela análise deve ser informado.");
		}
		Long idItem = Long.parseLong(idItemPedidoIsencao);
		if (novaSituacao.equals("DEFERIDO")) {
			this.deferirItem(idItem, professorResponsavel);
		} else if (novaSituacao.equals("INDEFERIDO")) {
			this.indeferirItem(idItem, professorResponsavel, observacao);
		} else {
			throw new IllegalArgumentException(
					"Valor inválido para nova situação do item de isenção.");
		}
	}

	public void removerItem(Long idItem) {
		for (ItemPedidoIsencaoDisciplina item : this.itens) {
			if (item.getId().equals(idItem)) {
				this.itens.remove(item);
			}
			break;
		}
	}

	public Comprovante getComprovanteDoItem(Long idItem) {
		for (ItemPedidoIsencaoDisciplina item : this.itens) {
			if (item.getId().equals(idItem)) {
				return item.getComprovante();
			}
		}
		return null;
	}
}
