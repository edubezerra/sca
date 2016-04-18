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
import br.cefetrj.sca.dominio.Departamento;
import br.cefetrj.sca.dominio.EnumStatusSolicitacao;
import br.cefetrj.sca.dominio.PeriodoLetivo;
import br.cefetrj.sca.dominio.Turma;

@Entity
public class MatriculaForaPrazo {

	@Id
	@GeneratedValue
	private Long id;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "SOLICITACAO_INCLUSAO_ID", referencedColumnName = "ID")
	private List<ItemMatriculaForaPrazo> itensMatriculaForaPrazo;

	@ManyToOne
	@JoinColumn(nullable = false)
	private Aluno aluno;

	private PeriodoLetivo semestreLetivo;

	@SuppressWarnings("unused")
	private MatriculaForaPrazo() {
	}

	public MatriculaForaPrazo(List<ItemMatriculaForaPrazo> itemSolicitacao,
			Aluno aluno, PeriodoLetivo semestreLetivo) {
		if (itemSolicitacao == null || aluno == null || semestreLetivo == null) {
			throw new IllegalArgumentException(
					"Erro: argumentos inválidos para SolicitacaoInclusao().");
		}
		this.itensMatriculaForaPrazo = new ArrayList<>();
		this.itensMatriculaForaPrazo.addAll(itemSolicitacao);
		this.aluno = aluno;
		this.semestreLetivo = semestreLetivo;
	}

	public Long getId() {
		return id;
	}

	public List<ItemMatriculaForaPrazo> getItensSolicitacao() {
		return itensMatriculaForaPrazo;
	}

	public Aluno getAluno() {
		return aluno;
	}

	public PeriodoLetivo getSemestreLetivo() {
		return semestreLetivo;
	}

	public void addItensSolicitacao(List<ItemMatriculaForaPrazo> itens) {
		this.itensMatriculaForaPrazo.addAll(itens);
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
			List<MatriculaForaPrazo> solicitacoes) {
		List<PeriodoLetivo> result = new ArrayList<>();
		HashSet<PeriodoLetivo> set = new HashSet<>();
		for (MatriculaForaPrazo item : solicitacoes) {
			PeriodoLetivo semestre = item.getSemestreLetivo();
			if (!set.contains(semestre)) {
				result.add(semestre);
				set.add(semestre);
			}
		}
		return result;
	}

	public void definirStatusItem(Long idItemSolicitacao, String status) {
		ItemMatriculaForaPrazo itemSolicitacao = null;
		for (ItemMatriculaForaPrazo item : itensMatriculaForaPrazo) {
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

	public void addItem(Turma turma, Departamento departamento, int opcao) {
		ItemMatriculaForaPrazo item = new ItemMatriculaForaPrazo(turma,
				departamento, opcao);
		itensMatriculaForaPrazo.add(item);
	}
}
