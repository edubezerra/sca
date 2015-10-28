package br.cefetrj.sca.infra;

import java.util.List;

import br.cefetrj.sca.dominio.Disciplina;

public interface DisciplinaDao {
	void gravar(Disciplina d);

	void gravar(List<Disciplina> lista);

	List<Disciplina> getDisciplinas();

	void excluir(Disciplina disciplina);

	Disciplina getByNome(String nome);

	Disciplina getByCodigo(String codigoDisciplina);
}
