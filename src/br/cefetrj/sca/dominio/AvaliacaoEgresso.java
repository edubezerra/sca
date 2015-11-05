package br.cefetrj.sca.dominio;


import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
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
	@JoinColumn(nullable = false)
	private FormularioAvaliacao formAvaliacao;
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "RESPOSTA", joinColumns = {
			@JoinColumn(name = "AVALIACAOEGRESSO_ID", referencedColumnName = "ID", nullable = true) }, inverseJoinColumns = {
					@JoinColumn(name = "ALTERNATIVA_ID", referencedColumnName = "ID", nullable = false) })
	private List<Alternativa> alternativas;
	
	@SuppressWarnings("unused")
	private AvaliacaoEgresso() {
	}

	public AvaliacaoEgresso(Aluno aluno, FormularioAvaliacao formulario, Alternativa alternativa) {
		if (aluno == null || formulario == null || alternativa == null) {
			throw new IllegalArgumentException(
					"Erro: argumentos inválidos para AvaliacaoEgresso().");
		}

		this.alunoAvaliador = aluno;
		this.formAvaliacao = formulario;
	}

	public Long getId() {
		return id;
	}
	
	public Aluno getAluno() {
		return alunoAvaliador;
	}
	
	public List<Alternativa> getAlternativas() {
		return alternativas;
	}

}
