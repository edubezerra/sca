package br.cefetrj.sca.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import br.cefetrj.sca.dominio.Aluno;
import br.cefetrj.sca.dominio.Departamento;
import br.cefetrj.sca.dominio.EnumStatusSolicitacao;
import br.cefetrj.sca.dominio.PeriodoAvaliacoesTurmas;
import br.cefetrj.sca.dominio.PeriodoLetivo;
import br.cefetrj.sca.dominio.PeriodoLetivo.EnumPeriodo;
import br.cefetrj.sca.dominio.Turma;
import br.cefetrj.sca.dominio.inclusaodisciplina.Comprovante;
import br.cefetrj.sca.dominio.inclusaodisciplina.ItemSolicitacaoMatriculaForaPrazo;
import br.cefetrj.sca.dominio.inclusaodisciplina.SolicitacaoMatriculaForaPrazo;
import br.cefetrj.sca.dominio.repositorio.AlunoRepositorio;
import br.cefetrj.sca.dominio.repositorio.DepartamentoRepositorio;
import br.cefetrj.sca.dominio.repositorio.InclusaoDisciplinaRepositorio;
import br.cefetrj.sca.dominio.repositorio.TurmaRepositorio;
import br.cefetrj.sca.service.util.SolicitaInclusaoDisciplinaResponse;

@Component
public class SolicitacaoMatriculaForaPrazoService {

	private static final int TAMANHO_MAXIMO_COMPROVANTE = 10000000;

	@Autowired
	private AlunoRepositorio alunoRepo;

	@Autowired
	private DepartamentoRepositorio departamentoRepo;

	@Autowired
	private TurmaRepositorio turmaRepositorio;

	@Autowired
	private InclusaoDisciplinaRepositorio inclusaoRepo;

	public SolicitaInclusaoDisciplinaResponse iniciarSolicitacoes() {
		List<Departamento> departamentos = departamentoRepo.getTodos();
		SolicitaInclusaoDisciplinaResponse response = new SolicitaInclusaoDisciplinaResponse();
		for (Departamento depto : departamentos) {
			response.add(response.new Item(depto.getNome(), depto.getId().toString()));
		}
		return response;
	}

	public Aluno getAlunoByCpf(String cpf) {
		return alunoRepo.getByCPF(cpf);
	}

	public Turma getTurmaPorCodigo(String codigoTurma) {
		if (codigoTurma == null || codigoTurma.trim().equals("")) {
			throw new IllegalArgumentException("Turma " + codigoTurma + " inválido");
		}

		Turma turma;

		try {
			turma = turmaRepositorio.getByCodigo(codigoTurma);
		} catch (Exception exc) {
			turma = null;
		}

		return turma;
	}

	public Departamento getDepartamentoById(String idDepartamento) {
		return departamentoRepo.getById(Long.valueOf(idDepartamento));
	}

	public SolicitacaoMatriculaForaPrazo getSolicitacaoByAlunoSemestre(Long alunoId, int ano, EnumPeriodo periodo) {
		return inclusaoRepo.getSolicitacaoByAlunoSemestre(alunoId, ano, periodo);
	}

	public List<SolicitacaoMatriculaForaPrazo> getSolicitacoesAluno(Long idAluno) {
		return inclusaoRepo.getSolicitacoesAluno(idAluno);
	}

	public void incluiSolicitacao(List<ItemSolicitacaoMatriculaForaPrazo> listaItemSolicitacao, Aluno aluno,
			PeriodoLetivo semestreLetivo) {

		SolicitacaoMatriculaForaPrazo solicitacao = getSolicitacaoByAlunoSemestre(aluno.getId(),
				semestreLetivo.getAno(), semestreLetivo.getPeriodo());
		if (solicitacao != null) {
			solicitacao.addItemSolicitacao(listaItemSolicitacao);
		} else {
			solicitacao = new SolicitacaoMatriculaForaPrazo(listaItemSolicitacao, aluno, semestreLetivo);
		}

		inclusaoRepo.adicionarSolicitacaoInclusao(solicitacao);
	}

	public SolicitacaoMatriculaForaPrazo getSolicitacaoAtual(String cpf) {
		Aluno aluno = getAlunoByCpf(cpf);
		Long idAluno = aluno.getId();
		PeriodoAvaliacoesTurmas periodoAvaliacao = PeriodoAvaliacoesTurmas.getInstance();
		PeriodoLetivo semestreLetivo = periodoAvaliacao.getSemestreLetivo();
		SolicitacaoMatriculaForaPrazo solicitacaoAtual = getSolicitacaoByAlunoSemestre(idAluno, semestreLetivo.getAno(),
				semestreLetivo.getPeriodo());
		return solicitacaoAtual;
	}

