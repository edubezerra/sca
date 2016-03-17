package br.cefetrj.sca.infra.cargadados;

import javax.persistence.EntityManagerFactory;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class ImportadorTudo {

	static ImportadorAlunos importadorAlunos;

	public static AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
			StandalonePersistenceConfig.class);

	public static EntityManagerFactory emf = (EntityManagerFactory) context
			.getBean("entityManagerFactory");

	public static void main(String[] args) {
		ImportadorTudo importador = context.getBean(ImportadorTudo.class);
		importador.run();
	}

	public void run() {
		try {
			new ImportadorQuestionarioAvaliacaoTurmas().run();
			new ImportadorCursos().run();
			new ImportadorDisciplinas().run();
			new ImportadorPreReqs().run();
			new ImportadorAlunos().run();
			new ImportadorDocentes().run();
			new ImportadorDepartamentos().run();
			new ImportadorHistoricosEscolares().run();
			new ImportadorInscricoes().run();
		} catch (IllegalArgumentException | IllegalStateException ex) {
			System.err.println(ex.getMessage());
			System.exit(1);
		}
	}
}
