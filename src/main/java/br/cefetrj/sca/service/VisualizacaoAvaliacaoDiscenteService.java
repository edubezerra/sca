package br.cefetrj.sca.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.cefetrj.sca.dominio.Professor;
import br.cefetrj.sca.dominio.Turma;
import br.cefetrj.sca.dominio.avaliacaoturma.Alternativa;
import br.cefetrj.sca.dominio.avaliacaoturma.AvaliacaoTurma;
import br.cefetrj.sca.dominio.repositories.AvaliacaoTurmaRepositorio;
import br.cefetrj.sca.dominio.repositories.TurmaRepositorio;

@Service
public class VisualizacaoAvaliacaoDiscenteService {

	@Autowired
	private TurmaRepositorio repositorio;
	@Autowired
	private AvaliacaoTurmaRepositorio repositorio2;

	private String[] PerguntasAvaliacao = { "Primeira", "Segunda", "Terceira", "Quarta", "Quinta", "Sexta", "Setima",
			"Oitava" };

	public List<Turma> listarTurmaLecionadas(Professor p) {
		List<Turma> t = repositorio.findTurmasLecionadasPorProfessor(p.getMatricula());
		List<Turma> turmasAvaliadas = new ArrayList<>();

		for (Turma turma : t) {
			List<AvaliacaoTurma> a = repositorio2.findAvaliacoesTurmaLista(turma.getId());
			if (a.size() != 0) {
				turmasAvaliadas.add(turma);
			}
		}
		return turmasAvaliadas;
	}

	public List<AvaliacaoTurma> selecionarTurma(Turma t) {
		List<AvaliacaoTurma> tAvaliadas = repositorio2.findAvaliacoesTurmaLista(t.getId());
		return tAvaliadas;
	}

	public List<String> conversaoRespospa(List<AvaliacaoTurma> avaliacaoTurma) {

		List<String> respAvaliacao = new ArrayList<>();
		respAvaliacao.add("Pergunta");
		respAvaliacao.add("Insuficiente(s) ou Ruim(ns)");
		respAvaliacao.add("Suficiente(s) ou Regular(es)");
		respAvaliacao.add("Bom(ns) ou Boa(s)");
		respAvaliacao.add("Otimo(s) ou Otima(s)");

		List<Alternativa> resp = new ArrayList<>();
		for (int i = 0; i < avaliacaoTurma.size(); i++) {
			AvaliacaoTurma a = avaliacaoTurma.get(i);
			resp.addAll(a.getRespostas());
		}

		int[] valorRespostas = new int[4];
		int posicao = 0;
		int posicaoPerg = 0;
		for (int i = 0; i < resp.size(); i++) {
			int valorResp = 1;
			if (resp.get(posicao).getDescritor().equals("Insuficiente(s) ou Ruim(ns)")) {
				valorRespostas[3] += valorResp;
			}
			if (resp.get(posicao).getDescritor().equals("Suficiente(s) ou Regular(es)")) {
				valorRespostas[2] += valorResp;
			}
			if (resp.get(posicao).getDescritor().equals("Bom(ns) ou Boa(s)")) {
				valorRespostas[1] += valorResp;
			}
			if (resp.get(posicao).getDescritor().equals("Ótimo(s) ou Ótima(s)")) {
				valorRespostas[0] += valorResp;
			}
			posicao += 8;
			if (posicao >= resp.size()) {
				respAvaliacao.add(PerguntasAvaliacao[posicaoPerg]);
				respAvaliacao.add("" + valorRespostas[3]);
				respAvaliacao.add("" + valorRespostas[2]);
				respAvaliacao.add("" + valorRespostas[1]);
				respAvaliacao.add("" + valorRespostas[0]);

				posicaoPerg += 1;
				posicao = posicaoPerg;
				valorRespostas = new int[4];
			}
		}

		return respAvaliacao;

	}

	public List<String> obterRespostasPos(List<AvaliacaoTurma> avaliacaoTurma) {

		List<String> respAvaliacao = new ArrayList<>();

		for (int i = 0; i < avaliacaoTurma.size(); i++) {
			AvaliacaoTurma a = avaliacaoTurma.get(i);
			respAvaliacao.add(a.getAspectosPositivos());
		}
		return respAvaliacao;

	}
	public List<String> obterRespostasNeg(List<AvaliacaoTurma> avaliacaoTurma) {

		List<String> respAvaliacao = new ArrayList<>();

		for (int i = 0; i < avaliacaoTurma.size(); i++) {
			AvaliacaoTurma a = avaliacaoTurma.get(i);
			respAvaliacao.add(a.getAspectosNegativos());
		}
		return respAvaliacao;

	}
}
