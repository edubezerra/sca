package br.cefetrj.sca.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import br.cefetrj.sca.dominio.FichaAvaliacoes;
import br.cefetrj.sca.dominio.Turma;
import br.cefetrj.sca.dominio.repositories.TurmaRepositorio;

public class RegistrarAvaliacaoService {

	@Autowired
	TurmaRepositorio repositorioTurma;

	List<Turma> iniciarLancamentoNotas(String matriculaProfessor) {
		return null;
	}

	FichaAvaliacoes solicitarLancamento(String codigoTurma) {
		return null;
	}

	void registrarLancamentos(FichaAvaliacoes ficha) {
		Turma turma = repositorioTurma.findTurmaByCodigo(ficha.getCodigoTurma());
		ficha.lancarAvaliacoesEmTurma(turma);
	}

	void confirmarLancamentoAvaliacoes() {
	}
}
