package br.cefetrj.sca.dominio;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

@Entity
public class HistoricoEscolar {

	@Id
	@GeneratedValue
	Long id;

	public Long getId() {
		return id;
	}

	@OneToMany(fetch = FetchType.EAGER, cascade=CascadeType.ALL)
	@JoinColumn(name = "HISTORICO_ESCOLAR_ID", referencedColumnName = "ID")
	Set<ItemHistoricoEscolar> itens = new HashSet<>();

	public HistoricoEscolar() {

	}

	public List<Disciplina> getDisciplinasPossiveis() {
		// TODO Auto-generated method stub
		return null;
	}

	public void lancarAprovacao(Disciplina d, NotaFinal nf, SemestreLetivo sl) {
		ItemHistoricoEscolar item = new ItemHistoricoEscolar(d, sl,
				nf.getSituacaoFinal());
		this.itens.add(item);
	}

	public void lancarReprovacaoPorMedia(Disciplina d, NotaFinal nf,
			SemestreLetivo sl) {

	}

	public void lancar(Disciplina disciplina,
			EnumSituacaoFinalAvaliacao situacao, SemestreLetivo periodo) {
		ItemHistoricoEscolar item = new ItemHistoricoEscolar(disciplina, periodo,
				situacao);
		this.itens.add(item);
	}
}
