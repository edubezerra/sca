package br.cefetrj.sca.web.controllers;

import java.io.IOException;
import java.util.SortedMap;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import br.cefetrj.sca.dominio.Aluno;
import br.cefetrj.sca.dominio.PeriodoLetivo;
import br.cefetrj.sca.dominio.PeriodoLetivo.EnumPeriodo;
import br.cefetrj.sca.dominio.Turma;
import br.cefetrj.sca.dominio.matriculaforaprazo.MatriculaForaPrazo;
import br.cefetrj.sca.dominio.usuarios.Usuario;
import br.cefetrj.sca.service.RequerimentoMatriculaForaPrazoService;
import br.cefetrj.sca.service.util.FichaMatriculaForaPrazo;

/**
 * TODO
 * 
 * remoção de item - OK
 * 
 * centralização de colunas bootstrap - OK
 * 
 * atualização de turmas em função de seleção de departamento - OK
 * 
 * até três itens por requerimento - OK
 * 
 * alocações ***PARCIAIS*** disciplinas a departamentos - OK
 * 
 * cadastro de alocações disciplinas a departamentos
 * 
 * análise de itens
 * 
 * aluno deve visualizar suas matrículas regulares no período no momento da
 * realização do requerimento
 * 
 * @author Eduardo
 *
 */
@Controller
@RequestMapping("/matriculaForaPrazo/requerimento")
public class RequerimentoMatriculaForaPrazoController {

	protected Logger logger = Logger
			.getLogger(RequerimentoMatriculaForaPrazoController.class.getName());

	@Autowired
	private RequerimentoMatriculaForaPrazoService service;

	@RequestMapping(value = "/{*}", method = RequestMethod.GET)
	public String get(Model model) {
		model.addAttribute("error", "Erro: página não encontrada.");
		return "/visualizarRequerimentosView";
	}

	/**
	 * Invocado quando o aluno solicita iniciar o registro de um novo
	 * requerimento de matrícula fora do prazo.
	 */
	@RequestMapping(value = "/iniciarRegistroRequerimento", method = RequestMethod.POST)
	public String iniciarRegistroRequerimento(Model model, HttpSession sessao) {
		Usuario usr = UsuarioController.getCurrentUser();
		String matriculaAluno = usr.getMatricula();
		try {
			FichaMatriculaForaPrazo ficha = service
					.criarFichaSolicitacao(matriculaAluno);

			sessao.setAttribute("ficha", ficha);

			sessao.setAttribute("periodoLetivo", PeriodoLetivo.PERIODO_CORRENTE);

			sessao.setAttribute("turmasCursadas", service
					.findTurmasCursadasPorAlunoNoPeriodo(matriculaAluno,
							PeriodoLetivo.PERIODO_CORRENTE));

			sessao.setAttribute("turmasDisponiveis", service
					.findTurmasByPeriodoLetivo(matriculaAluno,
							PeriodoLetivo.PERIODO_CORRENTE));

			return "/matriculaForaPrazo/requerimento/registrarRequerimentoView";
		} catch (Exception exc) {
			model.addAttribute("error", exc.getMessage());
			return "/homeView";
		}
	}

	/**
	 * Método invocado quando o aluno seleciona a opção (link) para realizar um
	 * requerimento. Esse método produz a lista de requerimentos anteriores. A
	 * partir dessa lista, o aluno pode selecionar o requerimento correspondente
	 * ao período letivo corrente para edição. Pode também selecionar qualquer
	 * requerimento de períodos letivos anteriores para visualização apenas.
	 */
	@RequestMapping(value = "/visualizarRequerimentos", method = RequestMethod.GET)
	public String visualizarRequerimentos(Model model) {
		Usuario usr = UsuarioController.getCurrentUser();
		String matriculaAluno = usr.getMatricula();
		this.definirModelParaVisualizacaoRequerimentos(model, matriculaAluno);
		return "/matriculaForaPrazo/requerimento/visualizarRequerimentosView";
	}