	public void solicitaInclusao(String cpf, int numeroSolicitacoes, Model model) {

		Aluno aluno = getAlunoByCpf(cpf);
		PeriodoAvaliacoesTurmas periodoAvaliacao = PeriodoAvaliacoesTurmas.getInstance();

		model.addAttribute("departamentos", iniciarSolicitacoes());
		model.addAttribute("cpf", cpf);
		model.addAttribute("aluno", aluno);
		model.addAttribute("periodoLetivo", periodoAvaliacao.getSemestreLetivo());
		model.addAttribute("numeroSolicitacoes", numeroSolicitacoes);
	}

	public void validaturma(HttpServletRequest request, Aluno aluno, PeriodoLetivo semestreLetivo, Departamento depto,
			Comprovante comprovante, int opcao, String observacao) {
		int i = 0;
		Map<String, String[]> parameters = request.getParameterMap();
		List<ItemSolicitacaoMatriculaForaPrazo> listaItemSolicitacao = new ArrayList<>();
		// parameters must contain only sorted quesitoX parameters
		while (parameters.containsKey("codigoTurma" + i)) {
			Turma turma = getTurmaPorCodigo(parameters.get("codigoTurma" + i)[0]);
			if (turma == null) {
				throw new IllegalArgumentException("Turma " + parameters.get("codigoTurma" + i)[0] + " inexistente");
			}

			boolean isSolicitacaoRepetida = isSolicitacaoRepetida(aluno.getId(), turma.getCodigo(), semestreLetivo);

			if (isSolicitacaoRepetida) {
				throw new IllegalArgumentException(
						"Erro: Já foi feita uma solicitação para a turma " + turma.getCodigo());
			}

			Date dataSolicitacao = new Date();
			listaItemSolicitacao.add(new ItemSolicitacaoMatriculaForaPrazo(dataSolicitacao, turma, depto, comprovante,
					EnumStatusSolicitacao.AGUARDANDO, opcao, observacao));
			++i;
		}

		incluiSolicitacao(listaItemSolicitacao, aluno, semestreLetivo);

	}

	public void validaSolicitacao(HttpServletRequest request, String cpf, MultipartFile file, String departamento,
			int opcao, String observacao) throws IOException {

		Departamento depto = getDepartamentoById(departamento);
		PeriodoAvaliacoesTurmas periodoAvaliacao = PeriodoAvaliacoesTurmas.getInstance();
		PeriodoLetivo semestreLetivo = periodoAvaliacao.getSemestreLetivo();
		Aluno aluno = getAlunoByCpf(cpf);

		validaComprovante(file);
		Comprovante comprovante = new Comprovante(file.getContentType(), file.getBytes(), file.getOriginalFilename());

		validaturma(request, aluno, semestreLetivo, depto, comprovante, opcao, observacao);
	}

	public void validaComprovante(MultipartFile file) {
		if (file.getSize() > TAMANHO_MAXIMO_COMPROVANTE) {
			throw new IllegalArgumentException("O arquivo de comprovante deve ter 10mb no máximo");
		}
		String[] tiposAceitos = { "application/pdf", "image/jpeg", "image/png" };
		if (ArrayUtils.indexOf(tiposAceitos, file.getContentType()) < 0) {
			throw new IllegalArgumentException("O arquivo de comprovante deve ser no formato PDF, JPEG ou PNG");
		}
	}

	public boolean isSolicitacaoRepetida(Long idAluno, String codigoTurma, PeriodoLetivo semestreLetivo) {
		Turma turma = inclusaoRepo.getTurmaSolicitada(idAluno, codigoTurma, semestreLetivo.getAno(),
				semestreLetivo.getPeriodo());

		if (turma == null) {
			return false;
		} else {
			return true;
		}
	}
	
	public void carregaHomeView(Model model, String cpf) {
		SolicitacaoMatriculaForaPrazo solicitacaoAtual = getSolicitacaoAtual(cpf);
		if (solicitacaoAtual != null) {
			List<SolicitacaoMatriculaForaPrazo> solicitacoes = getSolicitacoesAluno(solicitacaoAtual.getAluno().getId());
			List<PeriodoLetivo> listaSemestresLetivos = SolicitacaoMatriculaForaPrazo
					.semestresCorrespondentes(solicitacoes);
			model.addAttribute("listaSemestresLetivos", listaSemestresLetivos);
		}

		model.addAttribute("aluno", getAlunoByCpf(cpf));

		if (solicitacaoAtual != null) {
			model.addAttribute("numeroSolicitacoes", solicitacaoAtual.getItensSolicitacao().size());
		} else {
			model.addAttribute("numeroSolicitacoes", 0);
		}
	}

	public Comprovante getComprovante(Long solicitacaoId) {
		return inclusaoRepo.getComprovante(solicitacaoId);
	}
}
