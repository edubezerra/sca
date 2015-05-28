package br.cefetrj.sca.infra;

import java.util.List;

import br.cefetrj.sca.dominio.Disciplina;

public interface DisciplinaDao {
	void gravar(Disciplina d);

	void gravar(List<Disciplina> lista);

	Disciplina getByNome(String nome);

	List<Disciplina> getDisciplinas();

	void excluir(Disciplina disciplina);
}
