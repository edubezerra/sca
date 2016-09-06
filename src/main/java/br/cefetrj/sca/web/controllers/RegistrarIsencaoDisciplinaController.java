package br.cefetrj.sca.web.controllers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;

import br.cefetrj.sca.dominio.Aluno;
import br.cefetrj.sca.dominio.Disciplina;
import br.cefetrj.sca.dominio.ItemIsencao;
import br.cefetrj.sca.dominio.ProcessoIsencaoDisciplinas;
import br.cefetrj.sca.dominio.Professor;
import br.cefetrj.sca.dominio.matriculaforaprazo.Comprovante;
import br.cefetrj.sca.dominio.repositories.ItemIsencaoRepositorio;
import br.cefetrj.sca.dominio.repositories.ProcessoIsencaoRepositorio;
import br.cefetrj.sca.dominio.usuarios.Usuario;
import br.cefetrj.sca.service.IsencaoDisciplinaService;

@Controller
@SessionAttributes("login")
@RequestMapping("/isencaoDisciplina")
public class RegistrarIsencaoDisciplinaController {

	@Autowired
	IsencaoDisciplinaService is;

	@Autowired
	ItemIsencaoRepositorio itemIsencaoRepo;

	@Autowired
	ProcessoIsencaoRepositorio processoIsencaoRepo;

	@RequestMapping(value = "/visualizarProcessoIsencao", method = RequestMethod.GET)
	public String visualizarProcessoIsencao(Model model,
			HttpServletRequest request, HttpSession session) {

		String matricula = UsuarioController.getCurrentUser().getLogin();
		try {
			Aluno aluno = is.findAlunoByMatricula(matricula);
			List<ItemIsencao> it = new ArrayList<>();
			int contadorItemIsencao = 0;
			String situacaoNome = null;

			ProcessoIsencaoDisciplinas pi = processoIsencaoRepositorio.findByMatriculaAluno(aluno);
			
			if (pi != null) {
				for (int i = 0; i < pi
						.getListaItenIsencao().size(); i++) {
					it.add(aluno.getProcessoIsencao().getListaItenIsencao()
							.get(i));
					if (aluno.getProcessoIsencao().getListaItenIsencao().get(i)
							.getSituacao() != null) {
						contadorItemIsencao = contadorItemIsencao + 1;
						if (aluno.getProcessoIsencao().getListaItenIsencao()
								.size() == contadorItemIsencao) {
							aluno.getProcessoIsencao()
									.setSituacaoProcessoIsencao("ANALISADO");
						} else {
							aluno.getProcessoIsencao()
									.setSituacaoProcessoIsencao("EM ANALISE");
						}
					}
				}

				model.addAttribute("itemIsencaoByProcessoIsencao", it);
				model.addAttribute("situacaoNome", situacaoNome);
			}
			model.addAttribute("aluno", aluno);

			return "/isencaoDisciplina/aluno/alunoSucesso";
		} catch (Exception exc) {
			model.addAttribute("error", exc.getMessage());
			return "/menuPrincipalView";
		}
	}

	@RequestMapping(value = "/alunoView", method = RequestMethod.GET)
	public String isencaoDisciplina(Model model, HttpServletRequest request,
			HttpSession session) {

		String matricula = UsuarioController.getCurrentUser().getLogin();
		session.setAttribute("login", matricula);
		try {

			Aluno aluno = is.findAlunoByMatricula(matricula);

			String siglaCurso = aluno.getVersaoCurso().getCurso().getSigla();

			List<Disciplina> disciplinas = is.findDisciplinas(siglaCurso);
			model.addAttribute("aluno", aluno);
			model.addAttribute("disciplinas", disciplinas);

			return "/isencaoDisciplina/aluno/alunoView";

		} catch (Exception exc) {
			model.addAttribute("error", exc.getMessage());
			return "/menuPrincipalView";
		}
	}

