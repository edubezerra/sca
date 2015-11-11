package br.cefetrj.sca.service;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import br.cefetrj.sca.dominio.Aluno;
import br.cefetrj.sca.dominio.Departamento;
import br.cefetrj.sca.dominio.EnumStatusSolicitacao;
import br.cefetrj.sca.dominio.PeriodoAvaliacoesTurmas;
import br.cefetrj.sca.dominio.SemestreLetivo;
import br.cefetrj.sca.dominio.SemestreLetivo.EnumPeriodo;
import br.cefetrj.sca.dominio.Turma;
import br.cefetrj.sca.dominio.inclusaodisciplina.Comprovante;
import br.cefetrj.sca.dominio.inclusaodisciplina.ItemSolicitacao;
import br.cefetrj.sca.dominio.inclusaodisciplina.SolicitacaoInclusao;
import br.cefetrj.sca.dominio.repositorio.AlunoRepositorio;
import br.cefetrj.sca.dominio.repositorio.DepartamentoRepositorio;
import br.cefetrj.sca.dominio.repositorio.InclusaoDisciplinaRepositorio;
import br.cefetrj.sca.dominio.repositorio.TurmaRepositorio;
import br.cefetrj.sca.service.util.SolicitaInclusaoDisciplinaResponse;

@Component
public class InclusaoDisciplinaService {
	
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
		for (Departamento depto: departamentos) {
			response.add(response.new Item(depto.getNome(), depto.getId().toString()));
		}
		return response;
	}
	
	public Aluno getAlunoByCpf(String cpf) {
		return alunoRepo.getByCPF(cpf);
	}
	
	public Comprovante getComprovante(Long solicitacaoId){
		return inclusaoRepo.getComprovante(solicitacaoId);
	}
	
	public Turma getTurmaPorCodigo(String codigoTurma) {
		if (codigoTurma == null || codigoTurma.trim().equals("")) {
			throw new IllegalArgumentException("Turma "+codigoTurma+" inválido");
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
	
	public SolicitacaoInclusao getSolicitacaoByAlunoSemestre(Long alunoId, int ano, EnumPeriodo periodo) {
		return inclusaoRepo.getSolicitacaoByAlunoSemestre(alunoId, ano, periodo);
	}
	
	public List<SolicitacaoInclusao> getSolicitacoesAluno(Long idAluno) {
		return inclusaoRepo.getSolicitacoesAluno(idAluno);
	}
	
	public static List<SemestreLetivo> removeSemestresDuplicados(List<SolicitacaoInclusao> list) {

		// Store unique items in result.
		List<SemestreLetivo> result = new ArrayList<>();

		// Record encountered Strings in HashSet.
		HashSet<SemestreLetivo> set = new HashSet<>();

		// Loop over argument list.
		for (SolicitacaoInclusao item : list) {
			SemestreLetivo semestre = item.getSemestreLetivo();
			// If String is not in set, add it to the list and the set.
		    if (!set.contains(semestre)) {
		    	result.add(semestre);
		    	set.add(semestre);
		    }
		}
		return result;
	}

	public void incluiSolicitacao(List<ItemSolicitacao> listaItemSolicitacao,
								  Aluno aluno, SemestreLetivo semestreLetivo) {
		
		SolicitacaoInclusao solicitacao = getSolicitacaoByAlunoSemestre(aluno.getId(), 
																		semestreLetivo.getAno(), 
																		semestreLetivo.getPeriodo());
		if(solicitacao != null){
			solicitacao.addItemSolicitacao(listaItemSolicitacao);
		} else {
			solicitacao = new SolicitacaoInclusao(listaItemSolicitacao, aluno, semestreLetivo);
		}
		
		inclusaoRepo.adicionarSolicitacaoInclusao(solicitacao);
	}
	
	public void homeInclusao(String cpf, Model model) {
		Aluno aluno = getAlunoByCpf(cpf);
		Long idAluno = aluno.getId();
		List<SolicitacaoInclusao> solicitacao = getSolicitacoesAluno(idAluno);
		PeriodoAvaliacoesTurmas periodoAvaliacao = PeriodoAvaliacoesTurmas.getInstance();
		SemestreLetivo semestreLetivo = periodoAvaliacao.getSemestreLetivo();
		SolicitacaoInclusao solicitacaoAtual = getSolicitacaoByAlunoSemestre(idAluno, semestreLetivo.getAno(), 
																					 semestreLetivo.getPeriodo());
		if(solicitacao != null){
			List<SemestreLetivo> listaSemestresLetivos = removeSemestresDuplicados(solicitacao);
			model.addAttribute("listaSemestresLetivos", listaSemestresLetivos);
		}

		if(solicitacaoAtual != null){
			model.addAttribute("numeroSolicitacoes", solicitacaoAtual.getItemSolicitacao().size());
		} else {
			model.addAttribute("numeroSolicitacoes", 0);
		}
		
		model.addAttribute("periodoLetivo",	periodoAvaliacao.getSemestreLetivo());
		model.addAttribute("aluno", aluno);
	}
	
	public void solicitaInclusao(String cpf, int numeroSolicitacoes, Model model) {
		
		Aluno aluno = getAlunoByCpf(cpf);
		PeriodoAvaliacoesTurmas periodoAvaliacao = PeriodoAvaliacoesTurmas
				.getInstance();

		model.addAttribute("departamentos", iniciarSolicitacoes());
		model.addAttribute("cpf", cpf);
		model.addAttribute("aluno", aluno);
		model.addAttribute("periodoLetivo",	periodoAvaliacao.getSemestreLetivo());
		model.addAttribute("numeroSolicitacoes",numeroSolicitacoes);

	}
	
	public void validaturma(HttpServletRequest request, Aluno aluno, SemestreLetivo semestreLetivo, 
							Departamento depto, Comprovante comprovante, int opcao, String observacao) {
		int i = 0;
		Map<String, String[]> parameters = request.getParameterMap();
		List<ItemSolicitacao> listaItemSolicitacao = new ArrayList<>();
		// parameters must contain only sorted quesitoX parameters
		while (parameters.containsKey("codigoTurma" + i)) {
			Turma turma = getTurmaPorCodigo(parameters.get("codigoTurma" + i)[0]);
			if(turma == null) {
				throw new IllegalArgumentException("Turma "+parameters.get("codigoTurma" + i)[0]+" inexistente");
			}
			
			boolean isSolicitacaoRepetida = isSolicitacaoRepetida(aluno.getId(), turma.getCodigo(),	semestreLetivo);
			
			if(isSolicitacaoRepetida){
				throw new IllegalArgumentException(
						"Erro: Já foi feita uma solicitação para a turma "+ turma.getCodigo());
			}
			
			Date dataSolicitacao = new Date();
			listaItemSolicitacao.add(new ItemSolicitacao(dataSolicitacao,turma,depto,comprovante,EnumStatusSolicitacao.AGUARDANDO, opcao, observacao));
			++i;
		}
		
		incluiSolicitacao(listaItemSolicitacao, aluno, semestreLetivo);
		
	}
	
	public void validaSolicitacao(HttpServletRequest request, String cpf, MultipartFile file, 
									String departamento, int opcao, String observacao) throws IOException {
		
		Departamento depto = getDepartamentoById(departamento);
		PeriodoAvaliacoesTurmas periodoAvaliacao = PeriodoAvaliacoesTurmas.getInstance();
		SemestreLetivo semestreLetivo = periodoAvaliacao.getSemestreLetivo();
		Aluno aluno = getAlunoByCpf(cpf);
		
		validaComprovante(file);
		Comprovante comprovante = new Comprovante(file.getContentType(), file.getBytes(), file.getOriginalFilename());
		
		validaturma(request,aluno,semestreLetivo, depto, comprovante, opcao, observacao);
	}
	
	public void validaComprovante(MultipartFile file){
		if(file.getSize() > TAMANHO_MAXIMO_COMPROVANTE){
			throw new IllegalArgumentException("O arquivo de comprovante deve ter 10mb no máximo");
		}
		String[] tiposAceitos = {"application/pdf", "image/jpeg", "image/png"};
		if (ArrayUtils.indexOf(tiposAceitos, file.getContentType()) < 0){
			throw new IllegalArgumentException("O arquivo de comprovante deve ser no formato PDF, JPEG ou PNG");
		}
	}
	
	public boolean isSolicitacaoRepetida(Long idAluno, String codigoTurma, SemestreLetivo semestreLetivo) {
		Turma turma = inclusaoRepo.getTurmaSolicitada(idAluno,codigoTurma,semestreLetivo.getAno(),semestreLetivo.getPeriodo());
		
		if(turma == null){
			return false;
		} else {
			return true;
		}
	}

	public void downloadFile(String cpf, Long solicitacaoId,
			HttpServletRequest request, HttpServletResponse response) throws IOException {
		Comprovante comprovante = getComprovante(solicitacaoId);
		response.setContentType(comprovante.getContentType());
        response.setHeader("Content-Disposition", "attachment;filename=\"" +comprovante.getNome()+ "\"");
        OutputStream out = response.getOutputStream();
        response.setContentType(comprovante.getContentType());
        out.write(comprovante.getData());
        out.flush();
        out.close();
	}
}



	
