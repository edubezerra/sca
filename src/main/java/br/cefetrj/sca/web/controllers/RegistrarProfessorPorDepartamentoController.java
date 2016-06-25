package br.cefetrj.sca.web.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import br.cefetrj.sca.dominio.Departamento;
import br.cefetrj.sca.dominio.Professor;
import br.cefetrj.sca.dominio.repositories.DepartamentoRepositorio;
import br.cefetrj.sca.dominio.repositories.ProfessorRepositorio;
import br.cefetrj.sca.service.RegistrarProfessorPorDepartamentoService;

@Controller
@RequestMapping("/cadastroProfessorDepartamento")
public class RegistrarProfessorPorDepartamentoController {

	@Autowired
	private RegistrarProfessorPorDepartamentoService registroProfDepService;

	@Autowired
	ProfessorRepositorio professorRepo;

	@Autowired
	DepartamentoRepositorio departamentoRepo;

	@RequestMapping(value = { "/","/listProfessorDepartamento" }, method = RequestMethod.GET)
	public String listProfessorByDepartamento(ModelMap model) {
		
		try{
		List<Professor> professores = registroProfDepService.findProfessores();
		
		List<Departamento> departamentos = registroProfDepService.findDepartamentos();
		
		Map<String, String> mapProfDep = new HashMap<String, String>();

		for (int i = 0; i < professores.size(); i++) {
			
			Departamento dep = registroProfDepService.findDepartamentoByProfessor(professores.get(i).getMatricula());
			
			String nomeDep = null;
			String matricula = null;
			
			if (registroProfDepService.findDepartamentoByProfessor(professores.get(i).getMatricula()) != null) {
				nomeDep = dep.getSigla();
				matricula = professores.get(i).getMatricula();

			} else{
				matricula = professores.get(i).getMatricula();				
			}		
			
			mapProfDep.put(matricula, nomeDep);
		}
		
		model.addAttribute("mapProfDep", mapProfDep);
		model.addAttribute("departamentos", departamentos);
		model.addAttribute("professores", professores);

		return "/cadastroProfessorDepartamento/cadastroProfessorDepartamento";

		} catch (Exception exc) {
			model.addAttribute("error", exc.getMessage());
			return "/menuPrincipalView";
		}
	}

	/**
	 * Esse método adiciona o Professor a um Departamento.
	 */
	@RequestMapping(value = { "/", "/setListProfessorDepartamento" }, method = RequestMethod.POST)
	public String setListProfessorByDepartamento(Model model, HttpServletRequest request, HttpSession session,
			@RequestParam("matricula") List<String> matriculas,
			@RequestParam("departamento") List<String> departamentos) {
		
		try{
			
		

		for (int j = 0; j < departamentos.size(); j++) {

			int tracoDep = departamentos.get(j).indexOf("-");

			tracoDep = tracoDep + 1;

			Departamento d = registroProfDepService.findDepartamentoBySigla(departamentos.get(j).substring(0, tracoDep - 1));

			for (int i = 0; i < matriculas.size(); i++) {
				int tracoMat = matriculas.get(i).indexOf("-");

				tracoMat = tracoMat + 1;

				if (matriculas.get(i).substring(tracoMat).equals(departamentos.get(j).substring(tracoDep))) {
					Professor p = professorRepo.findProfessorByMatricula(matriculas.get(i).substring(0, tracoMat - 1));
					d.addProfessor(p);
				}
				tracoMat = 0;
			}
			tracoDep = 0;
			departamentoRepo.save(d);
		}
		return "/menuPrincipalView";
		
		} catch (Exception exc) {
			model.addAttribute("error", exc.getMessage());
			return "/menuPrincipalView";
		}
	}
}
