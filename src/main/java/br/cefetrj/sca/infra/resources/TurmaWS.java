package br.cefetrj.sca.infra.resources;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TurmaWS {
	
	private Long id;
	private String codigo;
	private DisciplinaWS disciplina;
	private List<PessoaWS> alunos;
	 
	@JsonCreator
	public TurmaWS(Long id, String codigo, DisciplinaWS disciplina) {
		super();
		this.id = id;
		this.codigo = codigo;
		this.disciplina = disciplina;
		this.alunos = new ArrayList<>();
	}

	@JsonProperty("id")
	public Long getId() {
		return id;
	}

	@JsonProperty("codigo")
	public String getCodigo() {
		return codigo;
	}
	
	@JsonProperty("disciplina")
	public DisciplinaWS getDisciplina() {
		return disciplina;
	}	
	
	public void adicionarAluno(PessoaWS a){
		this.alunos.add(a);
	}
	
	@JsonProperty("alunos")
	public List<PessoaWS> getAlunos(){
		return alunos;
	}
	
}
