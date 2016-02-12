package misc;

import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import br.cefetrj.sca.dominio.PeriodoLetivo;
import br.cefetrj.sca.dominio.PeriodoLetivo.EnumPeriodo;
import br.cefetrj.sca.dominio.Turma;
import br.cefetrj.sca.infra.jpa.TurmaRepositorioJpa;

public class GetTurmasCursadasPorAluno {
	private static ApplicationContext context;

	public static void main(String[] args) {
		context = new ClassPathXmlApplicationContext(
				new String[] { "applicationContext.xml" });

		TurmaRepositorioJpa dao = new TurmaRepositorioJpa();

		String cpf = "usuarioeic";
		System.out.println("Listando turmas do usuário " + cpf + "...");
		PeriodoLetivo semestreLetivo = new PeriodoLetivo(2015, EnumPeriodo.PRIMEIRO);
		List<Turma> lista = dao.getTurmasCursadas(cpf, semestreLetivo);
		for (Turma turma : lista) {
			System.out.println(turma.getCodigo());
		}
		System.out.println("Feito!");
	}
}