package br.cefetrj.sca.dominio;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;

@Entity
public class ListaEspera {

	@Id
	@GeneratedValue
	private Long id;

	@OneToOne
	private Disciplina disciplina;

	@ManyToMany
	@JoinTable(name = "LISTAESPERA_ALUNO", joinColumns = { @JoinColumn(name = "LISTAESPERA_ID", referencedColumnName = "ID") }, inverseJoinColumns = { @JoinColumn(name = "ALUNO_ID", referencedColumnName = "ID") })
	private List<Aluno> alunos;

	@SuppressWarnings("unused")
	private ListaEspera() {
	}

	public ListaEspera(Disciplina disciplina) {
		if (disciplina != null) {
			throw new IllegalArgumentException("Disciplina não pode ser nula.");
		}
		this.disciplina = disciplina;
		this.alunos = new ArrayList<Aluno>();
	}

	public Long getId() {
		return id;
	}

	public Disciplina getDisciplina() {
		return disciplina;
	}

	public List<Aluno> getAlunos() {
		return alunos;
	}

	public void inserirAluno(Aluno aluno) {
		this.alunos.add(aluno);
	}

	public void removerAluno(Aluno aluno) {
		this.alunos.remove(aluno);
	}
}
