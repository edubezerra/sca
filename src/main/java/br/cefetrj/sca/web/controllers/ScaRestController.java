package br.cefetrj.sca.web.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.cefetrj.sca.dominio.PeriodoLetivo;
import br.cefetrj.sca.dominio.Turma;
import br.cefetrj.sca.service.RequerimentoMatriculaForaPrazoService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/rest")
public class ScaRestController {

	protected Logger logger = Logger
			.getLogger(RequerimentoMatriculaForaPrazoController.class.getName());

	@Autowired
	private RequerimentoMatriculaForaPrazoService service;

	@RequestMapping(value = "/turmas/{siglaDepartamento}", method = RequestMethod.GET, produces = { "application/json; charset=UTF-8" })
	public String getTurmasNoperiodoCorrenteByDepartamento(
			@PathVariable String siglaDepartamento) {

		List<Turma> turmas = service.findTurmasByDepartamentoAndPeriodoLetivo(
				siglaDepartamento, PeriodoLetivo.PERIODO_CORRENTE);

		Map<String, String> mapaTurmas = new HashMap<String, String>();
		for (Turma turma : turmas) {
			mapaTurmas.put(turma.getId().toString(), turma.getCodigo() + " - "
					+ turma.getNomeDisciplina() + " ("
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
}
