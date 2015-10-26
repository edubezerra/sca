package br.cefetrj.sca.dominio;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class HistoricoEscolar {

	@Id
	@GeneratedValue
	Long id;
	
	public Long getId() {
		return id;
	}

	private HistoricoEscolar(){
		
	}

	public List<Disciplina> getDisciplinasPossiveis() {
		// TODO Auto-generated method stub
		return null;
	}

}
