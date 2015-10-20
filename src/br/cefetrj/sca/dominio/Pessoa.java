package br.cefetrj.sca.dominio;

import java.util.Date;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.cefetrj.sca.dominio.contas.Email;

@Embeddable
public class Pessoa {

	private String nome;

	@Embedded
	private Email email;

	@Embedded
	private Endereco endereco;

	@Temporal(TemporalType.TIMESTAMP)
	private Date dataNascimento;

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

}
