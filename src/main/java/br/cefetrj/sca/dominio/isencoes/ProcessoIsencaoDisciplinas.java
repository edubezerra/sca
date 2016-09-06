package br.cefetrj.sca.dominio.isencoes;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import br.cefetrj.sca.dominio.Aluno;
import br.cefetrj.sca.dominio.Disciplina;

@Entity
public class ProcessoIsencaoDisciplinas {
	@Id
	@GeneratedValue
	private Long id;

	/**
	 * O aluno solicitante.
	 */
	@OneToOne
	Aluno aluno;

	private String situacao;

	private Date dataRegistro;

	@OneToMany(cascade = CascadeType.ALL)
	List<ItemIsencaoDisciplina> itens = new ArrayList<>();

	@SuppressWarnings("unused")
	private ProcessoIsencaoDisciplinas() {
	}

	public ProcessoIsencaoDisciplinas(Aluno aluno) {
		this.aluno = aluno;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getDataRegistro() {
		return dataRegistro;
	}

	public void setDataRegistro(Date dataRegistro) {
		this.dataRegistro = dataRegistro;
	}

	public List<ItemIsencaoDisciplina> getItens() {
		return itens;
	}

	public String getSituacao() {
		int contador = 0;
		for (int i = 0; i < this.getItens().size(); i++) {
			if (this.getItens().get(i).getSituacao() != null) {
				contador = contador + 1;
			}
		}
		if (this.getItens().size() == contador) {
			this.setSituacao("ANALISADO");
		} else {
			this.setSituacao("EM ANALISE");
		}
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	public Aluno getAluno() {
		return aluno;
	}

	public void comMaisUmItem(Disciplina disciplina) {
		ItemIsencaoDisciplina item = new ItemIsencaoDisciplina(disciplina);
		this.itens.add(item);
	}
}
