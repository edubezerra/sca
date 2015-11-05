package br.cefetrj.sca.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.mysql.fabric.Response;

import br.cefetrj.sca.apresentacao.SolicitaAvaliacaoEgressoResponse;
import br.cefetrj.sca.apresentacao.SolicitaAvaliacaoEgressoResponse.AlternativaDto;
import br.cefetrj.sca.dominio.FormularioAvaliacao;
import br.cefetrj.sca.dominio.avaliacaoturma.Alternativa;
import br.cefetrj.sca.dominio.avaliacaoturma.Quesito;
import br.cefetrj.sca.dominio.repositorio.FormularioAvaliacaoRepositorio;

public class AvaliacaoEgressoService {
	
	@Autowired
	private FormularioAvaliacaoRepositorio faRepo;
	
	public void avaliaEgresso(String cpf, List<Integer> respostas) {
		// TODO Auto-generated method stub
		
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
