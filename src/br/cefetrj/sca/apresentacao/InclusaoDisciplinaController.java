package br.cefetrj.sca.apresentacao;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;

import br.cefetrj.sca.dominio.Aluno;
import br.cefetrj.sca.dominio.SemestreLetivo.EnumPeriodo;
import br.cefetrj.sca.dominio.inclusaodisciplina.SolicitacaoInclusao;
import br.cefetrj.sca.service.InclusaoDisciplinaService;

@Controller
@SessionAttributes("cpf")
@RequestMapping("/inclusaoDisciplina")
public class InclusaoDisciplinaController {
	
	protected Logger logger = Logger.getLogger(InclusaoDisciplinaController.class
			.getName());

	@Autowired
	private InclusaoDisciplinaService service;
	
	
	@RequestMapping(value = "/{*}", method = RequestMethod.GET)
	public String get(Model model) {
		model.addAttribute("error", "Erro: página não encontrada.");
		return "/homeView";
	}
	
	@RequestMapping(value = "/solicitaInclusaoDisciplinas", method = RequestMethod.POST)
	public String solicitaInclusao(HttpSession session, @RequestParam("numeroSolicitacoes") int numeroSolicitacoes, Model model) {
		String cpf = (String) session.getAttribute("cpf");
		try {
			service.solicitaInclusao(cpf, numeroSolicitacoes, model);
			return "/inclusaoDisciplina/inclusaoDisciplinaView";
		} catch (Exception exc) {
			model.addAttribute("error", exc.getMessage());
			return "/homeView";
		}
	}
	
	@RequestMapping(value = "/homeInclusao", method = RequestMethod.GET)
	public String paginaInicialInclusao(@ModelAttribute("cpf") String cpf,HttpServletRequest request, Model model){
		try {
			service.homeInclusao(cpf, model);		
			return "/inclusaoDisciplina/homeInclusaoView";
		} catch (Exception exc) {
			model.addAttribute("error", exc.getMessage());
			return "/homeView";
		}
	}
	
	@RequestMapping(value = "/incluiSolicitacao", method = RequestMethod.POST)
	public String incluiDisciplina(@ModelAttribute("cpf") String cpf,
			@RequestParam MultipartFile file, @RequestParam String departamento,
			@RequestParam int opcao, @RequestParam String observacao, @RequestParam int numeroSolicitacoes,
			HttpServletRequest request,	HttpSession session, Model model) throws IOException {
				
		try {
			service.validaSolicitacao(request, cpf , file, departamento, opcao, observacao);
			model.addAttribute("sucesso", "Solicitação registrada.");
		} catch (Exception exc) {
			model.addAttribute("error",	exc.getMessage());
			service.solicitaInclusao(cpf, numeroSolicitacoes, model);
			return "/inclusaoDisciplina/inclusaoDisciplinaView";
		}
		
		service.homeInclusao(cpf, model);			
		return "/inclusaoDisciplina/homeInclusaoView";
	}
	
	@RequestMapping(value = "/listarSolicitacoes", method = RequestMethod.POST)
	public String listarSolicitacoes(@ModelAttribute("cpf") String cpf,
			@RequestParam int ano,
			@RequestParam EnumPeriodo periodo,
			HttpServletRequest request,HttpServletResponse response,
			Model model) {
		
		try {
			Aluno aluno = service.getAlunoByCpf(cpf);
			SolicitacaoInclusao solicitacaoAtual = service.getSolicitacaoByAlunoSemestre(aluno.getId(), ano, periodo);
			
			model.addAttribute("solicitacaoAtual", solicitacaoAtual);
			model.addAttribute("aluno", aluno);
			
			return "/inclusaoDisciplina/listaSolicitacoesView";
		} catch (Exception exc) {
			model.addAttribute("error", exc.getMessage());
			service.homeInclusao(cpf, model);			
			return "/inclusaoDisciplina/homeInclusaoView";
		}
	}
	
	@RequestMapping(value = "/downloadFile", method = RequestMethod.POST)
	public void downloadFile(@ModelAttribute("cpf") String cpf,
							 @RequestParam Long solicitacaoId, 
							 HttpServletRequest request, HttpServletResponse response) {
		try {
			service.downloadFile(cpf, solicitacaoId, request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
	}

}
