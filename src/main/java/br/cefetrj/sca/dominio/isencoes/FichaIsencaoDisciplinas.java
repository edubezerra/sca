package br.cefetrj.sca.dominio.isencoes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import br.cefetrj.sca.dominio.Aluno;
import br.cefetrj.sca.dominio.matriculaforaprazo.Comprovante;

public class FichaIsencaoDisciplinas {

	List<ItemFichaIsencaoDisciplina> itens = new ArrayList<>();

	Aluno aluno;

	boolean temHistoricoEscolarAnexado = false;

	PedidoIsencaoDisciplinas pedido;

	public FichaIsencaoDisciplinas(Aluno aluno, PedidoIsencaoDisciplinas pedido) {

		if (aluno == null) {
			throw new IllegalArgumentException("Ficha de isenções de disciplinas não pode ser criada sem aluno!");
		}

		this.aluno = aluno;

		if (pedido != null) {
			this.pedido = pedido;

			if (this.pedido.getAluno() != aluno) {
				throw new IllegalArgumentException(
						"Ficha de isenções de disciplinas não pode ser criada sem a vinculação a um aluno!");
			}

			List<ItemPedidoIsencaoDisciplina> itensPedido = pedido.getItens();
			for (ItemPedidoIsencaoDisciplina itemPedido : itensPedido) {
				itens.add(new ItemFichaIsencaoDisciplina(itemPedido));
			}

			if (pedido.getHistoricosEscolares().size() > 0) {
				temHistoricoEscolarAnexado = true;
			}
		}
	}

	public Aluno getAluno() {
		return this.aluno;
	}

	public List<ItemFichaIsencaoDisciplina> getItens() {
		return itens;
	}

	public boolean isHistoricoEscolarAnexado() {
		return true;
	}

	public String getNomeAluno() {
		return this.aluno.getNome();
	}

	public String getMatriculaAluno() {
		return this.aluno.getNome();
	}

	public String getDescritorVersaoCurso() {
		return this.aluno.getVersaoCurso().getNumero();
	}

	public String getSiglaCurso() {
		return this.aluno.getVersaoCurso().getCurso().getSigla();
	}

	public String getNomeCurso() {
		return this.aluno.getVersaoCurso().getCurso().getNome();
	}

	public boolean isSubmissaoJaRealizada() {
		if (this.pedido != null) {
			if (this.pedido.getSituacao() != PedidoIsencaoDisciplinas.Situacao.EM_PREPARACAO) {
				return true;
			}
		}
		return false;
	}

	public List<Comprovante> getHistoricosEscolares() {
		if (pedido != null) {
			return this.pedido.getHistoricosEscolares();
		}
		return Collections.emptyList();
	}
}
