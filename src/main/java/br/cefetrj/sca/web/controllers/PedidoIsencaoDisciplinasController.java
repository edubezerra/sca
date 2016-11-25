package br.cefetrj.sca.web.controllers;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.cefetrj.sca.dominio.Aluno;
import br.cefetrj.sca.dominio.Departamento;
import br.cefetrj.sca.dominio.Professor;
import br.cefetrj.sca.dominio.isencoes.FichaIsencaoDisciplinas;
import br.cefetrj.sca.dominio.isencoes.ItemPedidoIsencaoDisciplina;
import br.cefetrj.sca.dominio.isencoes.PedidoIsencaoDisciplinas;
import br.cefetrj.sca.dominio.matriculaforaprazo.Comprovante;
import br.cefetrj.sca.dominio.repositories.AlunoRepositorio;
import br.cefetrj.sca.dominio.repositories.DepartamentoRepositorio;
import br.cefetrj.sca.dominio.repositories.PedidoIsencaoDisciplinasRepositorio;
import br.cefetrj.sca.dominio.usuarios.Usuario;
import br.cefetrj.sca.service.PedidoIsencaoDisciplinasService;

@Controller
@RequestMapping("/isencaoDisciplina")
public class PedidoIsencaoDisciplinasController {

	@Autowired
	PedidoIsencaoDisciplinasService service;

	@Autowired
	PedidoIsencaoDisciplinasRepositorio processoIsencaoRepo;

	@Autowired
	DepartamentoRepositorio departamentoRepositorio;

	@Autowired
	AlunoRepositorio alunoRepositorio;

	@RequestMapping(value = "/visualizarProcessoIsencao", method = RequestMethod.GET)
	public String visualizarProcessoIsencao(Model model,
			HttpServletRequest request) {

		System.out
				.println("RegistrarIsencaoDisciplinaController.visualizarProcessoIsencao()");

		try {
			String matricula = UsuarioController.getCurrentUser().getLogin();
			Aluno aluno = service.findAlunoByMatricula(matricula);
			PedidoIsencaoDisciplinas processo = processoIsencaoRepo
					.findByAluno(aluno);

			if (processo != null) {
				model.addAttribute("processo", processo);
				model.addAttribute("itensProcesso", processo.getItens());
				model.addAttribute("situacaoNome", processo.getSituacao());
			}
			model.addAttribute("aluno", aluno);

			return "/isencaoDisciplina/aluno/novoProcessoIsencao";
		} catch (Exception exc) {
			model.addAttribute("error", exc.getMessage());
			return "/menuPrincipalView";
		}
	}

	@RequestMapping(value = "/criaProcessoIsencao", method = RequestMethod.GET)
	public String criarProcessoIsencao(Model model, HttpServletRequest request) {

		System.out
				.println("RegistrarIsencaoDisciplinaController.isencaoDisciplina()");

		String matricula = UsuarioController.getCurrentUser().getLogin();

		try {

			FichaIsencaoDisciplinas ficha = service.findFichaIsencao(matricula);

			model.addAttribute("aluno", ficha.getAluno());
			model.addAttribute("itensIsencao", ficha.getItens());

			return "/isencaoDisciplina/aluno/escolheDisciplinasParaIsentar";

		} catch (Exception exc) {
			model.addAttribute("error", exc.getMessage());
			return "/isencaoDisciplina/visualizarProcessoIsencao";
		}
	}

	@RequestMapping(value = "/validaComprovante", method = RequestMethod.POST)
	public String validaComprovante(Model model, HttpServletRequest request,
			@RequestParam("file") MultipartFile file) {

		System.out
				.println("RegistrarIsencaoDisciplinaController.validaComprovante()");

		try {
			String matricula = UsuarioController.getCurrentUser().getLogin();
			Aluno aluno = service.findAlunoByMatricula(matricula);

			String auxcheckboxes[] = request.getParameterValues("choice");
			Long checkBoxes[] = new Long[auxcheckboxes.length];

			for (int i = 0; i < auxcheckboxes.length; i++) {
				checkBoxes[i] = Long.parseLong(auxcheckboxes[i]);
			}
			PedidoIsencaoDisciplinas processo = processoIsencaoRepo
					.findByAluno(aluno);

			if (processo != null) {
				processo.getItens().removeAll(processo.getItens());

				for (int i = 0; i < processo.getItens().size(); i++) {
					processo.getItens().removeAll(processo.getItens());
				}
				for (int i = 0; i < checkBoxes.length; i++) {
					processo.comMaisUmItem(service
							.getDisciplinaPorId(checkBoxes[i]));
				}
				processoIsencaoRepo.save(processo);
			} else {
				processo = new PedidoIsencaoDisciplinas(aluno);
				for (int i = 0; i < checkBoxes.length; i++) {
					processo.comMaisUmItem(service
							.getDisciplinaPorId(checkBoxes[i]));
					// item.setComprovante(file.getContentType(),
					// file.getBytes(), file.getOriginalFilename());
				}
				processoIsencaoRepo.save(processo);
			}

			model.addAttribute("aluno", aluno);
			model.addAttribute("itemIsencaoByProcessoIsencao",
					processo.getItens());

			return "/isencaoDisciplina/aluno/relacaoMateriaExterna";
		} catch (Exception exc) {
			model.addAttribute("error", exc.getMessage());
			return "/menuPrincipalView";
		}
	}

