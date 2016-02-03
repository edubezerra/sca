package br.cefetrj.sca.infra.cargadados;

public class ImportadorTudo {
	public static void main(String[] args) {

		try {
			ImportadorQuestionarioAvaliacaoTurmas.run();
			ImportadorCursos.main(args);
			ImportadorDisciplinas.main(args);
			ImportadorPreReqs.main(args);
			ImportadorAlunos.main(args);
			ImportadorDocentes.main(args);
			ImportadorDepartamentos.main(args);
			ImportadorHistoricosEscolares.main(args);
			ImportadorInscricoes.main(args);
			ImportadorAlocacoesProfessoresEmTurmas.main(args);
			ImportadorLocais.main(args);
			ImportadorAulas.main(args);
		} catch (IllegalArgumentException | IllegalStateException ex) {
			System.err.println(ex.getMessage());
			System.exit(1);
		}
	}
}
