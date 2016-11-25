package br.cefetrj.sca.dominio;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import br.cefetrj.sca.dominio.avaliacaoturma.Quesito;

@Entity
public class PesquisaAvaliacao {

	@Id
	@GeneratedValue
	Long id;

	String descritor;

	String nome;

	public Long getId() {
		return id;
	}

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "FORMULARIO_ID", referencedColumnName = "ID")
	List<Quesito> quesitos = new ArrayList<>();

	public PesquisaAvaliacao() {
	}

	public PesquisaAvaliacao(String descritor, String nome) {
		this.descritor = descritor;
		this.nome = nome;
	}

	public void adicionarQuesito(Quesito quesito) {
		if (quesitos == null) {
			quesitos = new ArrayList<Quesito>();
		}
		this.quesitos.add(quesito);
	}

	public String getNome() {
		return nome;
	}

	public String getDescritor() {
		return descritor;
	}

	public List<Quesito> getQuesitos() {
		return quesitos;
	}
}
