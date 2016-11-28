package br.cefetrj.sca.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.cefetrj.sca.dominio.Aluno;
import br.cefetrj.sca.dominio.AvaliacaoEgresso;
import br.cefetrj.sca.dominio.PesquisaAvaliacao;
import br.cefetrj.sca.dominio.avaliacaoturma.Alternativa;
import br.cefetrj.sca.dominio.avaliacaoturma.Quesito;
import br.cefetrj.sca.dominio.repositories.AlternativaRepositorio;
import br.cefetrj.sca.dominio.repositories.AlunoRepositorio;
import br.cefetrj.sca.dominio.repositories.AvaliacaoEgressoRepositorio;
import br.cefetrj.sca.dominio.repositories.PesquisaAvaliacaoRepositorio;
import br.cefetrj.sca.web.controllers.SolicitaAvaliacaoEgressoResponse;
import br.cefetrj.sca.web.controllers.SolicitaAvaliacaoEgressoResponse.AlternativaDto;

@Service
public class AvaliacaoEgressoService {

	@Autowired
	private PesquisaAvaliacaoRepositorio faRepo;

	@Autowired
	private AlunoRepositorio alunoRepo;

	@Autowired
	private AlternativaRepositorio alternativaRepo;

	@Autowired
	private AvaliacaoEgressoRepositorio egressoRepo;

	private Aluno getAlunoPorMatricula(String matricula) {
		if (matricula == null || matricula.trim().equals("")) {
			throw new IllegalArgumentException("Matrícula deve ser fornecida!");
		}

		Aluno aluno = null;

		try {
			aluno = alunoRepo.findAlunoByMatricula(matricula);
		} catch (Exception e) {
			throw new IllegalArgumentException("Aluno não encontrado ("
					+ matricula + ")", e);
		}

		return aluno;
	}

	public void avaliaEgresso(String matricula, List<Long> respostas,
			String especialidade, String questao10Outro, String questao15Area) {

		PesquisaAvaliacao form = faRepo.findByDescritor("AvaliacaoEgresso");
		Aluno aluno = getAlunoPorMatricula(matricula);
		List<Alternativa> alternativas = new ArrayList<Alternativa>();

		for (int i = 0; i < respostas.size(); i++) {
			Alternativa a = alternativaRepo.findOne(respostas.get(i));
			alternativas.add(a);
		}

		AvaliacaoEgresso avaliacao = new AvaliacaoEgresso(aluno, alternativas,
				form);

		if (especialidade != null && especialidade.length() > 0) {
			avaliacao.setEspecialidade(especialidade);
		}

		if (questao10Outro != null && questao10Outro.length() > 0) {
			avaliacao.setQuestao10_Outro(questao10Outro);
		}

		if (questao15Area != null && questao15Area.length() > 0) {
			avaliacao.setQuestao15_Area(questao15Area);
		}

		egressoRepo.save(avaliacao);

	}

	public SolicitaAvaliacaoEgressoResponse retornaQuestoes() {
		SolicitaAvaliacaoEgressoResponse response = new SolicitaAvaliacaoEgressoResponse();
		PesquisaAvaliacao form = faRepo.findByDescritor("AvaliacaoEgresso");
		List<Quesito> quesitos = form.getQuesitos();
		List<AlternativaDto> alternativas;
		if (quesitos == null || quesitos.size() == 0)
			return null;

		for (Quesito quesito : quesitos) {
			alternativas = new ArrayList<AlternativaDto>();

			for (Alternativa alternativa : quesito.getAlternativas()) {
				alternativas.add(response.new AlternativaDto(alternativa
						.getId(), alternativa.getDescritor()));
			}

			response.add(response.new Item(quesito.getEnunciado(), alternativas));
		}
		return response;
	}

	public Aluno findAlunoByMatricula(String matricula) {
		return alunoRepo.findAlunoByMatricula(matricula);
	}

}
