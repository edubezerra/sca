package br.cefetrj.sca.dominio;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Curso {

	@Id
	@GeneratedValue
	Long id;
	
	String sigla;
	
	String nome;
	
	List<VersaoGrade> versoesGrade;
}
