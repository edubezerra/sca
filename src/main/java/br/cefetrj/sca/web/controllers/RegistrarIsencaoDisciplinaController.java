package br.cefetrj.sca.web.controllers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;

import br.cefetrj.sca.dominio.Aluno;
import br.cefetrj.sca.dominio.Departamento;
import br.cefetrj.sca.dominio.Disciplina;
import br.cefetrj.sca.dominio.Professor;
import br.cefetrj.sca.dominio.isencoes.ItemIsencaoDisciplina;
import br.cefetrj.sca.dominio.isencoes.ProcessoIsencaoDisciplinas;
import br.cefetrj.sca.dominio.matriculaforaprazo.Comprovante;
import br.cefetrj.sca.dominio.repositories.AlunoRepositorio;
import br.cefetrj.sca.dominio.repositories.DepartamentoRepositorio;
import br.cefetrj.sca.dominio.repositories.ProcessoIsencaoRepositorio;
import br.cefetrj.sca.dominio.usuarios.Usuario;
import br.cefetrj.sca.service.IsencaoDisciplinaService;

@Controller
@SessionAttributes("login")
@RequestMapping("/isencaoDisciplina")
public class RegistrarIsencaoDisciplinaController {

	@Autowired
	IsencaoDisciplinaService service;

	@Autowired
	ProcessoIsencaoRepositorio processoIsencaoRepo;

	@Autowired
	DepartamentoRepositorio departamentoRepositorio;

	@Autowired
	AlunoRepositorio alunoRepositorio;

