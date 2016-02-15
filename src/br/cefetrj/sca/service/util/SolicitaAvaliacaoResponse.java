package br.cefetrj.sca.service.util;

import java.util.ArrayList;

import br.cefetrj.sca.dominio.PeriodoLetivo;
import br.cefetrj.sca.dominio.Turma;

@SuppressWarnings("serial")
public class SolicitaAvaliacaoResponse extends
		ArrayList<SolicitaAvaliacaoResponse.Item> {

	public class Item {
		private Long idTurma;
		private String codigoTurma;
		private String nomeDisciplina;
		private boolean isAvaliada;
		private PeriodoLetivo periodoLetivo;

		public Item(Long idTurma, String codigoTurma, String nomeDisciplina,
				PeriodoLetivo periodo, boolean isAvaliada) {
			this.idTurma = idTurma;
			this.codigoTurma = codigoTurma;
			this.nomeDisciplina = nomeDisciplina;
			this.periodoLetivo = periodo;
			this.isAvaliada = isAvaliada;
		}

		public Long getIdTurma() {
			return idTurma;
		}

		public String getCodigoTurma() {
			return codigoTurma;
		}

		public String getNomeDisciplina() {
			return nomeDisciplina;
		}

		public boolean getIsAvaliada() {
			return isAvaliada;
		}

		public PeriodoLetivo getPeriodoLetivo() {
			return periodoLetivo;
		}
	}

	public void add(Turma turma, boolean turmaAvaliada) {
		Item novo = this.new Item(turma.getId(), turma.getCodigo(),
				turma.getNomeDisciplina(), turma.getPeriodo(), turmaAvaliada);
		super.add(novo);
	}
}
