package br.cefetrj.sca.web.controllers;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import br.cefetrj.sca.dominio.Aluno;
import br.cefetrj.sca.dominio.PeriodoLetivo;
import br.cefetrj.sca.dominio.PeriodoLetivo.EnumPeriodo;
import br.cefetrj.sca.dominio.inclusaodisciplina.Comprovante;
import br.cefetrj.sca.dominio.inclusaodisciplina.MatriculaForaPrazo;
import br.cefetrj.sca.dominio.usuarios.Usuario;
import br.cefetrj.sca.service.MatriculaForaPrazoService;
import br.cefetrj.sca.service.util.FichaMatriculaForaPrazo;

@Controller
@RequestMapping("/requerimentoMatricula")
public class MatriculaForaPrazoController {

	protected Logger logger = Logger.getLogger(MatriculaForaPrazoController.class.getName());

	@Autowired
	private MatriculaForaPrazoService service;

	@RequestMapping(value = "/{*}", method = RequestMethod.GET)
	public String get(Model model) {
		model.addAttribute("error", "Erro: página não encontrada.");
		return "/homeView";
	}

	/**
	 * Invocado quando o usuário (aluno) solicita a realização de matrículas
	 * fora do prazo.
	 */
	@RequestMapping(value = "/solicitaInclusaoDisciplinas", method = RequestMethod.POST)
	public String solicitaInclusao(Model model, HttpSession sessao) {
		Usuario usr = UsuarioController.getCurrentUser();
		String matricula = usr.getLogin();
		try {
			FichaMatriculaForaPrazo ficha = service.criarFichaSolicitacao(matricula);

			sessao.setAttribute("fichaMatriculaForaPrazo", ficha);

			model.addAttribute("departamentos", ficha.getDepartamentos());
			model.addAttribute("matricula", ficha.getMatriculaAluno());
			model.addAttribute("aluno", ficha.getAluno());
			model.addAttribute("periodoLetivo", PeriodoLetivo.PERIODO_CORRENTE);
			model.addAttribute("turmasDisponiveis", service.findTurmasByPeriodoLetivo(PeriodoLetivo.PERIODO_CORRENTE));

			return "/requerimentoMatricula/requerimentoMatriculaView";
		} catch (Exception exc) {
			model.addAttribute("error", exc.getMessage());
			return "/homeView";
		}
	}

	@RequestMapping(value = "/visualizaRequerimentos", method = RequestMethod.GET)
	public String paginaInicialInclusao(HttpServletRequest request, Model model) {
		try {
			Usuario usr = UsuarioController.getCurrentUser();
			String matriculaAluno = usr.getLogin();
			this.carregaHomeView(model, matriculaAluno);
			model.addAttribute("periodoLetivo", PeriodoLetivo.PERIODO_CORRENTE);
			return "/requerimentoMatricula/visualizaRequerimentosView";
		} catch (Exception exc) {
			model.addAttribute("error", exc.getMessage());
			return "/requerimentoMatricula/requerimentoMatriculaView";
		}
	}

	@RequestMapping(value = "/adicionarItemMatriculaForaPrazo", method = RequestMethod.POST)
	public String adicionarItemEmRequerimento(@RequestParam String nomeDepartamento,
			@RequestParam String descritorTurma, @RequestParam int opcao, Model model, HttpSession sessao)
					throws IOException {

		FichaMatriculaForaPrazo ficha = (FichaMatriculaForaPrazo) sessao.getAttribute("fichaMatriculaForaPrazo");

		try {
			ficha.adicionarItemRequerimento(descritorTurma, nomeDepartamento, opcao);
			model.addAttribute("sucesso", "Item foi adicionado ao requerimento.");
		} catch (Exception exc) {
			model.addAttribute("error", exc.getMessage());
		}

		model.addAttribute("departamentos", ficha.getDepartamentos());
		model.addAttribute("matricula", ficha.getMatriculaAluno());
		model.addAttribute("aluno", ficha.getAluno());
		model.addAttribute("periodoLetivo", PeriodoLetivo.PERIODO_CORRENTE);
		model.addAttribute("itensRequerimento", ficha.getItensRequerimentos());
		model.addAttribute("turmasDisponiveis", service.findTurmasByPeriodoLetivo(PeriodoLetivo.PERIODO_CORRENTE));

		return "/requerimentoMatricula/requerimentoMatriculaView";
	}

