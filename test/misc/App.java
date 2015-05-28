package misc;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import br.cefetrj.sca.dominio.Professor;
import br.cefetrj.sca.service.FornecerGradeService;

public class App {
	private static ApplicationContext context;

	public static void main(String[] args) {
		context = new ClassPathXmlApplicationContext(
				new String[] { "applicationContext.xml" });

		FornecerGradeService serv = (FornecerGradeService) context
				.getBean("FornecerGradeServiceBean");

		Professor professor = serv.validarProfessor("1506449");
		System.out.println(professor.getNome());

		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}