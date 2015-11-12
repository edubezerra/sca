package br.cefetrj.sca.dominio;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.springframework.beans.factory.annotation.Autowired;

import br.cefetrj.sca.infra.DisciplinaRepositorio;

@Entity
public class HistoricoEscolar {

	@Id
	@GeneratedValue
	Long id;

	public Long getId() {
		return id;
	}

	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "HISTORICO_ESCOLAR_ID", referencedColumnName = "ID")
	Set<ItemHistoricoEscolar> itens = new HashSet<>();

	@ManyToOne
	private VersaoCurso versaoCurso;

	@SuppressWarnings("unused")
	private HistoricoEscolar() {
	}

	public HistoricoEscolar(VersaoCurso versaoCurso) {
		this.versaoCurso = versaoCurso;
	}

	@Transient
	@Autowired
	DisciplinaRepositorio dr;

	public List<Disciplina> getDisciplinasPossiveis() {

		List<Disciplina> disciplinas = dr.getDisciplinasPorVersaoCurso(this.versaoCurso);

		List<Disciplina> disciplinasCursadas = new ArrayList<Disciplina>();

		Iterator<ItemHistoricoEscolar> it = itens.iterator();
		EnumSituacaoFinalAvaliacao aprovado = EnumSituacaoFinalAvaliacao.APROVADO;
		EnumSituacaoFinalAvaliacao isento = EnumSituacaoFinalAvaliacao.ISENTO;
		EnumSituacaoFinalAvaliacao isentoTransf = EnumSituacaoFinalAvaliacao.ISENTO_POR_TRANSFERENCIA;
		EnumSituacaoFinalAvaliacao creditos = EnumSituacaoFinalAvaliacao.APROVEITAMENTO_CREDITOS;
		EnumSituacaoFinalAvaliacao estudos = EnumSituacaoFinalAvaliacao.APROVEITAMENTO_POR_ESTUDOS;

		while (it.hasNext()) {
			ItemHistoricoEscolar item = it.next();
			if (item.getSituacao() == aprovado || item.getSituacao() == isento
					|| item.getSituacao() == isentoTransf
					|| item.getSituacao() == creditos
					|| item.getSituacao() == estudos) {

				disciplinasCursadas.add(item.getDisciplina());
				disciplinas.remove(item.getDisciplina());
			}
		}

		for (Disciplina disciplina : disciplinas) {
			if (!disciplinasCursadas.containsAll(disciplina.getPreRequisitos())) {
				disciplinas.remove(disciplina);
			}
		}
		return disciplinas;
	}

	public void lancar(Disciplina disciplina,
			EnumSituacaoFinalAvaliacao situacao, SemestreLetivo periodo) {
		ItemHistoricoEscolar item = new ItemHistoricoEscolar(disciplina,
				periodo, situacao);
		this.itens.add(item);
	}
}
