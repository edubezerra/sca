package br.cefetrj.sca.dominio.repositorio;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.cefetrj.sca.dominio.Disciplina;
import br.cefetrj.sca.infra.DisciplinaDao;

@Component
public class DisciplinaRepositorio {

	@Autowired
	private DisciplinaDao dao;

	private DisciplinaRepositorio() {
	}

	public void adicionar(Disciplina d) {
		dao.gravar(d);
	}

	/**
	 * 
	 * @param nome
	 *            nome da disciplina
	 * @return a disciplina cujo nome foi passado como parâmetro, se existir;
	 *         null em caso contrário.
	 */
	public Disciplina getDisciplina(String nome) {
		return dao.getByNome(nome);
	}

	public List<Disciplina> getDisciplinas() {
		return dao.getDisciplinas();
	}

	public boolean estaContidaEm(Set<Disciplina> preReqs,
			Set<Disciplina> cursadas) {
		return cursadas.containsAll(preReqs);
	}
}