	/**
	 * Método invocado quando o aluno submete dados para a adição de um novo
	 * item (turma/disciplina) ao requerimento de matrícula fora do prazo.
	 */
	@RequestMapping(value = "/adicionarItem", method = RequestMethod.POST)
	public String adicionarItemEmRequerimento(
			@RequestParam String siglaDepartamento, @RequestParam Long idTurma,
			@RequestParam int opcao, @RequestParam String observacao,
			Model model, HttpSession sessao) throws IOException {

		FichaMatriculaForaPrazo ficha = (FichaMatriculaForaPrazo) sessao
				.getAttribute("ficha");

		if (ficha.getItensRequerimento().size() < MatriculaForaPrazo.QTD_MAXIMA_ITENS) {

			try {

				Turma turma = service.findTurmaById(idTurma);
				ficha.adicionarItemRequerimento(turma.getId(),
						turma.getCodigo(), turma.getDisciplina(),
						siglaDepartamento, opcao, observacao);
				model.addAttribute("sucesso",
						"Item foi corretamente adicionado ao requerimento.");
			} catch (Exception e) {
				model.addAttribute("error", e.getMessage());
			}
		} else {
			model.addAttribute("error",
					"A quantidade máxima possível de itens neste requerimento é "
							+ MatriculaForaPrazo.QTD_MAXIMA_ITENS);
		}
		return "/matriculaForaPrazo/requerimento/registrarRequerimentoView";
	}

	/**
	 * Método invocado quando o aluno solicita que um item do requerimento seja
	 * removido. Esse item é identificado pelo ID da turma correspondente, que é
	 * passado como parâmetro GET.
	 */
	@RequestMapping("/item/remove/{idTurma}")
	public String removerItem(@PathVariable int idTurma, Model model,
			HttpSession sessao) {

		FichaMatriculaForaPrazo ficha = (FichaMatriculaForaPrazo) sessao
				.getAttribute("ficha");

		ficha.removerItemRequerimento(idTurma);

		return "/matriculaForaPrazo/requerimento/registrarRequerimentoView";
	}

	/**
	 * Método invocado quando o aluno confirma o registro dos itens de
	 * requerimento de matrícula fora do prazo.
	 */
	@RequestMapping(value = "/confirmarRegistroRequerimento", method = RequestMethod.POST)
	public String definirComprovante(// @RequestParam MultipartFile file,
			HttpServletRequest request, Model model, HttpSession sessao) {

		FichaMatriculaForaPrazo ficha = (FichaMatriculaForaPrazo) sessao
				.getAttribute("ficha");

		Usuario usr = UsuarioController.getCurrentUser();
		String matricula = usr.getMatricula();

		try {
			service.confirmarRegistroRequerimento(ficha);
			model.addAttribute("sucesso", "Solicitação registrada com sucesso.");
			this.definirModelParaVisualizacaoRequerimentos(model, matricula);
		} catch (Exception exc) {
			model.addAttribute("error", exc.getMessage());
			model.addAttribute("aluno", ficha.getAluno());
			model.addAttribute("periodoLetivo", PeriodoLetivo.PERIODO_CORRENTE);
		}

		return "/matriculaForaPrazo/requerimento/visualizarRequerimentosView";

	}

	/**
	 * Método invocado quando o aluno solicita visualizar os detalhes de um
	 * requerimento que realizou anteriormente.
	 */
	@RequestMapping(value = "/visualizarDetalhesRequerimento", method = RequestMethod.POST)
	public String visualizarDetalhesRequerimento(@RequestParam int ano,
			@RequestParam EnumPeriodo periodo, Model model) {

		Usuario usr = UsuarioController.getCurrentUser();
		String matriculaAluno = usr.getMatricula();

		try {
			MatriculaForaPrazo requerimento;
			requerimento = service.findMatriculaForaPrazoByAlunoAndPeriodo(
					matriculaAluno, new PeriodoLetivo(ano, periodo));

			model.addAttribute("requerimento", requerimento);

			return "/matriculaForaPrazo/requerimento/listarDetalhesRequerimento";
		} catch (Exception exc) {
			model.addAttribute("error", exc.getMessage());
			this.definirModelParaVisualizacaoRequerimentos(model,
					matriculaAluno);
			return "/matriculaForaPrazo/requerimento/visualizarRequerimentosView";
		}
	}

	private void definirModelParaVisualizacaoRequerimentos(Model model,
			String matriculaAluno) {
		Aluno aluno = service.findAlunoByMatricula(matriculaAluno);

		SortedMap<PeriodoLetivo, MatriculaForaPrazo> mapa = service
				.findMatriculasForaPrazoByAluno(aluno.getId());

		model.addAttribute("listaPeriodosLetivos", mapa.keySet());

		if (mapa.get(PeriodoLetivo.PERIODO_CORRENTE) != null) {
			model.addAttribute("periodoLetivoCorrente",
					PeriodoLetivo.PERIODO_CORRENTE);
		}

		model.addAttribute("aluno", aluno);
	}
}
