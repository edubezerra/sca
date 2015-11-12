package br.cefetrj.sca.infra.cargadados;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import br.cefetrj.sca.dominio.Disciplina;
import br.cefetrj.sca.dominio.Professor;
import br.cefetrj.sca.dominio.repositorio.DisciplinaRepositorio;
import br.cefetrj.sca.dominio.repositorio.ProfessorRepositorio;

public class IncluirHabilitacaoParaProfessor {
	private static ApplicationContext context;

	public static void main(String[] args) {
		context = new ClassPathXmlApplicationContext(
				new String[] { "applicationContext.xml" });

		ProfessorRepositorio pr  = (ProfessorRepositorio) context
				.getBean("ProfessorRepoBean");

		DisciplinaRepositorio dr  = (DisciplinaRepositorio) context
				.getBean("DisciplinaRepoBean");

		Professor professor = pr.getProfessor("1506449");

		Disciplina d;
		d = dr.getDisciplinaPorCodigo("GCC1520");
		professor.habilitarPara(d);
		d = dr.getDisciplinaPorCodigo("GCC1208");
		professor.habilitarPara(d);
		d = dr.getDisciplinaPorCodigo("GCC1518");
		professor.habilitarPara(d);

		pr.atualizar(professor);

	}
}