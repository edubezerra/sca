package br.cefetrj.sca.dominio;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import br.cefetrj.sca.dominio.avaliacaoturma.Quesito;

public class FormularioAvaliacaoTurma {

	@Id
	@GeneratedValue
	Long id;

	public Long getId() {
		return id;
	}

	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "FORMULARIO_ID", referencedColumnName = "ID")
	List<Quesito> quesitos;

	public void adicionarQuesito(Quesito quesito) {
		this.quesitos.add(quesito);
	}
}
