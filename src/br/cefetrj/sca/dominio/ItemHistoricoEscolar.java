package br.cefetrj.sca.dominio;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class ItemHistoricoEscolar {

	@Id
	@GeneratedValue
	Long id;

	@OneToOne
	Disciplina disciplina;

	@OneToOne
	SemestreLetivo periodo;

	@Enumerated(EnumType.ORDINAL)
	EnumSituacaoFinalAvaliacao situacao;

	public ItemHistoricoEscolar(Disciplina disciplina, SemestreLetivo periodo,
			EnumSituacaoFinalAvaliacao situacao) {
		this.disciplina = disciplina;
		this.periodo = periodo;
		this.situacao = situacao;
	}

	public Long getId() {
		return id;
	}
	
}
