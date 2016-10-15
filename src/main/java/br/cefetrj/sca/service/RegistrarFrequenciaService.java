package br.cefetrj.sca.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.cefetrj.sca.dominio.PeriodoLetivo;
import br.cefetrj.sca.dominio.Professor;
import br.cefetrj.sca.dominio.Turma;
import br.cefetrj.sca.dominio.repositories.AlunoRepositorio;
import br.cefetrj.sca.dominio.repositories.ProfessorRepositorio;
import br.cefetrj.sca.dominio.repositories.TurmaRepositorio;

@Component
public class RegistrarFrequenciaService {

	@Autowired
	private AlunoRepositorio alunoRepo;

	@Autowired
	private ProfessorRepositorio professorRepo;

	@Autowired
	private TurmaRepositorio turmaRepo;

	public List<Turma> iniciarRegistroFreqTurma(String matriculaProfessor) {
		return turmaRepo.findTurmasLecionadasPorProfessorEmPeriodo(
				matriculaProfessor, PeriodoLetivo.PERIODO_CORRENTE);
	}

}
