package br.cefetrj.sca.dominio.repositories;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.cefetrj.sca.dominio.Disciplina;

@Component
public class VersaoCursoRepositorio {

	@Autowired
	private DisciplinaRepositorio dao;

	public void adicionar(Disciplina d) {
		dao.save(d);
	}

	/**
	 * 
	 * @param nome
	 *            nome da disciplina
	 * @return a disciplina cujo nome foi passado como parâmetro, se existir;
	 *         null em caso contrário.
	 */
	public Disciplina getDisciplinaPorNome(String nome) {
		return dao.findDisciplinaByNome(nome);
	}

	public List<Disciplina> getDisciplinas() {
		return dao.findAll();
	}

	public boolean estaContidaEm(Set<Disciplina> preReqs,
			Set<Disciplina> cursadas) {
		return cursadas.containsAll(preReqs);
	}

	public Disciplina getDisciplinaPorCodigo(String codigoDisciplina) {
		return dao.findDisciplinaByCodigo(codigoDisciplina);
	}
}
