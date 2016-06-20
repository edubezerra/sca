package br.cefetrj.sca.dominio;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class BlocoEquivalencia {

	@Id
	@GeneratedValue
	Long id;

	@OneToMany
	Set<Disciplina> disciplinasOriginais;

	@OneToMany
	Set<Disciplina> disciplinasEquivalentes;

	public BlocoEquivalencia() {
	}
}
