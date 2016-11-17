package br.cefetrj.sca.dominio;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Departamento {
	@Id
	@GeneratedValue
	private Long id;

	private String sigla;

	private String nome;

	@OneToMany(cascade = CascadeType.ALL)
	private Set<Professor> professores = new HashSet<>();

	@OneToMany(cascade = CascadeType.ALL)
	private Set<Disciplina> disciplinas = new HashSet<>();

	@SuppressWarnings("unused")
	private Departamento() {
	}

	public Departamento(String sigla, String nome) {
		this.sigla = sigla;
		this.nome = nome;
	}

	public Long getId() {
		return id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String deptName) {
		this.nome = deptName;
	}

	public String getSigla() {
		return sigla;
	}

	public void addProfessor(Professor professor) {
		this.professores.add(professor);
	}

	public void addDisciplina(Disciplina disciplina) {
		this.disciplinas.add(disciplina);
	}

	public Set<Professor> getProfessores() {
		return professores;
	}

	public Set<Disciplina> getDisciplinas() {
		return disciplinas;
	}

	public String toString() {
		return "sigla: " + getSigla() + ", nome: " + getNome();
	}

	public void removerProfessor(Professor professor) {
		this.professores.remove(professor);
	}

	public void removerDisciplina(Disciplina disciplina) {
		this.disciplinas.remove(disciplina);
	}

}