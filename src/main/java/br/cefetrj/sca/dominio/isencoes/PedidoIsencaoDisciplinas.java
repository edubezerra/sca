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
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import br.cefetrj.sca.dominio.Aluno;
import br.cefetrj.sca.dominio.Disciplina;
import br.cefetrj.sca.dominio.Professor;
import br.cefetrj.sca.dominio.matriculaforaprazo.Comprovante;

@Entity
public class PedidoIsencaoDisciplinas {
	/**
	 * 
	 * Diferentes estados em que um objeto <code>PedidoIsencaoDisciplinas</code>
	 * pode se encontrar.
	 * 
	 * "EM PREPARAÇÃO" --> pedido ainda está em preparação pelo aluno.
	 * 
	 * "SUBMETIDO" --> pedido foi submetido (pelo aluno) para ser analisado.
	 * enquanto houver algum item desse pedido no estado SUMETIDO, o próprio
	 * pedido permanecer no estado SUBMETIDO.
	 * 
	 * "ANALISADO" --> TODOS os itens do pedido foram analisados (i.e.,
	 * deferidos ou indeferidos).
	 */
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

	private Situacao situacao;

	private Date dataRegistro;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "PEDIDO_ISENCAO_ID", referencedColumnName = "ID")
	List<ItemPedidoIsencaoDisciplina> itens = new ArrayList<>();

	/**
	 * Cada pedido de isenção de disciplinas deve conter o histórico escolar do
	 * aluno na sua instituição de origem.
	 */
	@OneToMany(cascade = { CascadeType.ALL })
	List<Comprovante> historicosEscolares = new ArrayList<>();

	@SuppressWarnings("unused")
	private PedidoIsencaoDisciplinas() {
	}

	public PedidoIsencaoDisciplinas(Aluno aluno) {
		if (aluno == null) {
			throw new IllegalArgumentException(
					"Pedido de isenção não pode ser criado sem um aluno.");
		}
		this.aluno = aluno;
		this.situacao = Situacao.EM_PREPARACAO;
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

	public List<ItemPedidoIsencaoDisciplina> getItens() {
		return itens;
	}

	/**
	 * 
	 * @return situação em que o pedido de isenção se encontra ("EM PREPARAÇÃO",
	 *         "SUBMETIDO" ou "ANALISADO")
	 * 
	 * @see <code>Situacao</code>
	 */
	public String getDescritorSituacao() {
		if (this.situacao == Situacao.EM_PREPARACAO) {
			return this.situacao.getValue();
		}
		for (int i = 0; i < this.getItens().size(); i++) {
			if (this.getItens().get(i).getSituacao().equals("SUBMETIDO")) {
				return Situacao.SUBMETIDO.getValue();
			}
		}
		return Situacao.ANALISADO.getValue();
	}

	public Situacao getSituacao() {
		return situacao;
	}

	public void submeterParaAnalise() {
		if (this.situacao != Situacao.EM_PREPARACAO)
			throw new IllegalStateException(
					"Apenas pedidos em preparação podem ser submetidos para análise.");
		else if (this.historicosEscolares.size() == 0) {
			throw new IllegalStateException(
					"Ao menos um histórico escolar deve ser anexado ao pedido.");
		} else if (this.itens == null || this.itens.isEmpty()) {
			throw new IllegalStateException(
					"O pedido deve conter pelo menos um item de isenção.");
		} else {
			this.situacao = Situacao.SUBMETIDO;
			try {
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				this.dataRegistro = sdf.parse(sdf.format(new Date()));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public Aluno getAluno() {
		return aluno;
	}

	public void comMaisUmItem(Disciplina disciplina,
			String nomeDisciplinaExterna, String notaFinalDisciplinaExterna,
			String cargaHoraria, String observacao, Comprovante doc) {
		boolean isencaoJaFoiSolicitadaParaDisciplina = false;
		for (ItemPedidoIsencaoDisciplina umItem : this.itens) {
			if (umItem.getDisciplina().equals(disciplina)) {
				isencaoJaFoiSolicitadaParaDisciplina = true;
				break;
			}
		}
		if (!isencaoJaFoiSolicitadaParaDisciplina) {
			ItemPedidoIsencaoDisciplina item = new ItemPedidoIsencaoDisciplina(
					disciplina, nomeDisciplinaExterna,
					notaFinalDisciplinaExterna, cargaHoraria, observacao, doc);
			this.itens.add(item);
		} else {
			throw new IllegalArgumentException(
					"Isenção já solicitada para disciplina: "
							+ disciplina.getNome());
		}
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

	public void registrarRespostaParaItem(String idItemPedidoIsencao,
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
				break;
			}
		}
	}

	public Comprovante getComprovanteConteudoProgramatico(Long idItem) {
		for (ItemPedidoIsencaoDisciplina item : this.itens) {
			if (item.getId().equals(idItem)) {
				return item.getComprovante();
			}
		}
		return null;
	}

	public List<Comprovante> getHistoricosEscolares() {
		return this.historicosEscolares;
	}

	public void anexarHistoricoEscolar(Comprovante doc) {
		if (getComprovanteHistoricoEscolar(doc.getNome()) != null) {
			throw new IllegalArgumentException(
					"Já existe histórico escolar anexado com mesmo nome: "
							+ doc.getNome());
		} else {
			this.historicosEscolares.add(doc);
		}
	}

	public Comprovante getComprovanteHistoricoEscolar(String nomeArquivo) {
		for (Comprovante comprovante : historicosEscolares) {
			if (comprovante.getNome().equals(nomeArquivo)) {
				return comprovante;
			}
		}
		return null;
	}

	public void removerComprovanteHistoricoEscolar(String nomeArquivo) {
		Comprovante comprovante = this
				.getComprovanteHistoricoEscolar(nomeArquivo);
		if (comprovante != null) {
			this.historicosEscolares.remove(comprovante);
		}
	}

}
