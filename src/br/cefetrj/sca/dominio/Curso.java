package br.cefetrj.sca.dominio;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import org.apache.commons.lang3.StringUtils;

@Entity
public class Curso {

	@Id
	@GeneratedValue
	Long id;

	String sigla;

	String nome;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "CURSO_ID", referencedColumnName = "ID")
	List<VersaoGradeCurso> versoesGrade = new ArrayList<VersaoGradeCurso>();

	private Curso() {
	}

	public Curso(String siglaCurso, String nomeCurso) {
		this.sigla = siglaCurso;
		this.nome = nomeCurso;
	}

	public void registrarVersao(String versaoStr) {
		VersaoGradeCurso versao = new VersaoGradeCurso(versaoStr);
		if (this.versoesGrade.contains(versao)) {
			throw new IllegalStateException(
					"Versão de grade curricular com identificador " + versaoStr + " já existe no curso " + this.nome);
		}
		this.versoesGrade.add(versao);
	}

	public VersaoGradeCurso getVersao(String numVersao) {
		if (StringUtils.isBlank(numVersao)) {
			return null;
		}
		for (VersaoGradeCurso versaoGradeCurso : this.versoesGrade) {
			if (versaoGradeCurso.getNumero().equals(numVersao)) {
				return versaoGradeCurso;
			}
		}
		return null;
	}

	public String getSigla() {
		return sigla;
	}
	
	public String getNome() {
		return nome;
	}
}
