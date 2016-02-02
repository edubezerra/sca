package br.cefetrj.sca.infra.cargadados;

public class ImportadorTudo {
	public static void main(String[] args) {

		try {
			ImportadorQuestionarioAvaliacaoTurmas.run();
			ImportadorCursos.main(args);
			ImportadorDisciplinas.main(args);
			ImportadorPreReqs.main(args);
			ImportadorDiscentes.main(args);
			ImportadorDocentes.main(args);
			ImportadorDepartamentos.main(args);
			ImportadorHistoricosEscolares.main(args);
//			ImportadorTurmas.main(args);
			ImportadorInscricoes.main(args);
			ImportadorAlocacoesProfessoresEmTurmas.main(args);
			ImportadorLocais.main(args);
			ImportadorAulas.main(args);
		} catch (IllegalArgumentException | IllegalStateException ex) {
			System.err.println(ex.getMessage());
			System.exit(1);
		} finally {
		}

		System.out.println("Importação finalizada!");
	}
}
