package br.cefetrj.sca.service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
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

	public List<Turma> listarTurmaLecionadas(Professor p) {
		List<Turma> t = repositorio.findTurmasLecionadasPorProfessor(p.getMatricula());

		System.out.println(t.size());

		// listaAvaliacoes = repositorio2.findAll();

		for (Turma turma : t) {
			System.out.println(turma.getDisciplina() + "-" + turma.getCodigo() + " - " + turma.getPeriodo());
			/*List<AvaliacaoTurma> a = repositorio2.findAvaliacoesTurmaLista(turma.getId());
			if (a.size() == 0) {
				t.remove(turma);
			}*/

		}
		return t;

	}

	public List<AvaliacaoTurma> selecionarTurma(Turma t) {
		return repositorio2.findAvaliacoesTurmaLista(t.getId());
	}

	public void conversaoRespospa(AvaliacaoTurma avaliacaoTurma) throws IOException {
		BufferedWriter strW = new BufferedWriter(new FileWriter("C:/Users/Joao/workspaceSCA/sca-master/tabela.csv"));
		strW.write(
				"Pergunta,Insuficiente(s) ou Ruim(ns),Suficiente(s) ou Regular(es),Bom(ns) ou Boa(s),Ótimo(s) ou Ótima(s)\n");
		System.out.println("Service" + avaliacaoTurma.getRespostas().size());
		List<Alternativa> resp = avaliacaoTurma.getRespostas();
		int[] valorRespostas = new int[4];
		int posicao = 0;
		int posicaoPerg = 0;
		for (int i = 0; i < resp.size(); i++) {
			int valor;
			if (resp.get(posicao).getDescritor().equals("Insuficiente(s) ou Ruim(ns)")) {
				valor = 1;
				valorRespostas[3] += valor;
			}
			if (resp.get(posicao).getDescritor().equals("Suficiente(s) ou Regular(es)")) {
				valor = 2;
				valorRespostas[2] += valor;
			}
			if (resp.get(posicao).getDescritor().equals("Bom(ns) ou Boa(s)")) {
				valor = 3;
				valorRespostas[1] += valor;
			}
			if (resp.get(posicao).getDescritor().equals("Ótimo(s) ou Ótima(s)")) {
				valor = 4;
				valorRespostas[0] += valor;
			}
			posicao += 8;
			if (posicao >= resp.size()) {
				strW.write(posicaoPerg + "," + valorRespostas[0] + "," + valorRespostas[1] + "," + valorRespostas[2]
						+ "," + valorRespostas[3] + "\n");
				posicaoPerg += 1;
				posicao = posicaoPerg;
				valorRespostas = new int[4];
			}
		}
		strW.close();

	}
}
