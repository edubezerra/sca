package br.cefetrj.sca.dominio;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.context.ApplicationEvent;

/**
 * Essa classe representa o evento de fechamento de notas de um subconjuntos de
 * alunos de uma turma.
 * 
 * @author Eduardo
 *
 */
public class LancamentoNotasFechado extends ApplicationEvent {
	
	private static final long serialVersionUID = 1L;
	
	Turma turma;
	Date fechamentoEm;
	List<Long> idsAlunos = new ArrayList<>();

	public LancamentoNotasFechado(Object source, Turma turma, List<Long> idsAlunos) {
		super(source);
		this.turma = turma;
		this.idsAlunos = idsAlunos;
	}
	
	public Turma getTurma() {
		return turma;
	}
	
	public List<Long> getIdsAlunos() {
		return idsAlunos;
	}
}
