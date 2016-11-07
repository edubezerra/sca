package br.cefetrj.sca.service.util;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.cefetrj.sca.dominio.Aluno;
import br.cefetrj.sca.dominio.Disciplina;
import br.cefetrj.sca.dominio.isencoes.ItemPedidoIsencaoDisciplina;
import br.cefetrj.sca.dominio.isencoes.PedidoIsencaoDisciplinas;
import br.cefetrj.sca.dominio.repositories.DisciplinaRepositorio;
import br.cefetrj.sca.dominio.repositories.PedidoIsencaoDisciplinasRepositorio;

@Component
public class FichaIsencaoDisciplinas {
	@Autowired
	private DisciplinaRepositorio disciplinaRepo;

	@Autowired
	private PedidoIsencaoDisciplinasRepositorio pidrRepo;

	List<ItemFichaIsencaoDisciplina> itens = new ArrayList<>();
	Aluno aluno;

	private FichaIsencaoDisciplinas() {
	}

	public FichaIsencaoDisciplinas(Aluno aluno) {

		this.aluno = aluno;

		PedidoIsencaoDisciplinas pedido = pidrRepo.findByMatriculaAluno(aluno.getMatricula());

		List<ItemPedidoIsencaoDisciplina> itensPedido = pedido.getItens();
		for (ItemPedidoIsencaoDisciplina itemPedido : itensPedido) {
			itens.add(new ItemFichaIsencaoDisciplina(itemPedido));
		}

		List<Disciplina> disciplinas = disciplinaRepo.findAllEmVersaoCurso(aluno.getVersaoCurso());
		for (Disciplina disciplina : disciplinas) {
			if(!itens.contains(disciplina)){
				itens.add(new ItemFichaIsencaoDisciplina(disciplina));
			}
		}
	}

	public Aluno getAluno() {
		return this.aluno;
	}

	public List<ItemFichaIsencaoDisciplina> getItens() {
		return itens;
	}

}
