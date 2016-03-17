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
import br.cefetrj.sca.dominio.EnumStatusSolicitacao;
import br.cefetrj.sca.dominio.PeriodoLetivo;

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

	private PeriodoLetivo semestreLetivo;

	@SuppressWarnings("unused")
	private SolicitacaoMatriculaForaPrazo() {
	}

	public SolicitacaoMatriculaForaPrazo(
			List<ItemSolicitacaoMatriculaForaPrazo> itemSolicitacao,
			Aluno aluno, PeriodoLetivo semestreLetivo) {
		if (itemSolicitacao == null || aluno == null || semestreLetivo == null) {
			throw new IllegalArgumentException(
					"Erro: argumentos inválidos para SolicitacaoInclusao().");
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

	public PeriodoLetivo getSemestreLetivo() {
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
	public static List<PeriodoLetivo> semestresCorrespondentes(
			List<SolicitacaoMatriculaForaPrazo> solicitacoes) {
		List<PeriodoLetivo> result = new ArrayList<>();
		HashSet<PeriodoLetivo> set = new HashSet<>();
		for (SolicitacaoMatriculaForaPrazo item : solicitacoes) {
			PeriodoLetivo semestre = item.getSemestreLetivo();
			if (!set.contains(semestre)) {
				result.add(semestre);
				set.add(semestre);
			}
		}
		return result;
	}

	public void definirStatusItem(Long idItemSolicitacao, String status) {
		ItemSolicitacaoMatriculaForaPrazo itemSolicitacao = null;
		for (ItemSolicitacaoMatriculaForaPrazo item : itensSolicitacao) {
			if (item.getId().equals(idItemSolicitacao)) {
				itemSolicitacao = item;
				break;
			}
		}
		if (itemSolicitacao != null) {
			if (status.equalsIgnoreCase("deferido")) {
				itemSolicitacao.setStatus(EnumStatusSolicitacao.DEFERIDO);
			} else if (status.equalsIgnoreCase("indeferido")) {
				itemSolicitacao.setStatus(EnumStatusSolicitacao.INDEFERIDO);
			}
		}
	}
}
