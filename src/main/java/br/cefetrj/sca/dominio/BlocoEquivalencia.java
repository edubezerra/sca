package br.cefetrj.sca.dominio;

import java.util.Collections;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

@Entity
public class BlocoEquivalencia {

	@Id
	@GeneratedValue
	Long id;

	@ManyToMany(cascade = CascadeType.MERGE)
	@JoinTable(name = "DISCIPLINAS_ORIGINAIS", joinColumns = { @JoinColumn(name = "BLOCO_ID", referencedColumnName = "ID", nullable = true) }, inverseJoinColumns = { @JoinColumn(name = "DISCIPLINASORIGINAIS_ID", referencedColumnName = "ID") })
	Set<Disciplina> disciplinasOriginais;

	@ManyToMany(cascade = CascadeType.MERGE)
	@JoinTable(name = "DISCIPLINAS_EQUIVALENTES", joinColumns = { @JoinColumn(name = "BLOCO_ID", referencedColumnName = "ID", nullable = true) }, inverseJoinColumns = { @JoinColumn(name = "DISCIPLINASEQUIVALENTES_ID", referencedColumnName = "ID") })
	Set<Disciplina> disciplinasEquivalentes;

	@SuppressWarnings("unused")
	private BlocoEquivalencia() {
	}
	
	public BlocoEquivalencia(Set<Disciplina> disciplinasOriginais, Set<Disciplina> disciplinasEquivalentes) {
		this.disciplinasOriginais = disciplinasOriginais;
		this.disciplinasEquivalentes = disciplinasEquivalentes;
	}

	public Long getId() {
		return id;
	}

	public Set<Disciplina> getDisciplinasOriginais() {
		return Collections.unmodifiableSet(disciplinasOriginais);
	}

	public Set<Disciplina> getDisciplinasEquivalentes() {
		return Collections.unmodifiableSet(disciplinasEquivalentes);
	}
	
	
}
