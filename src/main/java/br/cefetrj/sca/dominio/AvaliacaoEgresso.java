package br.cefetrj.sca.dominio;


import java.util.ArrayList;
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
	
	private String especialidade;
	private String questao10_Outro;
	private String questao15_Area;
	
	@ManyToOne
	@JoinColumn(nullable = false)
	private Aluno alunoAvaliador;
	
	@ManyToOne
	@JoinColumn(nullable = false)
	private PesquisaAvaliacao formAvaliacao;
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "RESPOSTA_Egresso", joinColumns = {
			@JoinColumn(name = "AVALIACAOEGRESSO_ID", referencedColumnName = "ID", nullable = true) }, inverseJoinColumns = {
					@JoinColumn(name = "ALTERNATIVA_ID", referencedColumnName = "ID", nullable = false) })
	private List<Alternativa> alternativas = new ArrayList<Alternativa>();
	
	@SuppressWarnings("unused")
	private AvaliacaoEgresso() {
	}

	public AvaliacaoEgresso(Aluno aluno, List<Alternativa> alternativas, PesquisaAvaliacao form) {
		if (aluno == null || alternativas == null || form == null) {
			throw new IllegalArgumentException(
					"Erro: argumentos inválidos para AvaliacaoEgresso().");
		}
		
		this.formAvaliacao = form;
		this.alternativas = alternativas;
		this.alunoAvaliador = aluno;
	}

	public Long getId() {
		return id;
	}
	
	public Aluno getAluno() {
		return alunoAvaliador;
	}
	
	public void setEspecialidade(String especialidade){
		this.especialidade = especialidade;
	}
	
	public String getEspecialidade(){
		return this.especialidade;
	}
	
	public List<Alternativa> getAlternativas() {
		return alternativas;
	}

	public String getQuestao10_Outro() {
		return questao10_Outro;
	}

	public void setQuestao10_Outro(String questao10_Outro) {
		this.questao10_Outro = questao10_Outro;
	}

	public String getQuestao15_Area() {
		return questao15_Area;
	}

	public void setQuestao15_Area(String questao15_Area) {
		this.questao15_Area = questao15_Area;
	}

}
