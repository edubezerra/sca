package br.cefetrj.sca.dominio;

import java.util.Date;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.cefetrj.sca.dominio.contas.Email;

@Entity
public class Aluno {

	private static final int TAM_MATRICULA = 10;

	@Id
	@GeneratedValue
	private Long id;

	/**
	 * Nome do aluno.
	 */
	private String nome;

	/**
	 * Matrícula do aluno, composta de <code>TAM_MATRICULA</code> carateres.
	 */
	private String matricula;

	@Embedded
	private Email email;

	@Temporal(TemporalType.TIMESTAMP)
	private Date dataNascimento;

	public Long getId() {
		return id;
	}

	@SuppressWarnings("unused")
	private Aluno() {
	}

	public Aluno(String nome, String matricula) {
		super();
		if (nome == null || nome.equals("")) {
			throw new IllegalArgumentException("Nome não pode ser vazio.");
		}
		if (matricula == null || matricula.equals("")) {
			throw new IllegalArgumentException("Matrícula não pode ser vazia.");
		}
		if (matricula.length() != TAM_MATRICULA) {
			throw new IllegalArgumentException("Matrícula deve ter "
					+ TAM_MATRICULA + " caracteres: " + matricula);
		}
		this.nome = nome;
		this.matricula = matricula;
	}

	public Aluno(String nome, String matricula, Date dataNascimento,
			String enderecoEmail) {
		this(nome, matricula);
		this.dataNascimento = dataNascimento;
		this.email = new Email(enderecoEmail);
	}

	public String getNome() {
		return nome;
	}

	public Date getDataNascimento() {
		return dataNascimento;
	}

	public String getMatricula() {
		return matricula;
	}

	public String getEmail() {
		return email.getEndereco();
	}

}