	/**
	 * Invocado quando da submissão do form com detalhes das disciplinas
	 * externas.
	 */
//	@RequestMapping(value = "/relacaoMateriaExterna", method = RequestMethod.POST)
//	public String registroMateriaExterna(
//			Model model,
//			HttpServletRequest request,
//			@RequestParam("aluno") String matricula,
//			@RequestParam("itemIsencaoByProcessoIsencao") List<String> itemIsencao,
//			@RequestParam("disciplinaAssociada") List<String> disciplinaAssociada) {
//
//		System.out
//				.println("RegistrarIsencaoDisciplinaController.registroMateriaExterna()");
//
//		try {
//			Aluno aluno = service.findAlunoByMatricula(matricula);
//			PedidoIsencaoDisciplinas processo = processoIsencaoRepo
//					.findByAluno(aluno);
//
//			for (int i = 0; i < itemIsencao.size(); i++) {
//				processo.getItens()
//						.get(i)
//						.setDescritorDisciplinaExterna(
//								disciplinaAssociada.get(i));
//			}
//
//			processoIsencaoRepo.save(processo);
//
//			return visualizarProcessoIsencao(model, request);
//
//		} catch (Exception exc) {
//			model.addAttribute("error", exc.getMessage());
//			return "/menuPrincipalView";
//		}
//	}

	/***
	 * 
	 * ======================================
	 * 
	 * Callbacks para o perfil de coordenador
	 * 
	 * ======================================
	 * 
	 * 
	 */

	@RequestMapping(value = "/visualizaAlunosSolicitantes", method = RequestMethod.GET)
	public String visualizaAlunosSolicitantes(Model model) {

		System.out
				.println("RegistrarIsencaoDisciplinaController.visualizaAlunosSolicitantes()");

		Usuario usr = UsuarioController.getCurrentUser();
		String matricula = usr.getMatricula();
		try {

			Professor professor = service.findProfessorByMatricula(matricula);
			if (professor == null) {
				model.addAttribute("error", "Professor não encontrado!");
				return "forward:/isencaoDisciplina/menuPrincipal";
			}

			Departamento departamento = departamentoRepositorio
					.findDepartamentoByProfessor(professor.getMatricula());

			List<Aluno> alunosSolicitantes = processoIsencaoRepo
					.findAlunosSolicitantesByDepartamento(departamento);

			model.addAttribute("professor", professor);
			model.addAttribute("idDepartamento", departamento.getId());
			model.addAttribute("alunosSolicitantes", alunosSolicitantes);

			return "/isencaoDisciplina/professor/visualizaAlunosSolicitantesView";

		} catch (Exception exc) {
			model.addAttribute("error", exc.getMessage());
			return "/menuPrincipalView";
		}
	}

	@RequestMapping(value = { "/", "/analisaItensPedidoIsencao" }, method = RequestMethod.POST)
	public String analisaItensPedidoIsencao(Model model,
			HttpServletRequest request,
			@RequestParam("idDepartamento") Long idDepartamento,
			@RequestParam("matriculaAluno") String matriculaAluno) {

		System.out
				.println("RegistrarIsencaoDisciplinaController.analisaItensIsencao()");

		String matriculaProfessor = UsuarioController.getCurrentUser()
				.getLogin();

		try {
			Professor professor = service
					.findProfessorByMatricula(matriculaProfessor);
			Aluno aluno = service.findAlunoByMatricula(matriculaAluno);
			List<ItemPedidoIsencaoDisciplina> itensIsencao = service
					.findItensIsencaoByDepartamentoAndAluno(idDepartamento,
							matriculaAluno);

			Departamento departamento = departamentoRepositorio
					.findDepartamentoById(idDepartamento);

			model.addAttribute("departamento", departamento);
			model.addAttribute("professor", professor);
			model.addAttribute("aluno", aluno);
			model.addAttribute("itensPedidoIsencao", itensIsencao);

			return "/isencaoDisciplina/professor/analisaItensPedidoIsencaoView";

		} catch (Exception exc) {
			model.addAttribute("error", exc.getMessage());
			return "/menuPrincipalView";
		}
	}

	@ResponseBody
	@RequestMapping(value = "/analisaItemPedidoIsencao")
	public String analisaItemPedidoIsencao(
			@RequestBody HashMap<String, String> params) {

		System.out
				.println("RegistrarIsencaoDisciplinaController.analisaItemPedidoIsencao()");

		try {
			String matriculaAluno = params.get("matriculaAluno");
			String matriculaProfessor = params.get("matriculaProfessor");
			String idItemPedidoIsencao = params.get("idItemPedidoIsencao");
			String novaSituacao = params.get("novaSituacao");
			String motivoIndeferimento = params.get("motivoIndeferimento");

			service.registrarRespostaParaItem(idItemPedidoIsencao,
					matriculaAluno, matriculaProfessor, novaSituacao,
					motivoIndeferimento);

			String mapAsJson;
			try {
				mapAsJson = new ObjectMapper().writeValueAsString(novaSituacao);
				return mapAsJson;
			} catch (JsonProcessingException e) {
				return null;
			}
		} catch (Exception e) {
			return "Houver um erro durante o registro da resposta a este item de isenção.";
		}
	}

	@RequestMapping(value = "/downloadFile", method = RequestMethod.POST)
	public void downloadFile(
			@RequestParam("comprovanteProcIsencao") long idProcIsencao,
			HttpServletRequest request, HttpServletResponse response) {

		System.out
				.println("RegistrarIsencaoDisciplinaController.downloadFile()");

		try {
			List<ItemPedidoIsencaoDisciplina> listIten = processoIsencaoRepo
					.findItemIsencaoByProcessoIsencao(idProcIsencao);
			long idQualquerItemIsencao = 0;
			for (int i = 0; i < listIten.size(); i++) {
				idQualquerItemIsencao = listIten.get(0).getId();
			}
			ItemPedidoIsencaoDisciplina requerimento = service
					.findItemIsencaoById(idQualquerItemIsencao);
			Comprovante comprovante = requerimento.getComprovante();
			GerenteArquivos.downloadFileNovo(idQualquerItemIsencao, request,
					response, comprovante);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}