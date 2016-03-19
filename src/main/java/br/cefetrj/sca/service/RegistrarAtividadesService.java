package br.cefetrj.sca.service;

import java.io.IOException;
import java.time.Duration;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import br.cefetrj.sca.dominio.Aluno;
import br.cefetrj.sca.dominio.atividadecomplementar.AtividadeComplementar;
import br.cefetrj.sca.dominio.atividadecomplementar.RegistroAtividadeComplementar;
import br.cefetrj.sca.dominio.inclusaodisciplina.Comprovante;
import br.cefetrj.sca.dominio.repositories.AlunoRepositorio;
import br.cefetrj.sca.dominio.repositories.RegistroAtividadeComplementarRepositorio;
import br.cefetrj.sca.service.util.SolicitaRegistroAtividadesResponse;
import br.cefetrj.sca.service.util.SolicitaSituacaoAtividadesResponse;

@Component
public class RegistrarAtividadesService {
	
	private static final int TAMANHO_MAXIMO_COMPROVANTE = 10000000;

	@Autowired
	private AlunoRepositorio alunoRepo;
	
	@Autowired
	private RegistroAtividadeComplementarRepositorio regRepo;

	/**
	 * Esse método é invocado quando um aluno solicita o registro de atividades complementares.
	 * 
	 * @param matriculaAluno
	 *            CPF do aluno solicitante.
	 * 
	 * @return objeto com informações para construir formulário de registros de atividade complementar.
	 * 
	 */
	public SolicitaRegistroAtividadesResponse obterRegistrosAtividade(String matriculaAluno) {
		
		Aluno aluno = getAlunoPorMatricula(matriculaAluno);

		SolicitaRegistroAtividadesResponse response = new SolicitaRegistroAtividadesResponse();

		for (RegistroAtividadeComplementar reg : aluno.getRegistrosAtiv()) {
			response.add(reg);
		}

		return response;
	}
	
	public SolicitaSituacaoAtividadesResponse obterSituacaoAtividades(String matriculaAluno, Model model) {
		
		Aluno aluno = getAlunoPorMatricula(matriculaAluno);

		SolicitaSituacaoAtividadesResponse response = new SolicitaSituacaoAtividadesResponse();
		
		Set<String> categorias = new HashSet<String>();

		for (AtividadeComplementar ativ : aluno.getVersaoCurso().getTabelaAtividades().getAtividades()) {
			response.add(ativ, aluno.getCargaHorariaCumpridaAtiv(ativ),
					aluno.temCargaHorariaMinimaAtividade(ativ),aluno.temCargaHorariaMaximaAtividade(ativ));
			categorias.add(ativ.getTipo().getCategoria().toString());
		}
		model.addAttribute("categorias", categorias);

		return response;
	}
	
	public void solicitaRegistroAtividades(String matriculaAluno, Model model) {

		Aluno aluno = getAlunoPorMatricula(matriculaAluno);
		
		model.addAttribute("nomeAluno", aluno.getNome());
		model.addAttribute("curso", aluno.getVersaoCurso().getCurso());
		model.addAttribute("versaoCurso", aluno.getVersaoCurso().getNumero());
		model.addAttribute("temAtividadesSuficientes", aluno.temAtividadesSuficientes());
		
		String totalCH = aluno.getCargaHorariaCumpridaAtiv().toHours()+"/"+aluno.getVersaoCurso().getCargaHorariaMinAitvComp().toHours(); 
		model.addAttribute("totalCH", totalCH);
	}
	
	public void solicitaRegistroAtividade(String matriculaAluno, Long idAtividade, Model model) {

		Aluno aluno = getAlunoPorMatricula(matriculaAluno);
		AtividadeComplementar atividade = aluno.getVersaoCurso().getTabelaAtividades().getAtividade(idAtividade);
		
		model.addAttribute("codigoAtiv", atividade.getTipo().getCodigo());
		
		String descricao = atividade.getTipo().getDescricao();
		descricao = descricao.charAt(0) + (descricao.substring(1).toLowerCase());
		
		model.addAttribute("descrAtiv", descricao);
		
		Long cargaHorariaCumprida = aluno.getCargaHorariaCumpridaAtiv(atividade).toHours();
		Long cargaHorariaMax = atividade.getCargaHorariaMax().toHours();
		Long cargaHorariaRestante = cargaHorariaMax - cargaHorariaCumprida;
		
		model.addAttribute("cargaHorariaRestante", cargaHorariaRestante);
	}

