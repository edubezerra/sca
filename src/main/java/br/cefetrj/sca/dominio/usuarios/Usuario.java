/**
 * 
 */
package br.cefetrj.sca.dominio.usuarios;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name = "USERS")
public class Usuario {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	private String nome;

	@Column(nullable = false, unique = true)
	private String login;

	private Date dob;

	@NotEmpty
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "USER_USER_PROFILE", joinColumns = { @JoinColumn(name = "USER_ID") }, inverseJoinColumns = {
			@JoinColumn(name = "USER_PROFILE_ID") })
	private Set<PerfilUsuario> userProfiles = new HashSet<PerfilUsuario>();

	// @NotEmpty
	// @Column(name = "EMAIL", nullable = false)
	private String email;

	private String matricula;

	public Usuario() {
	}

	public Usuario(String nome, String login, String matricula, String email, Date dob) {
		if (login == null || login.isEmpty()) {
			throw new IllegalArgumentException("Login do usuário deve ser fornecido!");
		}
		if (nome == null || nome.isEmpty()) {
			throw new IllegalArgumentException("Nome do usuário deve ser fornecido!");
		}
		this.nome = nome;
		this.login = login;
		this.matricula = matricula;
		this.email = email;
		this.dob = dob;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", nome=" + nome + ", login=" + login + ", dob=" + dob + "]";
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}

	public Set<PerfilUsuario> getUserProfiles() {
		return userProfiles;
	}

	public void setUserProfiles(Set<PerfilUsuario> userProfiles) {
		this.userProfiles = userProfiles;
	}

	public String getMatricula() {
		return matricula;
	}

	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}
}
