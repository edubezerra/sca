package br.cefetrj.sca.infra.cargadados;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import br.cefetrj.sca.dominio.repositories.AlunoRepositorio;

@Component
public class ImportadorTudo {

	public static AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
			StandalonePersistenceConfig.class);

	// private static EntityManagerFactory emf = (EntityManagerFactory) context
	// .getBean("entityManagerFactory");
	//
	// public static EntityManager entityManager = emf.createEntityManager();

	@Autowired
	ImportadorQuestionarioAvaliacaoTurmas importadorQuestionarioAvaliacaoTurmas;

	@Autowired
	ImportadorCursos importadorCursos;

	@Autowired
	ImportadorDisciplinas importadorDisciplinas;

	@Autowired
	ImportadorPreReqs importadorPreReqs;

	@Autowired
	ImportadorAtividadesComp importadorAtividadesComp;

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

	public static void main(String[] args) {

		ImportadorTudo importador = context.getBean(ImportadorTudo.class);

		// ImportadorHistoricosEscolares importador = context
		// .getBean(ImportadorHistoricosEscolares.class);
		importador.run();

		// entityManager = emf.createEntityManager();
		//
		// importador.run();
		// entityManager.close();
		// emf.close();
	}

	@Transactional
	public void run() {
		try {
			importadorQuestionarioAvaliacaoTurmas.run();
			importadorCursos.run();
			importadorDisciplinas.run();
			importadorPreReqs.run();
			importadorAtividadesComp.run();
			importadorAlunos.run();
			importadorProfessores.run();
			importadorDepartamentos.run();

			importadorHistoricoEscolar.run();

			importadorTurmasComInscricoes.run();
			importadorAlocacoesProfessoresEmTurmas.run();
			importadorHabilitacoesParaProfessor.run();

			importadorAlocacoesProfessoresEmDepartamentos.run();

			importadorAlocacoesDisciplinasEmDepartamentos.run();

		} catch (IllegalArgumentException | IllegalStateException ex) {
			System.err.println(ex.getMessage());
			System.exit(1);
		}
	}
}
