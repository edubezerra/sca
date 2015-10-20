package br.cefetrj.sca.dominio;

import java.util.Date;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.cefetrj.sca.dominio.contas.Email;
import br.com.caelum.stella.validation.CPFValidator;
import br.com.caelum.stella.validation.InvalidStateException;

@Embeddable
public class Pessoa {

	private String nome;

	@Embedded
	private Email email;

	@Embedded
	private Endereco endereco;

	@Temporal(TemporalType.TIMESTAMP)
	private Date dataNascimento;

	private String cpf;

	public Pessoa() {
	}

	public Pessoa(String nome) {
		this.nome = nome;
	}

	public Pessoa(String nome, Endereco endereco) {
		this(nome);
		this.endereco = endereco;
	}

	public Pessoa(String nome, Date dataNascimento, String enderecoEmail) {
		this.nome = nome;
		this.dataNascimento = dataNascimento;
		this.email = new Email(enderecoEmail);
	}

	/**
	 * 
	 * @param nome
	 *            nome completo.
	 * @param cpf
	 *            número do CPF.
	 * 
	 * @see <a
	 *      href="http://github.com/caelum/caelum-stella/wiki/Validadores-core">http://github.com/caelum/caelum-stella/wiki/Validadores-core</a>
	 */
	public Pessoa(String nome, String cpf) {
		this(nome);
		CPFValidator validator = new CPFValidator();
		try {
			validator.assertValid(cpf);
			this.cpf = cpf;
		} catch (InvalidStateException e) {
			throw new IllegalArgumentException("CPF é inválido!");
		}
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Endereco getEndereco() {
		return endereco;
	}

	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}

	public Date getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(Date dataNascimento) {
		this.dataNascimento = dataNascimento;
	}

	public Email getEmail() {
		return email;
	}

	public void setEmail(Email email) {
		this.email = email;
	}

	public String getCpf() {
		return cpf;
	}
}
