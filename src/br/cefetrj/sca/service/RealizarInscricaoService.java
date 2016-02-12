package br.cefetrj.sca.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import br.cefetrj.sca.dominio.Aluno;
import br.cefetrj.sca.dominio.PeriodoLetivo;
import br.cefetrj.sca.dominio.Turma;
import br.cefetrj.sca.dominio.TurmaRepositorio;
import br.cefetrj.sca.dominio.inscricoes.TurmasPossiveisServico;

public class RealizarInscricaoService {

	Aluno aluno;

	@Autowired
	TurmaRepositorio turmaRepositorio;

	private List<Turma> turmaSelecionadas;

	private TurmasPossiveisServico tpServico;

	public List<Turma> iniciarInscricao(String matrAluno) {
		return tpServico.getTurmasPossiveis(matrAluno);
	}

	public Turma registrarInscricao(String codigoTurma) {
		Turma t = turmaRepositorio.getByCodigoNoPeriodoLetivo(codigoTurma,
				PeriodoLetivo.PERIODO_CORRENTE.proximo());
		t.inscreverAluno(aluno);
		turmaSelecionadas.add(t);
		return t;
	}

}