	@RequestMapping(value = "/validaComprovante", method = RequestMethod.POST)
	public String validaComprovante(Model model, HttpServletRequest request,
			HttpSession session, @RequestParam("file") MultipartFile file) {
		try {
			String matricula = (String) session.getAttribute("login");
			Aluno aluno = is.findAlunoByMatricula(matricula);

			String auxcheckboxes[] = request.getParameterValues("choice");
			Long checkBoxes[] = new Long[auxcheckboxes.length];

			for (int i = 0; i < auxcheckboxes.length; i++) {
				checkBoxes[i] = Long.parseLong(auxcheckboxes[i]);
			}
			ProcessoIsencaoDisciplinas pi = null;
			ItemIsencao itemIsencao = null;

			if (aluno.getProcessoIsencao() != null) {
				aluno.getProcessoIsencao()
						.getListaItenIsencao()
						.removeAll(
								aluno.getProcessoIsencao()
										.getListaItenIsencao());
				pi = aluno.getProcessoIsencao();

				for (int i = 0; i < aluno.getProcessoIsencao()
						.getListaItenIsencao().size(); i++) {
					aluno.getProcessoIsencao()
							.getListaItenIsencao()
							.removeAll(
									aluno.getProcessoIsencao()
											.getListaItenIsencao());
				}
				for (int i = 0; i < checkBoxes.length; i++) {
					itemIsencao = new ItemIsencao();

					itemIsencao.setDisciplina(is
							.getDisciplinaPorId(checkBoxes[i]));

					aluno.getProcessoIsencao().getListaItenIsencao()
							.add(itemIsencao);

					itemIsencaoRepo.save(itemIsencao);
				}

				processoIsencaoRepo.save(pi);

			} else {
				List<ItemIsencao> listaIsen = new ArrayList<>();

				pi = new ProcessoIsencaoDisciplinas();

				for (int i = 0; i < checkBoxes.length; i++) {
					itemIsencao = new ItemIsencao();

					itemIsencao.setDisciplina(is
							.getDisciplinaPorId(checkBoxes[i]));
					listaIsen.add(itemIsencao);

					itemIsencao.setComprovante(file.getContentType(),
							file.getBytes(), file.getOriginalFilename());

					itemIsencaoRepo.save(itemIsencao);

				}
				pi.setListaItenIsencao(listaIsen);

				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				Date data = new Date();
				Date dataAtual = sdf.parse(sdf.format(data));

				pi.setDataRegistro(dataAtual);
				aluno.setProcessoIsencao(pi);

				processoIsencaoRepo.save(pi);
			}

			model.addAttribute("aluno", aluno);
			model.addAttribute("itemIsencaoByProcessoIsencao", aluno
					.getProcessoIsencao().getListaItenIsencao());

			return "/isencaoDisciplina/aluno/relacaoMateriaExterna";
		} catch (Exception exc) {
			model.addAttribute("error", exc.getMessage());
			return "/menuPrincipalView";
		}
	}

	@RequestMapping(value = "/relacaoMateriaExterna", method = RequestMethod.POST)
	public String registroMateriaExterna(
			Model model,
			HttpServletRequest request,
			HttpSession session,
			@RequestParam("aluno") String matricula,
			@RequestParam("itemIsencaoByProcessoIsencao") List<String> itemIsencao,
			@RequestParam("disciplinaAssociada") List<String> disciplinaAssociada) {

		try {

			Aluno aluno = is.findAlunoByMatricula(matricula);

			for (int i = 0; i < itemIsencao.size(); i++) {
				aluno.getProcessoIsencao().getListaItenIsencao().get(i)
						.setDisciplinaAssociada(disciplinaAssociada.get(i));

				itemIsencaoRepo.save(aluno.getProcessoIsencao()
						.getListaItenIsencao().get(i));
			}

			return visualizarProcessoIsencao(model, request, session);

		} catch (Exception exc) {
			model.addAttribute("error", exc.getMessage());
			return "/menuPrincipalView";
		}
	}

	// //////////////////////////////////// PROFESSOR
	// ///////////////////////////////////// ////////////////////////////

