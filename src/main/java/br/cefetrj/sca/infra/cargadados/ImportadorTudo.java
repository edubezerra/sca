package br.cefetrj.sca.infra.cargadados;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import br.cefetrj.sca.dominio.repositories.AlunoRepositorio;

@Component
public class ImportadorTudo {

	public static AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
			StandalonePersistenceConfig.class);

	private static EntityManagerFactory emf = (EntityManagerFactory) context.getBean("entityManagerFactory");

	public static EntityManager entityManager = emf.createEntityManager();

	@Autowired
	AlunoRepositorio alunoRepositorio;

	public static void main(String[] args) {

		ImportadorTudo importador = context.getBean(ImportadorTudo.class);

		System.out.println("# alunos = " + importador.alunoRepositorio.count());

//		entityManager = emf.createEntityManager();
//
//		importador.run();
//		entityManager.close();
//		emf.close();
	}

	public void run() {
		try {
			new ImportadorQuestionarioAvaliacaoTurmas().run();
			new ImportadorCursos().run();
			new ImportadorDisciplinas().run();
			new ImportadorPreReqs().run();
			new ImportadorAtividadesComp().run();
			new ImportadorAlunos().run();
			new ImportadorProfessores().run();
			new ImportadorDepartamentos().run();

			new ImportadorHistoricosEscolares().run();

			new ImportadorTurmasComInscricoes().run();
			new ImportadorAlocacoesProfessoresEmTurmas().run();
			new ImportadorHabilitacoesParaProfessor().run();

			new ImportadorAlocacoesProfessoresEmDepartamentos().run();

			new ImportadorAlocacoesDisciplinasEmDepartamentos().run();

		} catch (IllegalArgumentException | IllegalStateException ex) {
			System.err.println(ex.getMessage());
			System.exit(1);
		}
	}
}
