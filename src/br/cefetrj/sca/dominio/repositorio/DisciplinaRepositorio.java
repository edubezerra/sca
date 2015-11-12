package br.cefetrj.sca.dominio.repositorio;

import java.util.List;
import java.util.Set;

import br.cefetrj.sca.dominio.Disciplina;
import br.cefetrj.sca.dominio.VersaoCurso;

public interface DisciplinaRepositorio {
	void adicionar(Disciplina d);

	void adicionarTodas(List<Disciplina> lista);

	List<Disciplina> getDisciplinas();

	void excluir(Disciplina disciplina);

	Disciplina getDisciplinaPorNome(String nomeDisciplina);

	Disciplina getDisciplinaPorCodigo(String codigoDisciplina);

	Disciplina getByCodigo(String codigoDisciplina, String siglaCurso, String versaoCurso);

	Disciplina getByNome(String nomeDisciplina, String siglaCurso, String versaoCurso);

	List<Disciplina> getDisciplinasPorVersaoCurso(VersaoCurso versaoCurso);

	boolean estaContidaEm(Set<Disciplina> preReqs, Set<Disciplina> cursadas);
}