	public void registraAtividade(String matriculaAluno, Long idAtividade, String descricao,
			String cargaHoraria, MultipartFile file) throws IOException  {

		Aluno aluno = getAlunoPorMatricula(matriculaAluno);
		AtividadeComplementar atividade = aluno.getVersaoCurso().getTabelaAtividades().getAtividade(idAtividade);
		
		if (aluno != null && atividade != null) {
			
			Long ch = 0l;
			try {
				ch = Long.parseLong(cargaHoraria);
			} catch (NumberFormatException e) {
				throw new IllegalArgumentException(
						"Erro: Carga horária inválida. "
								+ "Por favor, forneça um valor de carga horária maior do que zero.");
			}
			if (ch <= 0) {
				throw new IllegalArgumentException(
						"Erro: Carga horária inválida. "
								+ "Por favor, forneça um valor de carga horária maior do que zero.");
			}
			
			Long cargaHorariaCumprida = aluno.getCargaHorariaCumpridaAtiv(atividade).toHours();
			Long cargaHorariaMax = atividade.getCargaHorariaMax().toHours();
			if (ch + cargaHorariaCumprida > cargaHorariaMax) {
				Long cargaHorariaRestante = cargaHorariaMax - cargaHorariaCumprida;
				throw new IllegalArgumentException(
						"Erro: A carga horária máxima da atividade complementar foi ultrapassada. "
								+ "Por favor, forneça um valor de carga horária de até "+
								cargaHorariaRestante.intValue() +" horas.");
			}
			
			if (file == null || file.isEmpty()) {
				throw new IllegalArgumentException(
						"Erro: Comprovante não encontrado. "
								+ "Por favor, forneça o comprovante da atividade complementar.");
			}
			validaComprovante(file);
			Comprovante doc = new Comprovante(file.getContentType(),
					file.getBytes(), file.getOriginalFilename());
			
			RegistroAtividadeComplementar reg = 
					new RegistroAtividadeComplementar(Calendar.getInstance().getTime(),atividade, descricao, Duration.ofHours(ch), doc);
			
			regRepo.save(reg);
			aluno.registraAtividade(reg);
			alunoRepo.save(aluno);
		}
	}
	
	public void removeRegistroAtividade(String matriculaAluno, Long idReg) {

		Aluno aluno = getAlunoPorMatricula(matriculaAluno);

		if (aluno != null) {
			
			if (idReg == null || idReg <= 0) {
				throw new IllegalArgumentException(
						"Erro: Id de registro de atividade complementar inválido.");
			}
			
			RegistroAtividadeComplementar reg = aluno.getRegistroAtiv(idReg);
			
			aluno.removeRegAtividade(idReg);
			alunoRepo.save(aluno);
			regRepo.delete(reg);
		}
	}

	private Aluno getAlunoPorMatricula(String matriculaAluno) {
		if (matriculaAluno == null || matriculaAluno.trim().equals("")) {
			throw new IllegalArgumentException("Matrícula deve ser fornecida!");
		}

		Aluno aluno = null;

		try {
			aluno = alunoRepo.findAlunoByMatricula(matriculaAluno);
		} catch (Exception e) {
			throw new IllegalArgumentException("Aluno não encontrado ("
					+ matriculaAluno + ")", e);
		}

		return aluno;
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
	
	public Comprovante getComprovante(String matriculaAluno, Long idReg) {
		
		Aluno aluno = getAlunoPorMatricula(matriculaAluno);
		return aluno.getRegistroAtiv(idReg).getDocumento();
	}
}
