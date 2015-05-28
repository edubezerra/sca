package misc;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import br.cefetrj.sca.service.FornecerGradeService;

public class IncluirProfessor {
	private static ApplicationContext context;

	public static void main(String[] args) {
		context = new ClassPathXmlApplicationContext(
				new String[] { "applicationContext.xml" });

		FornecerGradeService serv = (FornecerGradeService) context
				.getBean("FornecerGradeServiceBean");

		serv.incluirProfessor("1506449", "Eduardo Bezerra");

		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}