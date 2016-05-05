package br.cefetrj.sca.dominio.matriculaforaprazo;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import br.cefetrj.sca.dominio.Departamento;
import br.cefetrj.sca.dominio.Disciplina;

@Entity
public class AlocacacaoDisciplinasEmDepartamento {
	@Id
	@GeneratedValue
	Long id;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "ALOCACAO_DEPTO_ID", referencedColumnName = "ID")
	Set<Disciplina> disciplinas = new HashSet<>();

	@OneToOne
	Departamento departamento;

	public AlocacacaoDisciplinasEmDepartamento() {
	}

	public AlocacacaoDisciplinasEmDepartamento(Departamento depto) {
		this.departamento = depto;
	}

	public Long getId() {
		return id;
	}

	public Departamento getDepartamento() {
		return departamento;
	}
	
	public void alocarDisciplina(Disciplina disciplina){
		this.disciplinas.add(disciplina);
	}
	
	public Set<Disciplina> getDisciplinas() {
		return disciplinas;
	}
}
