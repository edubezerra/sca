package br.cefetrj.sca.apresentacao;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.cefetrj.sca.dominio.Aluno;
import br.cefetrj.sca.dominio.Disciplina;
import br.cefetrj.sca.dominio.Turma;
import br.cefetrj.sca.service.SolicitacaoIsencaoDisciplinasService;
import br.cefetrj.sca.service.SolicitacaoMatriculaForaPrazoService;

@RestController
@RequestMapping("/rest")
public class ScaRestController {

	protected Logger logger = Logger
			.getLogger(SolicitacaoMatriculaForaPrazoController.class.getName());

	@Autowired
	private SolicitacaoMatriculaForaPrazoService service;

	@Autowired
	private SolicitacaoIsencaoDisciplinasService isencaoService;

	@RequestMapping(value = "/turma/{codigoTurma}", method = RequestMethod.GET, headers = "Accept=application/json")
	public Map<String, String> changeTaskStatus(@PathVariable String codigoTurma)
			throws ParseException {
		Map<String, String> turmaDisciplina = new HashMap<String, String>();
		Turma turma = service.getTurmaPorCodigo(codigoTurma);
		if (turma != null) {
			String nomeDisciplina = turma.getDisciplina().getCodigo() + " - "
					+ turma.getDisciplina().getNome();
			turmaDisciplina.put(codigoTurma, nomeDisciplina);
		} else {
			turmaDisciplina.put(codigoTurma, "Turma não encontrada");
		}

		return turmaDisciplina;

	}

	@RequestMapping(value = "/disciplina/{codigoDisciplina}", method = RequestMethod.GET, headers = "Accept=application/json")
	public Map<String, String> changeCodigoDisciplina(HttpSession session,
			@PathVariable String codigoDisciplina) throws ParseException {
		Map<String, String> mapaDisciplina = new HashMap<String, String>();
		Aluno aluno = (Aluno) session.getAttribute("aluno");
		String idAluno = aluno.getId().toString();
		Disciplina disciplina = isencaoService.getDisciplinaPorCodigo(
				codigoDisciplina, idAluno);
		if (disciplina != null) {
			String nomeDisciplina = disciplina.getCodigo() + " - "
					+ disciplina.getNome();
			mapaDisciplina.put(codigoDisciplina, nomeDisciplina);
		} else {
			mapaDisciplina.put(codigoDisciplina, "Disciplina não encontrada");
		}

		return mapaDisciplina;

	}
}
