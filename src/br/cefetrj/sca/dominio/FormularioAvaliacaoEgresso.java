package br.cefetrj.sca.dominio;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import br.cefetrj.sca.dominio.avaliacaoturma.Quesito;

@Entity
public class FormularioAvaliacaoEgresso {

	@Id
	@GeneratedValue
	private Long id;

	public Long getId() {
		return id;
	}

	public FormularioAvaliacaoEgresso(){
		
	}
	
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "FORMEGRESSO_ID", referencedColumnName = "ID")
	private List<Quesito> quesitos;

	public void adicionarQuesito(Quesito quesito) {
		if(quesitos == null){
			quesitos = new ArrayList<Quesito>();
		}
		quesitos.add(quesito);
	}
	
	
}
