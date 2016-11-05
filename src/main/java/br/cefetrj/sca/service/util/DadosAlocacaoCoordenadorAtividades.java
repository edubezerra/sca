package br.cefetrj.sca.service.util;

import java.util.Map;

public class DadosAlocacaoCoordenadorAtividades {

	private Map<String, String> cursos;
	private Map<String, String> professores;
	private Map<String, String> coordenacaoCursoProf;

	public DadosAlocacaoCoordenadorAtividades(Map<String, String> cursos, Map<String, String> professores,
			Map<String, String> coordenacaoCursoProf) {
		super();
		this.cursos = cursos;
		this.professores = professores;
		this.coordenacaoCursoProf = coordenacaoCursoProf;
	}

	public Map<String, String> getProfessores() {
		return MapUtil.sortByValue(professores);
	}

	public void setProfessores(Map<String, String> professores) {
		this.professores = professores;
	}

	public Map<String, String> getCursos() {
		return cursos;
	}

	public void setCursos(Map<String, String> cursos) {
		this.cursos = cursos;
	}

	public Map<String, String> getCoordenacaoCursoProf() {
		return coordenacaoCursoProf;
	}

	public void setCoordenacaoCursoProf(Map<String, String> coordenacaoCursoProf) {
		this.coordenacaoCursoProf = coordenacaoCursoProf;
	}

}
