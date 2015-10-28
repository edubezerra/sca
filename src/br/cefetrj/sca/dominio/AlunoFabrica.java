package br.cefetrj.sca.dominio;

import org.springframework.beans.factory.annotation.Autowired;

public class AlunoFabrica {
	@Autowired
	private CursoRepositorio crep;

	public AlunoFabrica() {
		System.out.println("AlunoFabrica.AlunoFabrica()");
	}

	public Aluno criar(String aluno_nome, String aluno_matricula, String aluno_cpf, String codigoSigla) {
		Aluno aluno = new Aluno(aluno_nome, aluno_cpf, aluno_matricula);
		Curso curso = crep.getPorSigla(codigoSigla);
		aluno.setCurso(curso);
		return aluno;
	}

}
