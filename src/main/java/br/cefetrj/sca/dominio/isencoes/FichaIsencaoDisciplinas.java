package br.cefetrj.sca.dominio.isencoes;

import java.util.ArrayList;
import java.util.List;

import br.cefetrj.sca.dominio.Aluno;
import br.cefetrj.sca.dominio.Disciplina;

public class FichaIsencaoDisciplinas {

	List<ItemFichaIsencaoDisciplina> itens = new ArrayList<>();

	Aluno aluno;

	public FichaIsencaoDisciplinas(Aluno aluno, PedidoIsencaoDisciplinas pedido, List<Disciplina> disciplinas) {

		this.aluno = aluno;

		if (pedido != null) {
			List<ItemPedidoIsencaoDisciplina> itensPedido = pedido.getItens();
			for (ItemPedidoIsencaoDisciplina itemPedido : itensPedido) {
				itens.add(new ItemFichaIsencaoDisciplina(itemPedido));
			}
		}

//		for (Disciplina disciplina : disciplinas) {
//			if (!itens.contains(disciplina)) {
//				itens.add(new ItemFichaIsencaoDisciplina(disciplina));
//			}
//		}
	}

	public Aluno getAluno() {
		return this.aluno;
	}

	public List<ItemFichaIsencaoDisciplina> getItens() {
		return itens;
	}

}
