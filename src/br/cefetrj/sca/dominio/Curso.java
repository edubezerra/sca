package br.cefetrj.sca.dominio;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.UniqueConstraint;

import org.apache.commons.lang3.StringUtils;

@Entity
public class Curso {

	@Id
	@GeneratedValue
	Long id;

	String sigla;

	String nome;

//	@OneToMany(cascade = CascadeType.ALL)
//	@JoinColumn(name = "CURSO_ID", referencedColumnName = "ID")
//	List<VersaoGradeCurso> versoesGrade = new ArrayList<VersaoGradeCurso>();

	private Curso() {
	}

	public Curso(String siglaCurso, String nomeCurso) {
		this.sigla = siglaCurso;
		this.nome = nomeCurso;
	}

//	public void registrarVersao(String versaoStr) {
//		VersaoGradeCurso versao = new VersaoGradeCurso(versaoStr);
//		if (this.versoesGrade.contains(versao)) {
//			throw new IllegalStateException(
//					"Versão de grade curricular com identificador " + versaoStr + " já existe no curso " + this.nome);
//		}
//		this.versoesGrade.add(versao);
//	}

//	public VersaoGradeCurso getVersao(String numVersao) {
//		if (StringUtils.isBlank(numVersao)) {
//			return null;
//		}
//		for (VersaoGradeCurso versaoGradeCurso : this.versoesGrade) {
//			if (versaoGradeCurso.getNumero().equals(numVersao)) {
//				return versaoGradeCurso;
//			}
//		}
//		return null;
//	}

	public String getSigla() {
		return sigla;
	}
	
	public String getNome() {
		return nome;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((sigla == null) ? 0 : sigla.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Curso other = (Curso) obj;
		if (sigla == null) {
			if (other.sigla != null)
				return false;
		} else if (!sigla.equals(other.sigla))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Curso [sigla=" + sigla + ", nome=" + nome + "]";
	}

	
	
}
