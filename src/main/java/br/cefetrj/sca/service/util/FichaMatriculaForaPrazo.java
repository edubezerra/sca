package br.cefetrj.sca.service.util;

import java.util.ArrayList;
import java.util.List;

import br.cefetrj.sca.dominio.Aluno;
import br.cefetrj.sca.dominio.Departamento;
import br.cefetrj.sca.dominio.PeriodoLetivo;
import br.cefetrj.sca.dominio.matriculaforaprazo.Comprovante;
import br.cefetrj.sca.dominio.matriculaforaprazo.ItemMatriculaForaPrazo;

public class FichaMatriculaForaPrazo {
	Aluno aluno;

	public class ItemRequerimentoInfo {
		String siglaDepartamento;
		int opcao;
		private Long idTurma;
		private String codigoTurma;
		private String codigoDisciplina;
		private String nomeDisciplina;
		private String observacao;

		public ItemRequerimentoInfo(Long idTurma, String codigoTurma,
				String codigoDisciplina, String nomeDisciplina,
				String siglaDepartamento, int opcao, String observacao) {
			super();
			this.idTurma = idTurma;
			this.codigoTurma = codigoTurma;
			this.codigoDisciplina = codigoDisciplina;
			this.nomeDisciplina = nomeDisciplina;
			this.siglaDepartamento = siglaDepartamento;
			this.opcao = opcao;
			this.observacao = observacao;
		}

		public String getCodigoDisciplina() {
			return codigoDisciplina;
		}

		public Long getIdTurma() {
			return idTurma;
		}

		public String getCodigoTurma() {
			return codigoTurma;
		}

		public String getSiglaDepartamento() {
			return siglaDepartamento;
		}

		public int getOpcao() {
			return opcao;
		}

		public String getNomeDisciplina() {
			return nomeDisciplina;
		}

		public void setObservacao(String observacao) {
			this.observacao = observacao;
		}

		public String getObservacao() {
			return this.observacao;
		}

	}

	private List<ItemRequerimentoInfo> itensRequerimento = new ArrayList<>();

	public class DepartamentoInfo {
		private String siglaDepartamento;
		private String idDepartamento;

		public DepartamentoInfo(String siglaDepartamento,
				String idDepartamento) {
			this.idDepartamento = idDepartamento;
			this.siglaDepartamento = siglaDepartamento;
		}

		public String getSiglaDepartamento() {
			return siglaDepartamento;
		}

		public String getCodigoDepartamento() {
			return idDepartamento;
		}
	}

	private List<DepartamentoInfo> departamentos = new ArrayList<>();

	private Comprovante comprovante;

	public List<DepartamentoInfo> getDepartamentos() {
		return departamentos;
	}

	public void comDepartamentos(List<Departamento> departamentos) {
		for (Departamento depto : departamentos) {
			this.departamentos.add(new DepartamentoInfo(depto.getSigla(), depto
					.getId().toString()));
		}
	}

	public String getMatriculaAluno() {
		return aluno.getMatricula();
	}

	public void comAluno(Aluno aluno) {
		this.aluno = aluno;
	}

	public Aluno getAluno() {
		return aluno;
	}

	public void adicionarItemRequerimento(Long idTurma, String codigoTurma,
			String codigoDisciplina, String nomeDisciplina,
			String siglaDepartamento, int opcao, String observacao) {
		if (isSolicitacaoRepetida(codigoTurma, codigoDisciplina)) {
			throw new IllegalArgumentException(
					"Erro: já existe uma solicitação para a turma/disciplina "
							+ codigoTurma + "/" + codigoDisciplina);
		} else {
			ItemRequerimentoInfo item = new ItemRequerimentoInfo(idTurma,
					codigoTurma, codigoDisciplina, nomeDisciplina,
					siglaDepartamento, opcao, observacao);
			itensRequerimento.add(item);
		}
	}

	public void removerItemRequerimento(long idTurma) {
		for (ItemRequerimentoInfo item : itensRequerimento) {
			if (item.getIdTurma().equals(idTurma)) {
				itensRequerimento.remove(item);
				return;
			}
		}
	}

	public List<ItemRequerimentoInfo> getItensRequerimento() {
		return itensRequerimento;
	}

	public void comSolicitacoes(
			final List<ItemMatriculaForaPrazo> itensSolicitacao) {
		for (ItemMatriculaForaPrazo item : itensSolicitacao) {
			String siglaDepartamento = item.getDepartamento().getSigla();
			int opcao = item.getOpcao();
			String nomeDisciplina = item.getTurma().getNomeDisciplina();
			String codigoDisciplina = item.getTurma().getDisciplina()
					.getCodigo();
			String codigoTurma = item.getTurma().getCodigo();
			ItemRequerimentoInfo itemreq = new ItemRequerimentoInfo(item
					.getTurma().getId(), codigoTurma, codigoDisciplina,
					nomeDisciplina, siglaDepartamento, opcao,
					item.getObservacao());
			itensRequerimento.add(itemreq);
		}
	}

	public boolean isSolicitacaoRepetida(String codigoTurma,
			String codigoDisciplina) {
		for (ItemRequerimentoInfo item : itensRequerimento) {
			if (item.getCodigoTurma().equals(codigoTurma)
					&& item.getCodigoDisciplina().equals(codigoDisciplina))
				return true;
		}
		return false;
	}

	public void setComprovante(String contentType, byte[] data, String nome) {
		this.comprovante = new Comprovante(contentType, data, nome);
	}

	public Comprovante getComprovante() {
		return this.comprovante;
	}

	public PeriodoLetivo getPeriodoLetivoCorrente() {
		return PeriodoLetivo.PERIODO_CORRENTE;
	}
}
