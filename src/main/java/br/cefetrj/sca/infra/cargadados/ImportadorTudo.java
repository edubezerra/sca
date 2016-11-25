package br.cefetrj.sca.infra.cargadados;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import br.cefetrj.sca.config.PersistenceConfig;

@Component
public class ImportadorTudo {

	public static AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
			PersistenceConfig.class);

	@Autowired
	ImportadorPesquisaAvaliacaoProfessor importadorQuestionarioAvaliacaoTurmas;

	@Autowired
	ImportadorPeriodoMinimoVersaoCurso importadorPerMinVersaoCurso;

	@Autowired
	ImportadorGradesCurriculares importadorGradesCurriculares;

	@Autowired
	ImportadorPreReqs importadorPreReqs;

	@Autowired
	ImportadorAtividadesComplementares importadorAtividadesComp;

	@Autowired
	ImportadorAlunos importadorAlunos;

	@Autowired
	ImportadorUsuariosAlunosUsandoMatriculaComoLogin importadorUsuariosAlunos;

	@Autowired
	ImportadorUsuariosProfessoresUsandoMatriculaComoLogin importadorUsuariosProfessores;

	@Autowired
	ImportadorUsuariosDemaisPerfis importadorUsuariosDemaisPerfis;

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
	ImportadorEquivalenciasDisciplinas importadorEquivalenciaDisciplinas;

	@Autowired
	ImportadorQuestionarioEgresso importadorQuestionarioEgresso;
	
	public static void main(String[] args) {
		ImportadorTudo importador = context.getBean(ImportadorTudo.class);
		importador.run();
	}

	@Transactional
	public void run() {
		try {
			importadorQuestionarioEgresso.run();
			
			importadorQuestionarioAvaliacaoTurmas.run();
			importadorGradesCurriculares.run();
			importadorTurmasComInscricoes.run();

			importadorPreReqs.run();

			importadorAlunos.run();

			importadorProfessores.run();

			importadorUsuariosAlunos.run();
			importadorUsuariosProfessores.run();
			importadorUsuariosDemaisPerfis.run();

			importadorDepartamentos.run();

			// Agora essa importação é feita pela aplicação WEB.
			//			 importadorAtividadesComp.run();

			 importadorHistoricoEscolar.run();

			 importadorAlocacoesProfessoresEmTurmas.run();
			 importadorHabilitacoesParaProfessor.run();
			 importadorPerMinVersaoCurso.run();
			
			 importadorAlocacoesProfessoresEmDepartamentos.run();
			
			 importadorAlocacoesDisciplinasEmDepartamentos.run();
			
			 importadorEquivalenciaDisciplinas.run();

		} catch (IllegalArgumentException | IllegalStateException ex) {
			System.err.println(ex.getMessage());
			System.exit(1);
		}
	}
}
