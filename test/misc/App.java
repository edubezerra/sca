package misc;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import br.cefetrj.sca.dominio.gradesdisponibilidade.FichaDisponibilidade;
import br.cefetrj.sca.service.FornecerGradeDisponibilidadeService;

public class App {
	private static ApplicationContext context;

	public static void main(String[] args) {
		context = new ClassPathXmlApplicationContext(
				new String[] { "applicationContext.xml" });

		FornecerGradeDisponibilidadeService serv = (FornecerGradeDisponibilidadeService) context
				.getBean("FornecerGradeServiceBean");

		FichaDisponibilidade ficha = serv.validarProfessor("1506449");
		System.out.println(ficha.getNomeProfessor());

		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}