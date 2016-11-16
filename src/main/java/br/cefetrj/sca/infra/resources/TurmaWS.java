package br.cefetrj.sca.infra.resources;

import java.util.ArrayList;
import java.util.List;

import br.cefetrj.sca.dominio.EncontroPresencial;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TurmaWS {
	
	@JsonProperty("id")
	private Long id;
	
	@JsonProperty("codigo")
	private String codigo;
	
	@JsonProperty("disciplina")
	private DisciplinaWS disciplina;
	
	@JsonProperty("alunos")
	private List<PessoaWS> alunos;
	
	@JsonProperty("encontros")
	private List<EncontroPresencialWS> encontros;
	 
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

	@JsonProperty("encontros")
	public List<EncontroPresencialWS> getEncontros() {
		return encontros;
	}

	@JsonProperty("encontros")
	public void setEncontros(List<EncontroPresencialWS> encontros) {
		this.encontros = encontros;
	}
	
}
