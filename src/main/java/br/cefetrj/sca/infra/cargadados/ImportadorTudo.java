package br.cefetrj.sca.infra.cargadados;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class ImportadorTudo {

	public static AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
			StandalonePersistenceConfig.class);

	ImportadorQuestionarioAvaliacaoDocente importadorQuestionarioAvaliacaoDocente = new ImportadorQuestionarioAvaliacaoDocente();

	@Autowired
	ImportadorPeriodoMinimoVersaoCurso importadorPerMinVersaoCurso;
	
//	@Autowired
//	ImportadorCursos importadorCursos;

	@Autowired
	ImportadorGradesCurriculares importadorGradesCurriculares;

	@Autowired
	ImportadorPreReqs importadorPreReqs;

	@Autowired
	ImportadorAtividadesComplementares importadorAtividadesComp;

	@Autowired
	ImportadorAlunos importadorAlunos;

	@Autowired
	ImportadorProfessores importadorProfessores;

	@Autowired
	ImportadorDepartamentos importadorDepartamentos;

	@Autowired
	ImportadorHistoricosEscolares importadorHistoricoEscolar;

	@Autowired
	ImportadorTurmasComInscricoes importadorTurmasComInscricoes;

	@Autowired
	ImportadorAlocacoesProfessoresEmTurmas importadorAlocacoesProfessoresEmTurmas;

	@Autowired
	ImportadorHabilitacoesParaProfessor importadorHabilitacoesParaProfessor;

	@Autowired
	ImportadorAlocacoesProfessoresEmDepartamentos importadorAlocacoesProfessoresEmDepartamentos;

	@Autowired
	ImportadorAlocacoesDisciplinasEmDepartamentos importadorAlocacoesDisciplinasEmDepartamentos;

	@Autowired
	ImportadorEquivalenciaDisciplinas importadorEquivalenciaDisciplinas;
	
	public static void main(String[] args) {
		ImportadorTudo importador = context.getBean(ImportadorTudo.class);
		importador.run();
	}

	@Transactional
	public void run() {
		try {
//			importadorQuestionarioAvaliacaoDocente.run();
//
//			importadorCursos.run();
//			importadorGradesCurriculares.run();
//			importadorPreReqs.run();
//			importadorAtividadesComp.run();
//			importadorAlunos.run();
//			importadorProfessores.run();
//			importadorDepartamentos.run();
//
//			importadorHistoricoEscolar.run();
//
//			importadorTurmasComInscricoes.run();
//			importadorAlocacoesProfessoresEmTurmas.run();
//			importadorHabilitacoesParaProfessor.run();
			importadorPerMinVersaoCurso.run();
//
//			importadorAlocacoesProfessoresEmDepartamentos.run();
//
//			importadorAlocacoesDisciplinasEmDepartamentos.run();
//			
//			importadorEquivalenciaDisciplinas.run();

		} catch (IllegalArgumentException | IllegalStateException ex) {
			System.err.println(ex.getMessage());
			System.exit(1);
		}
	}
}
