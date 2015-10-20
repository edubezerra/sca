package br.cefetrj.sca.dominio;

import java.util.Date;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.cefetrj.sca.dominio.contas.Email;

@Entity
public class Aluno {

	private static final int TAM_MIN_MATRICULA = 10;
	private static final int TAM_MAX_MATRICULA = 11;

	@Id
	@GeneratedValue
	private Long id;

	/**
	 * Matrícula do aluno, composta de <code>TAM_MATRICULA</code> carateres.
	 */
	private String matricula;


	@Embedded
	Pessoa pessoa;

	private String cpf;

	@ManyToOne
	private Curso curso;

	public Long getId() {
		return id;
	}

	@SuppressWarnings("unused")
	private Aluno() {
	}

	public Aluno(String nome, String matricula, String cpf) {
		this(nome, matricula);
		if (cpf == null || cpf.equals("")) {
			throw new IllegalArgumentException("CPF deve ser fornecido.");
		}
		this.cpf = cpf;
	}

	public Aluno(String nome, String matricula) {
		super();
		if (nome == null || nome.equals("")) {
			throw new IllegalArgumentException("Nome não pode ser vazio.");
		}
		if (matricula == null || matricula.equals("")) {
			throw new IllegalArgumentException("Matrícula não pode ser vazia.");
		}
		if (!(matricula.length() >= TAM_MIN_MATRICULA && matricula.length() <= TAM_MAX_MATRICULA)) {
			throw new IllegalArgumentException("Matrícula deve ter entre "
					+ TAM_MIN_MATRICULA + " e " + TAM_MAX_MATRICULA
					+ " caracteres: " + matricula);
		}
		this.pessoa = new Pessoa(nome);
		this.matricula = matricula;
	}

	public Aluno(String nome, String matricula, Date dataNascimento,
			String enderecoEmail) {
		this(nome, matricula);
		this.pessoa = new Pessoa(nome, dataNascimento, enderecoEmail);
	}

	public String getNome() {
		return this.pessoa.getNome();
	}

	public Date getDataNascimento() {
		return pessoa.getDataNascimento();
	}

	public String getMatricula() {
		return matricula;
	}

	public String getEmail() {
		return pessoa.getEmail().toString();
	}

	public String getCpf() {
		return cpf;
	}

	public void setCurso(Curso curso) {
		if (curso == null) {
			throw new IllegalArgumentException("Curso deve ser definido!");
		}
		this.curso = curso;
	}
}
