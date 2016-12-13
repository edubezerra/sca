package br.cefetrj.sca.dominio;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

@Entity
public class EncontroPresencial {

	@Id
	@GeneratedValue
	private Long id;

	private Date data;

	@ManyToMany
	@JoinTable(name = "FREQUENCIA_ALUNOS", joinColumns = { @JoinColumn(name = "ENCONTRO_ID", referencedColumnName = "ID") }, inverseJoinColumns = { @JoinColumn(name = "ALUNO_ID", referencedColumnName = "ID") })
	private List<Aluno> alunos = new ArrayList<>();

	public Long getId() {
		return id;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public List<Aluno> getAlunos() {
		return alunos;
	}

	public void setAlunos(List<Aluno> alunos) {
		this.alunos = alunos;
	}

	public void adicionarAluno(Aluno aluno) {
		this.alunos.add(aluno);
	}

}
