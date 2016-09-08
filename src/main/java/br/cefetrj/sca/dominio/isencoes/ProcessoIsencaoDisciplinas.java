package br.cefetrj.sca.dominio.isencoes;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
	public enum Situacao {
		EM_PREPARACAO("EM PREPARAÇÃO"), SUBMETIDO("SUBMETIDO"), ANALISADO(
				"ANALISADO");

		private String value;

		private Situacao(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}
	}

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
		this.situacao = "EM PREPARAÇÃO";
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

//	public void setDataRegistro(Date dataRegistro) {
//		this.dataRegistro = dataRegistro;
//	}

	public List<ItemIsencaoDisciplina> getItens() {
		return itens;
	}

	/**
	 * 
	 * @return situação do processo de isenção ("EM PREPARAÇÃO", "SUBMETIDO" ou
	 *         "ANALISADO")
	 */
	public String getSituacao() {
		if (this.situacao.equals("EM PREPARAÇÃO")) {
			return this.situacao;
		}
		for (int i = 0; i < this.getItens().size(); i++) {
			if (this.getItens().get(i).getSituacao().equals("INDEFINIDO")) {
				return "SUBMETIDO";
			}
		}
		return "ANALISADO";
	}

//	public void setSituacao(String situacao) {
//		this.situacao = situacao;
//	}

	public void submeterParaAnalise() {
		if (!this.situacao.equals("EM PREPARAÇÃO"))
			throw new IllegalStateException("Apenas pedidos em preparação podem ser submetidos para análise.");
		this.situacao = "SUBMETIDO";
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			this.dataRegistro = sdf.parse(sdf.format(new Date()));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Aluno getAluno() {
		return aluno;
	}

	public void comMaisUmItem(Disciplina disciplina) {
		ItemIsencaoDisciplina item = new ItemIsencaoDisciplina(disciplina);
		this.itens.add(item);
	}
	
	public void deferirItem(Disciplina disciplina) {
		for (int i = 0; i < this.getItens().size(); i++) {
			if (this.getItens().get(i).getDisciplina().equals(disciplina)) {
				this.getItens().get(i).deferir();
			}
		}
	}
	
	public void indeferirItem(Disciplina disciplina, String observacao) {
		for (int i = 0; i < this.getItens().size(); i++) {
			if (this.getItens().get(i).getDisciplina().equals(disciplina)) {
				this.getItens().get(i).indeferir(observacao);
			}
		}
	}
}
