package br.cefetrj.sca.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import br.cefetrj.sca.dominio.Aluno;
import br.cefetrj.sca.dominio.AlunoRepositorio;
import br.cefetrj.sca.dominio.Departamento;
import br.cefetrj.sca.dominio.Disciplina;
import br.cefetrj.sca.dominio.inclusaodisciplina.Comprovante;
import br.cefetrj.sca.dominio.isencoes.ItemSolicitacaoIsencaoDisciplinas;
import br.cefetrj.sca.dominio.isencoes.SolicitacaoIsencaoDisciplinas;
import br.cefetrj.sca.dominio.isencoes.SolicitacaoIsencaoDisciplinasRepositorio;
import br.cefetrj.sca.dominio.repositorio.DepartamentoRepositorio;
import br.cefetrj.sca.dominio.repositorio.DisciplinaRepositorio;
import br.cefetrj.sca.service.util.SolicitaInclusaoDisciplinaResponse;

@Component
public class SolicitacaoIsencaoDisciplinasService {

	private static final int TAMANHO_MAXIMO_COMPROVANTE = 10000000;

	@Autowired
	private AlunoRepositorio alunoRepo;

	@Autowired
	private DepartamentoRepositorio departamentoRepo;

	@Autowired
	private DisciplinaRepositorio disciplinaRepositorio;

	@Autowired
	private SolicitacaoIsencaoDisciplinasRepositorio isencaoRepo;

	public SolicitaInclusaoDisciplinaResponse iniciarSolicitacoes() {
		List<Departamento> departamentos = departamentoRepo.getTodos();
		SolicitaInclusaoDisciplinaResponse response = new SolicitaInclusaoDisciplinaResponse();
		for (Departamento depto : departamentos) {
			response.add(response.new Item(depto.getNome(), depto.getId()
					.toString()));
		}
		return response;
	}

	public Aluno getAlunoByCpf(String cpf) {
		return alunoRepo.getAlunoPorCPF(cpf);
	}

	public Disciplina getDisciplinaPorCodigo(String codigoDisciplina,
			String idAluno) {
		if (codigoDisciplina == null || codigoDisciplina.trim().equals("")) {
			throw new IllegalArgumentException("Disciplina: código "
					+ codigoDisciplina + " inválido");
		}

		Disciplina disciplina;

		Aluno aluno = alunoRepo.getAlunoPorId(idAluno);

		try {
			String versaoCurso = aluno.getVersaoCurso().getNumero();
			String siglaCurso = aluno.getVersaoCurso().getCurso().getSigla();
			disciplina = disciplinaRepositorio.getByCodigo(codigoDisciplina,
					siglaCurso, versaoCurso);
		} catch (Exception exc) {
			disciplina = null;
		}

		return disciplina;
	}

	public Departamento getDepartamentoById(String idDepartamento) {
		return departamentoRepo.getById(Long.valueOf(idDepartamento));
	}

	public SolicitacaoIsencaoDisciplinas getSolicitacaoByAluno(Long alunoId) {
		return isencaoRepo.getSolicitacaoByAluno(alunoId);
	}

	public void incluiSolicitacao(
			List<ItemSolicitacaoIsencaoDisciplinas> listaItemSolicitacao,
			Aluno aluno) {

		SolicitacaoIsencaoDisciplinas solicitacao = getSolicitacaoByAluno(aluno
				.getId());
		if (solicitacao != null) {
			solicitacao.adicionarItens(listaItemSolicitacao);
		} else {
			solicitacao = new SolicitacaoIsencaoDisciplinas(
					listaItemSolicitacao, aluno);
		}

		isencaoRepo.adicionarSolicitacaoInclusao(solicitacao);
	}

	public SolicitacaoIsencaoDisciplinas getSolicitacaoAtual(String cpf) {
		Aluno aluno = getAlunoByCpf(cpf);
		Long idAluno = aluno.getId();
		SolicitacaoIsencaoDisciplinas solicitacaoAtual = getSolicitacaoByAluno(idAluno);
		return solicitacaoAtual;
	}

	public void solicitaInclusao(String cpf, Model model) {
		Aluno aluno = getAlunoByCpf(cpf);
		model.addAttribute("departamentos", iniciarSolicitacoes());
		model.addAttribute("cpf", cpf);
		model.addAttribute("aluno", aluno);
	}

	public void validaDisciplina(HttpServletRequest request, Aluno aluno,
			Departamento depto, Comprovante comprovante, String observacao) {
		int i = 0;
		Map<String, String[]> parameters = request.getParameterMap();
		List<ItemSolicitacaoIsencaoDisciplinas> listaItemSolicitacao = new ArrayList<>();
		while (parameters.containsKey("codigoDisciplina" + i)) {

			Disciplina disciplina = getDisciplinaPorCodigo(
					parameters.get("codigoDisciplina" + i)[0], aluno.getId()
							.toString());
			if (disciplina == null) {
				throw new IllegalArgumentException("Disciplina "
						+ parameters.get("codigoDisciplina" + i)[0]
						+ " inexistente");
			}

			boolean isSolicitacaoRepetida = isSolicitacaoRepetida(
					aluno.getId(), disciplina.getCodigo());

			if (isSolicitacaoRepetida) {
				throw new IllegalArgumentException(
						"Erro: Já foi feita uma solicitação para a disciplina "
								+ disciplina.getCodigo());
			}

			listaItemSolicitacao.add(new ItemSolicitacaoIsencaoDisciplinas(
					disciplina));
			++i;
		}

		incluiSolicitacao(listaItemSolicitacao, aluno);
	}

	public void registrarSolicitacao(HttpServletRequest request, String cpf,
			MultipartFile file, String departamento, String observacao)
			throws IOException {

		Departamento depto = getDepartamentoById(departamento);

		Aluno aluno = getAlunoByCpf(cpf);

		validaComprovante(file);
		Comprovante comprovante = new Comprovante(file.getContentType(),
				file.getBytes(), file.getOriginalFilename());

		validaDisciplina(request, aluno, depto, comprovante, observacao);
	}

	public void validaComprovante(MultipartFile file) {
		if (file.getSize() > TAMANHO_MAXIMO_COMPROVANTE) {
			throw new IllegalArgumentException(
					"O arquivo de comprovante deve ter 10mb no máximo");
		}
		String[] tiposAceitos = { "application/pdf", "image/jpeg", "image/png" };
		if (ArrayUtils.indexOf(tiposAceitos, file.getContentType()) < 0) {
			throw new IllegalArgumentException(
					"O arquivo de comprovante deve ser no formato PDF, JPEG ou PNG");
		}
	}

	public boolean isSolicitacaoRepetida(Long idAluno, String codigoDisciplina) {
		Disciplina disciplina = isencaoRepo.getDisciplinaSolicitada(idAluno,
				codigoDisciplina);

		if (disciplina == null) {
			return false;
		} else {
			return true;
		}
	}

	public Comprovante getComprovante(Long solicitacaoId) {
		return isencaoRepo.getComprovante(solicitacaoId);
	}
}
