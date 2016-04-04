/**
 * 
 */
package br.cefetrj.sca.dominio.usuarios;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.validator.constraints.NotEmpty;

import br.cefetrj.sca.dominio.Role;

@Entity
@Table(name = "USERS")
public class Usuario {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	private String nome;

	@Column(nullable = false, unique = true)
	private String login;

	@Column(nullable = false)
	private String password;
	
	private Date dob;

	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "user_id")
	private Set<Role> roles = new HashSet<>();

	@NotEmpty
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "USER_USER_PROFILE", joinColumns = { @JoinColumn(name = "USER_ID") }, inverseJoinColumns = {
			@JoinColumn(name = "USER_PROFILE_ID") })
	private Set<PerfilUsuario> userProfiles = new HashSet<PerfilUsuario>();

	@NotEmpty
	@Column(name = "EMAIL", nullable = false)
	private String email;
	
	public Usuario() {
	}

	public Usuario(int id, String nome, String login, String password, String email, Date dob) {
		this.id = id;
		this.nome = nome;
		this.login = login;
		this.password = password;
		this.email = email;
		this.dob = dob;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", nome=" + nome + ", login=" + login
				+ ", dob=" + dob + "]";
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

	public void setNome(String name) {
		this.nome = name;
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public Set<PerfilUsuario> getUserProfiles() {
		return userProfiles;
	}

	public void setUserProfiles(Set<PerfilUsuario> userProfiles) {
		this.userProfiles = userProfiles;
	}
}
