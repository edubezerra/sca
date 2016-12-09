package br.cefetrj.sca.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.cefetrj.sca.dominio.Curso;
import br.cefetrj.sca.dominio.Professor;
import br.cefetrj.sca.dominio.repositories.CursoRepositorio;
import br.cefetrj.sca.dominio.repositories.ProfessorRepositorio;
import br.cefetrj.sca.service.util.DadosAlocacaoCoordenadorAtividades;

@Component
public class CoordenacaoAtividadesComplementaresService {

	@Autowired
	private ProfessorRepositorio professorRepositorio;
	
	@Autowired
	private CursoRepositorio cursoRepo;

	public DadosAlocacaoCoordenadorAtividades listarCoordenadoresAtividadeByCurso() {
		
		Map<String, String> professores = new HashMap<String, String>(); 
		Map<String, String> cursos = new HashMap<String, String>();
		Map<String, String> coordenacaoCursoProf = new HashMap<String, String>();
		
		for(Curso curso: cursoRepo.findAll()){
			cursos.put(curso.getSigla(),curso.getNome());
			if(curso.getCoordenadorAtividadesComplementares()!=null)
				coordenacaoCursoProf.put(curso.getSigla(), curso.getCoordenadorAtividadesComplementares().getMatricula());
			else
				coordenacaoCursoProf.put(curso.getSigla(), null);
		}
		for(Professor professor: professorRepositorio.findAll()){
			professores.put(professor.getMatricula(),professor.getNome());
		}
		
		DadosAlocacaoCoordenadorAtividades dadosAlocacaoCoordenadorAtividades = 
				new DadosAlocacaoCoordenadorAtividades(cursos,professores,coordenacaoCursoProf);
		
		return dadosAlocacaoCoordenadorAtividades;
	}

	public void alocarCoordenadoresAtividadeByCurso(Map<String, String> coordenacaoCursoProf) {

		for (String siglaCurso: coordenacaoCursoProf.keySet()) {
			
			Curso curso = cursoRepo.findCursoBySigla(siglaCurso);
			String matriculaProf = coordenacaoCursoProf.get(siglaCurso);
			Professor professor = professorRepositorio.findProfessorByMatricula(matriculaProf);
			
			curso.setCoordenadorAtividadesComplementares(professor);
			
			cursoRepo.save(curso);
		}
	}
}