	@RequestMapping(value = "/visualizarProcessoIsencao", method = RequestMethod.GET)
	public String visualizarProcessoIsencao(Model model, HttpServletRequest request) {

		System.out.println("RegistrarIsencaoDisciplinaController.visualizarProcessoIsencao()");

		try {
			String matricula = UsuarioController.getCurrentUser().getLogin();
			Aluno aluno = service.findAlunoByMatricula(matricula);
			ProcessoIsencaoDisciplinas processo = processoIsencaoRepo.findByAluno(aluno);

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

		System.out.println("RegistrarIsencaoDisciplinaController.isencaoDisciplina()");

		String matricula = UsuarioController.getCurrentUser().getLogin();

		try {

			Aluno aluno = service.findAlunoByMatricula(matricula);

			String siglaCurso = aluno.getVersaoCurso().getCurso().getSigla();

			List<Disciplina> disciplinas = service.findDisciplinas(siglaCurso);
			model.addAttribute("aluno", aluno);
			model.addAttribute("disciplinas", disciplinas);

			return "/isencaoDisciplina/aluno/escolheDisciplinasParaIsentar";

		} catch (Exception exc) {
			model.addAttribute("error", exc.getMessage());
			return "/menuPrincipalView";
		}
	}

	@RequestMapping(value = "/validaComprovante", method = RequestMethod.POST)
	public String validaComprovante(Model model, HttpServletRequest request, @RequestParam("file") MultipartFile file) {

		System.out.println("RegistrarIsencaoDisciplinaController.validaComprovante()");

		try {
			String matricula = UsuarioController.getCurrentUser().getLogin();
			Aluno aluno = service.findAlunoByMatricula(matricula);

			String auxcheckboxes[] = request.getParameterValues("choice");
			Long checkBoxes[] = new Long[auxcheckboxes.length];

			for (int i = 0; i < auxcheckboxes.length; i++) {
				checkBoxes[i] = Long.parseLong(auxcheckboxes[i]);
			}
			ProcessoIsencaoDisciplinas processo = processoIsencaoRepo.findByAluno(aluno);

			if (processo != null) {
				processo.getItens().removeAll(processo.getItens());

				for (int i = 0; i < processo.getItens().size(); i++) {
					processo.getItens().removeAll(processo.getItens());
				}
				for (int i = 0; i < checkBoxes.length; i++) {
					processo.comMaisUmItem(service.getDisciplinaPorId(checkBoxes[i]));
				}
				processoIsencaoRepo.save(processo);
			} else {
				processo = new ProcessoIsencaoDisciplinas(aluno);
				for (int i = 0; i < checkBoxes.length; i++) {
					processo.comMaisUmItem(service.getDisciplinaPorId(checkBoxes[i]));
					// item.setComprovante(file.getContentType(),
					// file.getBytes(), file.getOriginalFilename());
				}
				processoIsencaoRepo.save(processo);
			}

			model.addAttribute("aluno", aluno);
			model.addAttribute("itemIsencaoByProcessoIsencao", processo.getItens());

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
	@RequestMapping(value = "/relacaoMateriaExterna", method = RequestMethod.POST)
	public String registroMateriaExterna(Model model, HttpServletRequest request,
			@RequestParam("aluno") String matricula,
			@RequestParam("itemIsencaoByProcessoIsencao") List<String> itemIsencao,
			@RequestParam("disciplinaAssociada") List<String> disciplinaAssociada) {

		System.out.println("RegistrarIsencaoDisciplinaController.registroMateriaExterna()");

		try {
			Aluno aluno = service.findAlunoByMatricula(matricula);
			ProcessoIsencaoDisciplinas processo = processoIsencaoRepo.findByAluno(aluno);

			for (int i = 0; i < itemIsencao.size(); i++) {
				processo.getItens().get(i).setDisciplinaAssociada(disciplinaAssociada.get(i));
			}

			processoIsencaoRepo.save(processo);

			return visualizarProcessoIsencao(model, request);

		} catch (Exception exc) {
			model.addAttribute("error", exc.getMessage());
			return "/menuPrincipalView";
		}
	}

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

	@RequestMapping(value = "/professorView", method = RequestMethod.GET)
	public String isencaoDisciplinaProfessor(Model model, HttpServletRequest request) {

		System.out.println("RegistrarIsencaoDisciplinaController.isencaoDisciplinaProfessor()");

		Usuario usr = UsuarioController.getCurrentUser();
		String matricula = usr.getMatricula();
		try {

			Professor professor = service.findProfessorByMatricula(matricula);
			if (professor == null) {
				model.addAttribute("error", "Professor não encontrado!");
				return "forward:/isencaoDisciplina/menuPrincipal";
			}

			Departamento departamento = departamentoRepositorio.findDepartamentoByProfessor(professor.getMatricula());

			Set<ProcessoIsencaoDisciplinas> processos = processoIsencaoRepo.findByDepartamento(departamento);

			List<Aluno> alunosProcessoIsencao = new ArrayList<>();

			Iterator<ProcessoIsencaoDisciplinas> itr = processos.iterator();
			while (itr.hasNext()) {
				ProcessoIsencaoDisciplinas processo = itr.next();
				alunosProcessoIsencao.add(processo.getAluno());
			}

			model.addAttribute("professor", professor);
			model.addAttribute("alunosProcessoIsencao", alunosProcessoIsencao);

			return "/isencaoDisciplina/professor/professorView";

		} catch (Exception exc) {
			model.addAttribute("error", exc.getMessage());
			return "/menuPrincipalView";
		}
	}

	@RequestMapping(value = { "/", "/itemProcessoIsencaoView" }, method = RequestMethod.POST)
	public String itensProcessoIsencaoProfessor(Model model, HttpServletRequest request,
			@RequestParam("matricula") List<String> matriculas) {

		System.out.println("RegistrarIsencaoDisciplinaController.itensProcessoIsencaoProfessor()");

		String matricula = UsuarioController.getCurrentUser().getLogin();

		try {
			Professor professor = service.findProfessorByMatricula(matricula);

			Aluno aluno = alunoRepositorio.findAlunoByMatricula(matriculas.get(0));

			List<ItemIsencaoDisciplina> itensIsencao = new ArrayList<>();

			ProcessoIsencaoDisciplinas processo = processoIsencaoRepo.findByAluno(aluno);
			itensIsencao.addAll(processo.getItens());

			model.addAttribute("professor", professor);
			model.addAttribute("aluno", aluno);
			model.addAttribute("itensIsencaoAluno", itensIsencao);

			return "/isencaoDisciplina/professor/itemProcessoIsencaoView";

		} catch (Exception exc) {
			model.addAttribute("error", exc.getMessage());
			return "/menuPrincipalView";
		}
	}

	@RequestMapping(value = { "/", "/professorSucesso" }, method = RequestMethod.POST)
	public String confirmacaoProcessoIsencao(Model model, HttpServletRequest request,
			@RequestParam("btAvaliador") String btAvaliador,
			@RequestParam("alunosItemIsencao") List<String> itensIsencao,
			@RequestParam("aluno") String matriculaAluno) {

		System.out.println("RegistrarIsencaoDisciplinaController.confirmacaoProcessoIsencao()");

		try {
			Aluno aluno = service.findAlunoByMatricula(matriculaAluno);
			ProcessoIsencaoDisciplinas processo = processoIsencaoRepo.findByAluno(aluno);

			for (int i = 0; i < itensIsencao.size(); i++) {

				int itemNumero = itensIsencao.get(i).indexOf("-") + 1;
				String itemTraco = itensIsencao.get(i).substring(itemNumero);

				String botaoTraco = null;

				int botaoNumero = btAvaliador.indexOf("-") + 1;
				botaoTraco = btAvaliador.substring(botaoNumero);

				if (botaoTraco.equals(itemTraco)) {
					if (btAvaliador.contains("indeferir")) {
						String valorSalvo = btAvaliador.substring(0, btAvaliador.indexOf("-"));

						model.addAttribute("codigoDisciplina", itensIsencao.get(i));
						model.addAttribute("alunosItemIsencao", itensIsencao.get(i));
						model.addAttribute("aluno", aluno);
						model.addAttribute("situacao", valorSalvo);

						return "/isencaoDisciplina/professor/motivoProcessoIsencaoView";

					} else {
						String valorSalvo = btAvaliador.substring(0, btAvaliador.indexOf("-"));

						processo.getItens().get(i).analisar(valorSalvo, null);

						processoIsencaoRepo.save(processo);

						return "/menuPrincipalView";
					}
				}

			}
		} catch (Exception e) {
			return "/menuPrincipalView";
		}

		return null;
	}

	@RequestMapping(value = { "/", "/motivoProcessoIsencao" }, method = RequestMethod.POST)
	public String motivoProcessoIsencaoProfessor(Model model, HttpServletRequest request,
			@RequestParam("motivos") String motivos, @RequestParam("codigoDisciplina") String codigoDisciplina,
			@RequestParam("situacao") String situacao, @RequestParam("observacao") String observacao,
			@RequestParam("aluno") String matriculaAluno) {

		System.out.println("RegistrarIsencaoDisciplinaController.motivoProcessoIsencaoProfessor()");

		try {
			Aluno aluno = service.findAlunoByMatricula(matriculaAluno);
			ProcessoIsencaoDisciplinas processo = processoIsencaoRepo.findByAluno(aluno);

			List<ItemIsencaoDisciplina> lista = processo.getItens();

			String valor = codigoDisciplina.substring(0, codigoDisciplina.indexOf("-"));

			for (int i = 0; i < lista.size(); i++) {
				if (lista.get(i).getDisciplina().getCodigo().equals(valor)) {
					processo.getItens().get(i).setMotivo(motivos);
					processo.getItens().get(i).analisar(situacao, observacao);
				}
			}

			processoIsencaoRepo.save(processo);

			return "/menuPrincipalView";

		} catch (Exception exc) {
			model.addAttribute("error", exc.getMessage());
			return "/menuPrincipalView";
		}
	}

	@RequestMapping(value = "/downloadFile", method = RequestMethod.POST)
	public void downloadFile(@RequestParam("comprovanteProcIsencao") long idProcIsencao, HttpServletRequest request,
			HttpServletResponse response) {

		System.out.println("RegistrarIsencaoDisciplinaController.downloadFile()");

		try {
			List<ItemIsencaoDisciplina> listIten = processoIsencaoRepo.findItemIsencaoByProcessoIsencao(idProcIsencao);
			long idQualquerItemIsencao = 0;
			for (int i = 0; i < listIten.size(); i++) {
				idQualquerItemIsencao = listIten.get(0).getId();
			}
			ItemIsencaoDisciplina requerimento = service.findItemIsencaoById(idQualquerItemIsencao);
			Comprovante comprovante = requerimento.getComprovante();
			GerenteArquivos.downloadFileNovo(idQualquerItemIsencao, request, response, comprovante);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}