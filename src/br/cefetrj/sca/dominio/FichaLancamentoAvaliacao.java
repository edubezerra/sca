package br.cefetrj.sca.dominio;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class FichaLancamentoAvaliacao {
	Turma t;
	List<ItemFichaLancamento> itens = new ArrayList<ItemFichaLancamento>();

	public FichaLancamentoAvaliacao(Turma turma) {
		this.t = turma;
		Set<Inscricao> inscricoes = turma.getInscricoes();
		for (Inscricao inscricao : inscricoes) {
			itens.add(new ItemFichaLancamento(inscricao.getAluno().getNome(),
					inscricao.getAluno().getMatricula()));
		}
	}
}
