package br.cefetrj.sca.dominio.isencoes;

import java.util.ArrayList;
import java.util.List;

import br.cefetrj.sca.dominio.Aluno;

public class FichaIsencaoDisciplinas {

	List<ItemFichaIsencaoDisciplina> itens = new ArrayList<>();

	Aluno aluno;

	boolean temHistoricoEscolarAnexado = false;

	public FichaIsencaoDisciplinas(Aluno aluno, PedidoIsencaoDisciplinas pedido) {

		if (aluno == null) {
			throw new IllegalArgumentException(
					"Ficha de isenções de disciplinas não pode ser criada sem aluno!");
		}

		this.aluno = aluno;

		if (pedido != null) {
			List<ItemPedidoIsencaoDisciplina> itensPedido = pedido.getItens();
			for (ItemPedidoIsencaoDisciplina itemPedido : itensPedido) {
				itens.add(new ItemFichaIsencaoDisciplina(itemPedido));
			}

			if (pedido.getHistoricoEscolar() != null) {
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

	public boolean temHistoricoEscolarAnexado() {
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
}