	@RequestMapping(value = "/incluiSolicitacao", method = RequestMethod.POST)
	public String registrarRequerimentos(@RequestParam String observacao, @RequestParam MultipartFile file,
			HttpServletRequest request, Model model, HttpSession sessao) throws IOException {

		Usuario usr = UsuarioController.getCurrentUser();
		String matricula = usr.getLogin();

		FichaMatriculaForaPrazo ficha = (FichaMatriculaForaPrazo) sessao.getAttribute("fichaMatriculaForaPrazo");

		validarArquivoComprovanteMatricula(file);
		ficha.setComprovante(file.getContentType(), file.getBytes(), file.getName());

		try {
			service.registrarSolicitacao(ficha);
			model.addAttribute("sucesso", "Solicitação registrada com sucesso.");
			this.carregaHomeView(model, matricula);
		} catch (Exception exc) {
			model.addAttribute("error", exc.getMessage());
			model.addAttribute("aluno", ficha.getAluno());
			model.addAttribute("periodoLetivo", PeriodoLetivo.PERIODO_CORRENTE);
		}

		return "/requerimentoMatricula/visualizaRequerimentosView";
	}

	@RequestMapping(value = "/listarSolicitacoes", method = RequestMethod.POST)
	public String listarSolicitacoes(@ModelAttribute("login") String matriculaAluno, @RequestParam int ano,
			@RequestParam EnumPeriodo periodo, HttpServletRequest request, HttpServletResponse response, Model model) {

		try {
			Aluno aluno = service.findAlunoByMatricula(matriculaAluno);
			MatriculaForaPrazo solicitacaoAtual = service.findMatriculaForaPrazoByAlunoAndPeriodo(aluno.getId(),
					new PeriodoLetivo(ano, periodo));

			model.addAttribute("solicitacaoAtual", solicitacaoAtual);
			model.addAttribute("aluno", aluno);

			return "/requerimentoMatricula/listaSolicitacoesView";
		} catch (Exception exc) {
			model.addAttribute("error", exc.getMessage());
			this.carregaHomeView(model, matriculaAluno);
			return "/requerimentoMatricula/visualizaRequerimentosView";
		}
	}

	@RequestMapping(value = "/downloadFile", method = RequestMethod.POST)
	public void downloadFile(@ModelAttribute("login") String matricula, @RequestParam Long solicitacaoId,
			HttpServletRequest request, HttpServletResponse response, HttpSession sessao) {
		try {
			FichaMatriculaForaPrazo ficha = (FichaMatriculaForaPrazo) sessao.getAttribute("fichaMatriculaForaPrazo");
			Comprovante comprovante = ficha.getComprovante();
			GerenteArquivos.downloadFile(matricula, solicitacaoId, request, response, comprovante);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void validarArquivoComprovanteMatricula(MultipartFile file) {
		if (file == null) {
			throw new IllegalArgumentException("O comprovante da matrícula no período corrente deve ser fornecido.");
		}
		if (file.getSize() > MatriculaForaPrazoService.TAMANHO_MAXIMO_COMPROVANTE) {
			throw new IllegalArgumentException("O arquivo de comprovante deve ter 10mb no máximo");
		}
		String[] tiposAceitos = { "application/pdf", "image/jpeg", "image/png" };
		if (ArrayUtils.indexOf(tiposAceitos, file.getContentType()) < 0) {
			throw new IllegalArgumentException("O arquivo de comprovante deve ser no formato PDF, JPEG ou PNG");
		}
	}

	private void carregaHomeView(Model model, String matriculaAluno) {
		Aluno aluno = service.findAlunoByMatricula(matriculaAluno);
		List<MatriculaForaPrazo> solicitacoes = service.findMatriculasForaPrazoByAluno(aluno.getId());
		List<PeriodoLetivo> listaSemestresLetivos = MatriculaForaPrazo.semestresCorrespondentes(solicitacoes);
		model.addAttribute("listaSemestresLetivos", listaSemestresLetivos);
		model.addAttribute("aluno", aluno);
	}
}