	@RequestMapping(value = "/professorView", method = RequestMethod.GET)
	public String isencaoDisciplinaProfessor(Model model,
			HttpServletRequest request, HttpSession session) {

		Usuario usr = UsuarioController.getCurrentUser();
		String matricula = usr.getMatricula();
		try {

			Professor professor = is.findProfessorByMatricula(matricula);
			if (professor == null) {
				model.addAttribute("error", "Professor não encontrado!");
				return "forward:/isencaoDisciplina/menuPrincipal";
			}

			List<Aluno> alunos = is.getTodosOsAlunos();
			List<Aluno> alunosProcessoIsencao = new ArrayList<>();

			for (int i = 0; i < alunos.size(); i++) {
				if (alunos.get(i).getProcessoIsencao() != null) {
					alunosProcessoIsencao.add(alunos.get(i));
				}
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
	public String itensProcessoIsencaoProfessor(Model model,
			HttpServletRequest request, HttpSession session,
			@RequestParam("matricula") List<String> matriculas) {

		String matricula = UsuarioController.getCurrentUser().getLogin();
		session.setAttribute("login", matricula);

		try {
			Professor professor = is.findProfessorByMatricula(matricula);
			Aluno aluno = null;

			List<Aluno> alunoIsencao = is.getTodosOsAlunos();
			List<ItemIsencao> alunosItemIsencao = new ArrayList<>();

			for (int j = 0; j < matriculas.size(); j++) {

				for (int i = 0; i < alunoIsencao.size(); i++) {
					if (alunoIsencao.get(i).getMatricula()
							.equals(matriculas.get(j))) {
						alunosItemIsencao.addAll(alunoIsencao.get(i)
								.getProcessoIsencao().getListaItenIsencao());
						aluno = is.findAlunoByMatricula(alunoIsencao.get(i)
								.getMatricula());
					}
				}
			}

			model.addAttribute("professor", professor);
			model.addAttribute("aluno", aluno);
			model.addAttribute("alunosItemIsencao", alunosItemIsencao);

			return "/isencaoDisciplina/professor/itemProcessoIsencaoView";

		} catch (Exception exc) {
			model.addAttribute("error", exc.getMessage());
			return "/menuPrincipalView";
		}
	}

	@RequestMapping(value = { "/", "/professorSucesso" }, method = RequestMethod.POST)
	public String confirmacaoProcessoIsencao(Model model,
			HttpServletRequest request, HttpSession session,
			@RequestParam("btAvaliador") String btAvaliador,
			@RequestParam("alunosItemIsencao") List<String> item,
			@RequestParam("aluno") String matriculaAluno) {

		try {
			Aluno aluno = is.findAlunoByMatricula(matriculaAluno);

			for (int i = 0; i < item.size(); i++) {

				int itemNumero = item.get(i).indexOf("-") + 1;
				String itemTraco = item.get(i).substring(itemNumero);

				String botaoTraco = null;

				int botaoNumero = btAvaliador.indexOf("-") + 1;
				botaoTraco = btAvaliador.substring(botaoNumero);

				if (botaoTraco.equals(itemTraco)) {
					if (btAvaliador.contains("indeferir")) {
						String valorSalvo = btAvaliador.substring(0,
								btAvaliador.indexOf("-"));

						model.addAttribute("codigoDisciplina", item.get(i));
						model.addAttribute("alunosItemIsencao", item.get(i));
						model.addAttribute("aluno", aluno);
						model.addAttribute("situacao", valorSalvo);

						return "/isencaoDisciplina/professor/motivoProcessoIsencaoView";

					} else {
						String valorSalvo = btAvaliador.substring(0,
								btAvaliador.indexOf("-"));

						aluno.getProcessoIsencao().getListaItenIsencao().get(i)
								.setSituacao(valorSalvo);
						Date date = new Date();
						aluno.getProcessoIsencao().getListaItenIsencao().get(i)
								.setDataAnalise(date);

						itemIsencaoRepo.save(aluno.getProcessoIsencao()
								.getListaItenIsencao().get(i));

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
	public String motivoProcessoIsencaoProfessor(Model model,
			HttpServletRequest request, HttpSession session,
			@RequestParam("motivos") String motivos,
			@RequestParam("codigoDisciplina") String codigoDisciplina,
			@RequestParam("situacao") String situacao,
			@RequestParam("observacao") String observacao,
			@RequestParam("aluno") String matriculaAluno) {

		try {
			Aluno aluno = is.findAlunoByMatricula(matriculaAluno);
			List<ItemIsencao> lista = aluno.getProcessoIsencao()
					.getListaItenIsencao();

			String valor = codigoDisciplina.substring(0,
					codigoDisciplina.indexOf("-"));

			for (int i = 0; i < lista.size(); i++) {
				if (lista.get(i).getDisciplina().getCodigo().equals(valor)) {
					aluno.getProcessoIsencao().getListaItenIsencao().get(i)
							.setMotivo(motivos);
					aluno.getProcessoIsencao().getListaItenIsencao().get(i)
							.setSituacao(situacao);
					if (motivos.equals("Outros")) {
						aluno.getProcessoIsencao().getListaItenIsencao().get(i)
								.setObservacao(observacao);
					}

					Date date = new Date();
					aluno.getProcessoIsencao().getListaItenIsencao().get(i)
							.setDataAnalise(date);

					itemIsencaoRepo.save(aluno.getProcessoIsencao()
							.getListaItenIsencao().get(i));
				}
			}

			return "/menuPrincipalView";

		} catch (Exception exc) {
			model.addAttribute("error", exc.getMessage());
			return "/menuPrincipalView";
		}
	}

	@RequestMapping(value = "/downloadFile", method = RequestMethod.POST)
	public void downloadFile(
			@RequestParam("comprovanteProcIsencao") long idProcIsencao,
			HttpServletRequest request, HttpServletResponse response,
			HttpSession sessao) {

		try {
			List<ItemIsencao> listIten = processoIsencaoRepo
					.findItemIsencaoByProcessoIsencao(idProcIsencao);
			long idQualquerItemIsencao = 0;
			for (int i = 0; i < listIten.size(); i++) {
				idQualquerItemIsencao = listIten.get(0).getId();
			}
			ItemIsencao requerimento = is
					.findItemIsencaoById(idQualquerItemIsencao);
			Comprovante comprovante = requerimento.getComprovante();
			GerenteArquivos.downloadFileNovo(idQualquerItemIsencao, request,
					response, comprovante);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
