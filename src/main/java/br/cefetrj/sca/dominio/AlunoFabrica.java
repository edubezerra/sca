package br.cefetrj.sca.dominio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.cefetrj.sca.dominio.repositories.CursoRepositorio;

@Component
public class AlunoFabrica {
	@Autowired
	private CursoRepositorio crep;

	public Aluno criar(String nomeAluno, String matriculaAluno,
			String cpfAluno, String siglaCurso, String numeroVersaoCurso) {
		VersaoCurso versaoCurso = crep.getVersaoCurso(siglaCurso,
				numeroVersaoCurso);
		if (versaoCurso == null) {
			throw new IllegalArgumentException(
					"Versão do curso não encontrada. Sigla: " + siglaCurso
							+ "; Versão: " + numeroVersaoCurso);
		}
		Aluno aluno = new Aluno(nomeAluno, cpfAluno, matriculaAluno,
				versaoCurso);
		return aluno;
	}

}
