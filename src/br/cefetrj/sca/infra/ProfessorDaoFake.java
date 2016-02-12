package br.cefetrj.sca.infra;

import java.util.ArrayList;
import java.util.List;

import br.cefetrj.sca.dominio.Disciplina;
import br.cefetrj.sca.dominio.Professor;

public class ProfessorDaoFake implements ProfessorDao {

	private static List<Professor> professores = new ArrayList<Professor>();

	static {
		Professor p;

		p = new Professor("Eduardo Bezerra", "1234");
		p.habilitarPara(new Disciplina("Engenharia de Requisitos", "GSTI7270",
				"4", "72"));
		p.habilitarPara(new Disciplina("Matemática Discreta", "GSTI7201", "4",
				"72"));
		p.habilitarPara(new Disciplina("Padrões de Software", "GSTI5203", "4",
				"72"));
		professores.add(p);

		p = new Professor("Rafael Castaneda", "1235");
		p.habilitarPara(new Disciplina("Programação de Jogos", "GSTI5202", "4",
				"72"));
		professores.add(p);

		p = new Professor("Renato Mauro", "1236");
		professores.add(p);

		p = new Professor("Myrna Amorim", "1237");
		professores.add(p);
	}

	@Override
	public Professor getProfessorPorMatricula(String matricula) {
		for (Professor p : professores) {
			if (p.getMatricula().equals(matricula)) {
				return p;
			}
		}
		return null;
	}

	@Override
	public boolean incluir(Professor p) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean alterar(Professor p) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void excluir(Professor p) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<Professor> obterTodos() {
		// TODO Auto-generated method stub
		return null;
	}

}
