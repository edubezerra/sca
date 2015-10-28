package misc;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import br.cefetrj.sca.dominio.repositorio.ProfessorRepositorio;

public class IncluirProfessor {
	private static ApplicationContext context;

	public static void main(String[] args) {
		context = new ClassPathXmlApplicationContext(
				new String[] { "applicationContext.xml" });

		ProfessorRepositorio serv = (ProfessorRepositorio) context
				.getBean("ProfessorRepoBean");

		serv.adicionar("1506449", "Eduardo Bezerra");

		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}