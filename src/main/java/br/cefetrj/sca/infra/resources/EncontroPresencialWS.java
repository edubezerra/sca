package br.cefetrj.sca.infra.resources;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import br.cefetrj.sca.dominio.Aluno;

public class EncontroPresencialWS {

	@JsonProperty("data")
	private Date data;

	@JsonProperty("alunos")
	private List<PessoaWS> alunos = new ArrayList<>();

	@JsonProperty("data")
	public Date getData() {
		return data;
	}

	@JsonProperty("data")
	public void setData(Date data) {
		this.data = data;
	}

	@JsonProperty("alunos")
	public List<PessoaWS> getAlunos() {
		return alunos;
	}

	@JsonProperty("alunos")
	public void setAlunos(List<PessoaWS> alunos) {
		this.alunos = alunos;
	}

	public void adicionarAluno(PessoaWS aluno) {
		this.alunos.add(aluno);
	}

}
