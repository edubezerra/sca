package br.cefetrj.sca.dominio;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.cefetrj.sca.dominio.matriculaforaprazo.Comprovante;

@Entity
public class DocumentoProfessor {

	@Id
	@GeneratedValue
	private Long id;
	
	/**
	 * O nome do documento.
	 */
	private String nome;
	
	/**
	 * A categoria do documento.
	 * (eg. Diploma Graduação)
	 */
	private String categoria;
	
	/**
	 * Data do upload.
	 */
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataUpload;
	
	/**
	 * O documento.
	 */
	@OneToOne(cascade = CascadeType.ALL)
	Comprovante documento;
	
	@ManyToOne
	private Professor professor;
	
	
	public Professor getProfessor() {
		return professor;
	}

	public void setProfessor(Professor professor) {
		this.professor = professor;
	}

	public Date getDataUpload() {
		return dataUpload;
	}

	public void setDataUpload(Date dataUpload) {
		this.dataUpload = dataUpload;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	public Comprovante getDocumento() {
		return documento;
	}

	public void setDocumento(Comprovante documento) {
		this.documento = documento;
	}

	public Long getId() {
		return id;
	}
	
	@SuppressWarnings("unused")
	private DocumentoProfessor(){}
	
	public DocumentoProfessor(String nome, String categoria, Comprovante documento, Date dataUpload, Professor professor) throws IllegalArgumentException{
		if (dataUpload == null)
			throw new IllegalArgumentException("Data da solicitação não pode ser nula.");
		if(nome == null || nome.length() < 5)
			throw new IllegalArgumentException("Nome não pode ser nulo e deve conter pelo menos 5 caracteres.");
		if(categoria == null)
			throw new IllegalArgumentException("Categoria não pode ser nula.");
		if (documento == null)
			throw new IllegalArgumentException("Documento não pode ser nulo.");
		if(professor == null)
			throw new IllegalArgumentException("Professor não pode ser nulo.");
			
		this.nome = nome;
		this.categoria = categoria;
		this.documento = documento;
		this.dataUpload = dataUpload;
		this.professor = professor;
	}
	
}
