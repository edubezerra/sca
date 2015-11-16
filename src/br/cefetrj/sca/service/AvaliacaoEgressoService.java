package br.cefetrj.sca.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.mysql.fabric.Response;

import br.cefetrj.sca.apresentacao.SolicitaAvaliacaoEgressoResponse;
import br.cefetrj.sca.apresentacao.SolicitaAvaliacaoEgressoResponse.AlternativaDto;
import br.cefetrj.sca.dominio.Aluno;
import br.cefetrj.sca.dominio.AvaliacaoEgresso;
import br.cefetrj.sca.dominio.FormularioAvaliacao;
import br.cefetrj.sca.dominio.avaliacaoturma.Alternativa;
import br.cefetrj.sca.dominio.avaliacaoturma.Quesito;
import br.cefetrj.sca.dominio.repositorio.AlternativaRepositorio;
import br.cefetrj.sca.dominio.repositorio.AlunoRepositorio;
import br.cefetrj.sca.dominio.repositorio.AvaliacaoEgressoRepositorio;
import br.cefetrj.sca.dominio.repositorio.FormularioAvaliacaoRepositorio;

public class AvaliacaoEgressoService {
	
	@Autowired
	private FormularioAvaliacaoRepositorio faRepo;
	
	@Autowired
	private AlunoRepositorio alunoRepo;
	
	@Autowired
	private AlternativaRepositorio alternativaRepo;
	
	@Autowired
	private AvaliacaoEgressoRepositorio egressoRepo;
	
	private Aluno getAlunoPorCPF(String cpf) {
		if (cpf == null || cpf.trim().equals("")) {
			throw new IllegalArgumentException("CPF deve ser fornecido!");
		}

		Aluno aluno = null;

		try {
			aluno = alunoRepo.getByCPF(cpf);
		} catch (Exception e) {
			throw new IllegalArgumentException("Aluno não encontrado (" + cpf
					+ ")", e);
		}

		return aluno;
	}
	
	
	
	public void avaliaEgresso(String cpf, List<Integer> respostas, String especialidade, 
			String questao10Outro, String questao15Area) {
		
		FormularioAvaliacao form = faRepo.obterFormulario("Egresso");
		Aluno aluno = getAlunoPorCPF(cpf);
		List<Alternativa> alternativas = new ArrayList<Alternativa>();
		
		for(int i = 0; i < respostas.size(); i++){	
			Alternativa a = alternativaRepo.getById(respostas.get(i));
			alternativas.add(a);
		}

		AvaliacaoEgresso avaliacao = new AvaliacaoEgresso(aluno, alternativas, form);
		
		if(especialidade != null && especialidade.length() > 0){
			avaliacao.setEspecialidade(especialidade);
		}
		
		if(questao10Outro != null && questao10Outro.length() > 0){
			avaliacao.setQuestao10_Outro(questao10Outro);
		}
		
		if(questao15Area != null && questao15Area.length() > 0){
			avaliacao.setQuestao15_Area(questao15Area);
		}
		
		
		egressoRepo.adicionar(avaliacao);	
		
	}	

	public SolicitaAvaliacaoEgressoResponse retornaQuestoes() {
		SolicitaAvaliacaoEgressoResponse response = new SolicitaAvaliacaoEgressoResponse();
		List<Quesito> quesitos = faRepo.obterQuesitos("Egresso");
		List<AlternativaDto> alternativas;
		if(quesitos == null || quesitos.size() == 0)
			return null;

		for (Quesito quesito : quesitos) {
			alternativas = new ArrayList<AlternativaDto>();

			for (Alternativa alternativa : quesito.getAlternativas()) {
				alternativas.add(response.new AlternativaDto(alternativa.getId(), alternativa.getDescritor()));
			}

			response.add(response.new Item(quesito.getEnunciado(), alternativas));
		}
		return response;
	}

}
