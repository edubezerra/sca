package br.cefetrj.sca.dominio.inclusaodisciplina;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import br.cefetrj.sca.dominio.Aluno;
import br.cefetrj.sca.dominio.SemestreLetivo;

@Entity
public class SolicitacaoMatriculaForaPrazo {

	@Id
	@GeneratedValue
	private Long id;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "SOLICITACAO_INCLUSAO_ID", referencedColumnName = "ID")
	private List<ItemSolicitacaoMatriculaForaPrazo> itensSolicitacao;

	@ManyToOne
	@JoinColumn(nullable = false)
	private Aluno aluno;

	private SemestreLetivo semestreLetivo;

	@SuppressWarnings("unused")
	private SolicitacaoMatriculaForaPrazo() {
	}

	public SolicitacaoMatriculaForaPrazo(List<ItemSolicitacaoMatriculaForaPrazo> itemSolicitacao, Aluno aluno, SemestreLetivo semestreLetivo) {
		if (itemSolicitacao == null || aluno == null || semestreLetivo == null) {
			throw new IllegalArgumentException("Erro: argumentos inválidos para SolicitacaoInclusao().");
		}
		this.itensSolicitacao = new ArrayList<>();
		this.itensSolicitacao.addAll(itemSolicitacao);
		this.aluno = aluno;
		this.semestreLetivo = semestreLetivo;
	}

	public Long getId() {
		return id;
	}

	public List<ItemSolicitacaoMatriculaForaPrazo> getItensSolicitacao() {
		return itensSolicitacao;
	}

	public Aluno getAluno() {
		return aluno;
	}

	public SemestreLetivo getSemestreLetivo() {
		return semestreLetivo;
	}

	public void addItemSolicitacao(List<ItemSolicitacaoMatriculaForaPrazo> itens) {
		this.itensSolicitacao.addAll(itens);
	}

	/**
	 * Dada uma lista de objetos <code>SolicitacaoInclusao</code>, contrói o
	 * conjunto de objetos <code>SemestreLetivo</code> correspondentes.
	 * 
	 * @param solicitacoes
	 * 
	 * @return conjunto de objetos <code>SemestreLetivo</code>.
	 */
	public static List<SemestreLetivo> semestresCorrespondentes(List<SolicitacaoMatriculaForaPrazo> solicitacoes) {
		List<SemestreLetivo> result = new ArrayList<>();
		HashSet<SemestreLetivo> set = new HashSet<>();
		for (SolicitacaoMatriculaForaPrazo item : solicitacoes) {
			SemestreLetivo semestre = item.getSemestreLetivo();
			if (!set.contains(semestre)) {
				result.add(semestre);
				set.add(semestre);
			}
		}
		return result;
	}
}
