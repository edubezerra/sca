package br.cefetrj.sca.web.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.cefetrj.sca.dominio.Departamento;
import br.cefetrj.sca.dominio.DocumentoProfessor;
import br.cefetrj.sca.dominio.Professor;
import br.cefetrj.sca.dominio.repositories.DepartamentoRepositorio;
import br.cefetrj.sca.dominio.repositories.DocumentoProfessorRepositorio;
import br.cefetrj.sca.dominio.repositories.ProfessorRepositorio;
import br.cefetrj.sca.dominio.usuarios.Usuario;
import br.cefetrj.sca.service.DocumentoProfessorService;


@Controller
@RequestMapping("/pastaProfessor")
public class PastaProfessorController {
	
	protected Logger logger = Logger.getLogger(RealizarInscricaoController.class.getName());
	
	@Autowired
	private ProfessorRepositorio professorRepositorio;
	
	@Autowired
	private DocumentoProfessorRepositorio documentoRepositorio;
	
	@Autowired
	private DocumentoProfessorService documentoService;
	
	@Autowired
	private DepartamentoRepositorio departamentoRepositorio;
	
	
	@RequestMapping(value = {"/" , "/dashboard"}, method = RequestMethod.GET)
	public String dashboard(HttpServletRequest request, Model model) {
		try {
			Usuario usr = UsuarioController.getCurrentUser();
			String matricula = usr.getMatricula();
			
			List<DocumentoProfessor> documentos = documentoRepositorio.findAllByMatricula(matricula);
			model.addAttribute("documentos", documentos);
			
			return "/pastaProfessor/dashboardView";
		} catch (Exception exc) {
			model.addAttribute("error", exc.getMessage());
			return "/homeView";
		}
	}
	
	@RequestMapping(value = "/professores", method = RequestMethod.GET)
	public String professorList(HttpServletRequest request, Model model) {
		try {
			Usuario usr = UsuarioController.getCurrentUser();
			String matricula = usr.getMatricula();
			
			Departamento departamento = departamentoRepositorio.findDepartamentoByProfessor(matricula);
			
			List<Professor> professoresList = new ArrayList<Professor>(departamento.getProfessores());
			Collections.sort(professoresList, new Comparator<Professor>(){

				@Override
				public int compare(Professor o1, Professor o2) {
					return o1.getPessoa().getNome().compareTo(o2.getPessoa().getNome());
				}
				
			});
			
			model.addAttribute("professores", professoresList);
			model.addAttribute("departamento", departamento);
			
			return "/pastaProfessor/listaProfessores";
		} catch (Exception exc) {
			model.addAttribute("error", exc.getMessage());
			return "/homeView";
		}
	}
	
	@RequestMapping(value = "/detalhesProfessor/{professorId}", method = RequestMethod.GET)
	public String detalhesProfessor(HttpServletRequest request,
			@PathVariable(value="professorId") long professorId,
			Model model) {
		try {
			Professor professor = professorRepositorio.findOne(professorId);
			
			List<DocumentoProfessor> documentos = documentoRepositorio.findAllByMatricula(professor.getMatricula());
			model.addAttribute("professor", professor);
			model.addAttribute("documentos", documentos);
			
			return "/pastaProfessor/detalhesProfessor";
		} catch (Exception exc) {
			model.addAttribute("error", exc.getMessage());
			return "/homeView";
		}
	}
	
	
	
	@RequestMapping(value = "/registrarDocumento", method = RequestMethod.POST)
	public String registrarDocumento(HttpSession session,
			@RequestParam String nome,
			@RequestParam String categoria,
			@RequestParam MultipartFile file,
			Model model,
			final RedirectAttributes redirectAttributes)  throws IOException {

		Usuario usr = UsuarioController.getCurrentUser();
		String matricula = usr.getMatricula();
		
		try {
			Professor professor = professorRepositorio.findProfessorByMatricula(matricula);
			documentoService.saveDocumentoProfessor(nome, categoria, file, professor);
			
			redirectAttributes.addFlashAttribute("info", "Documento submetido.");
		} catch (Exception exc) {
			redirectAttributes.addFlashAttribute("error", exc.getMessage());
		}
		
		return "redirect:/pastaProfessor/dashboard";
	}

	@RequestMapping(value = "/detalhesProfessor/{professorId}/registrarDocumento", method = RequestMethod.POST)
	public String registrarDocumentoComoCoordenador(HttpSession session,
			@RequestParam String nome,
			@RequestParam String categoria,
			@RequestParam MultipartFile file,
			@PathVariable(value="professorId") long professorId,
			Model model,
			final RedirectAttributes redirectAttributes)  throws IOException {
		
		try {
			
			Professor professor = professorRepositorio.findOne(professorId);
			documentoService.saveDocumentoProfessor(nome, categoria, file, professor);
			
			redirectAttributes.addFlashAttribute("info", "Documento submetido.");
		} catch (Exception exc) {
			redirectAttributes.addFlashAttribute("error", exc.getMessage());
		}
		
		return "redirect:/pastaProfessor/detalhesProfessor/"+professorId;
	}
	
	@RequestMapping(value = "/excluirDocumento/{documentId}", method = RequestMethod.GET)
	public String excluirDocumento(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(value="documentId") long idDocumento,
			final RedirectAttributes redirectAttributes) {
		try {
			documentoService.deleteDocumentoProfessor(idDocumento);
			
			redirectAttributes.addFlashAttribute("info", "Documento excluído!.");
		}
		catch(Exception ex)
		{
			redirectAttributes.addFlashAttribute("error", ex.getMessage());
		}
		
		return "redirect:/pastaProfessor/dashboard";
	}
	
	@RequestMapping(value = "/detalhesProfessor/{professorId}/excluirDocumento/{documentId}", method = RequestMethod.GET)
	public String excluirDocumentoComoCoordenador(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(value="documentId") long idDocumento,
			@PathVariable(value="professorId") long professorId,
			final RedirectAttributes redirectAttributes) {
		try {
			documentoService.deleteDocumentoProfessor(idDocumento);
			
			redirectAttributes.addFlashAttribute("info", "Documento excluído!.");
		}
		catch(Exception ex)
		{
			redirectAttributes.addFlashAttribute("error", ex.getMessage());
		}
		
		return "redirect:/pastaProfessor/detalhesProfessor/"+professorId;
	}
	
	@RequestMapping(value = "/visualizarDocumento/{documentId}", method = RequestMethod.GET)
	public void visualizarDocumento(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(value="documentId") long idDocumento) {
		
		try {
			DocumentoProfessor doc = documentoRepositorio.findDocumentoProfessorById(idDocumento);
			
			GerenteArquivos.downloadFile(response, doc.getDocumento());
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		
	}
	
}
