package br.cefetrj.sca.web.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import br.cefetrj.sca.dominio.Aluno;
import br.cefetrj.sca.service.MonografiaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.cefetrj.sca.dominio.PeriodoLetivo;
import br.cefetrj.sca.dominio.Turma;
import br.cefetrj.sca.dominio.usuarios.Usuario;
import br.cefetrj.sca.service.RequerimentoMatriculaForaPrazoService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/rest")
public class ScaRestController {

	protected Logger logger = Logger.getLogger(RequerimentoMatriculaForaPrazoController.class.getName());

	@Autowired
	private RequerimentoMatriculaForaPrazoService service;

	@Autowired
	private MonografiaService monografiaService;

	@RequestMapping(value = "/turmas/{siglaDepartamento}", method = RequestMethod.GET, produces = {
			"application/json; charset=UTF-8" })
	public String getTurmasNoperiodoCorrenteByDepartamento(@PathVariable String siglaDepartamento) {

		Usuario usr = UsuarioController.getCurrentUser();
		String matriculaAluno = usr.getLogin();

		List<Turma> turmas = service.findTurmasByDepartamentoAndPeriodoLetivo(matriculaAluno, siglaDepartamento,
				PeriodoLetivo.PERIODO_CORRENTE);

		Map<String, String> mapaTurmas = new HashMap<String, String>();
		for (Turma turma : turmas) {
			mapaTurmas.put(turma.getId().toString(), turma.getCodigo() + " - " + turma.getNomeDisciplina() + " ("
					+ turma.getDisciplina().getCodigo() + ")");
		}
		String mapAsJson;
		try {
			mapAsJson = new ObjectMapper().writeValueAsString(mapaTurmas);
			return mapAsJson;
		} catch (JsonProcessingException e) {
			return null;
		}
	}

	@RequestMapping(value = "/alunos/", method = RequestMethod.GET, produces = {
			"application/json; charset=UTF-8" })
	public String alunoAutocomplete(String q) {
		List<Aluno> alunos = monografiaService.alunoAutocomplete(q);
		List<String> nomes = new ArrayList<>(alunos.size());
		for (Aluno aluno : alunos) {
			nomes.add(aluno.getNome());
		}
		nomes.sort(String::compareTo);

		String mapAsJson;
		try {
			mapAsJson = new ObjectMapper().writeValueAsString(nomes);
			return mapAsJson;
		} catch (JsonProcessingException e) {
			return null;
		}
	}
}
