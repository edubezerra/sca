package br.cefetrj.sca.infra;

import java.util.List;

import br.cefetrj.sca.dominio.Aluno;

public interface AlunoDao {

	public abstract boolean incluir(Aluno p);

	public abstract boolean alterar(Aluno p);

	public abstract boolean excluir(Aluno p);

	public abstract Aluno getAlunoPorMatricula(String matricula);

	public abstract List<Aluno> obterTodos();

}