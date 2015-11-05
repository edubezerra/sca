package br.cefetrj.sca.dominio;


import javax.persistence.Entity;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import br.cefetrj.sca.dominio.avaliacaoturma.Alternativa;

@Entity
public class AvaliacaoEgresso {
	@Id
	@GeneratedValue
	private Long id;
	
	@ManyToOne
	@JoinColumn(nullable = false)
	private Aluno alunoAvaliador;
	
	@ManyToOne
	private FormularioAvaliacaoEgresso formEgresso;
	
	@ManyToOne
	@JoinColumn(nullable = false)
	private Alternativa alternativa;
	
	@SuppressWarnings("unused")
	private AvaliacaoEgresso() {
	}

	public AvaliacaoEgresso(Aluno aluno, FormularioAvaliacaoEgresso formulario, Alternativa alternativa) {
		if (aluno == null || formulario == null || alternativa == null) {
			throw new IllegalArgumentException(
					"Erro: argumentos inválidos para AvaliacaoEgresso().");
		}

		this.alunoAvaliador = aluno;
		this.formEgresso = formulario;
	}

	public Long getId() {
		return id;
	}
	
	public Aluno getAluno() {
		return alunoAvaliador;
	}
	
	public Alternativa getAlternativa() {
		return alternativa;
	}

}
