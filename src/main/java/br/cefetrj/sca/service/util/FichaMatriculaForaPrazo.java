package br.cefetrj.sca.service.util;

import java.util.ArrayList;
import java.util.List;

import br.cefetrj.sca.dominio.Aluno;
import br.cefetrj.sca.dominio.Departamento;
import br.cefetrj.sca.dominio.inclusaodisciplina.Comprovante;
import br.cefetrj.sca.dominio.inclusaodisciplina.ItemMatriculaForaPrazo;

public class FichaMatriculaForaPrazo {
	Aluno aluno;

	String observacoes;

	public class ItemRequerimentoInfo {
		String nomeDepartamento;
		int opcao;
		private String codigoTurma;
		private String codigoDisciplina;
		private String nomeDisciplina;
		private String descritorTurma;

		public String getDescritorTurma() {
			return descritorTurma;
		}
		
		public ItemRequerimentoInfo(String codigoTurma, String codigoDisciplina, String nomeDisciplina,
				String nomeDepartamento, int opcao) {
			super();
			this.codigoTurma = codigoTurma;
			this.codigoDisciplina = codigoDisciplina;
			this.nomeDisciplina = nomeDisciplina;
			this.descritorTurma = codigoTurma + ";" + codigoDisciplina + ";" + nomeDisciplina;
			this.nomeDepartamento = nomeDepartamento;
			this.opcao = opcao;
		}

		public String getCodigoDisciplina() {
			return codigoDisciplina;
		}

		public String getCodigoTurma() {
			return codigoTurma;
		}

		public String getNomeDepartamento() {
			return nomeDepartamento;
		}

		public int getOpcao() {
			return opcao;
		}

		public String getNomeDisciplina() {
			return nomeDisciplina;
		}
	}

	private List<ItemRequerimentoInfo> itensRequerimentos = new ArrayList<>();

	public class DepartamentoInfo {
		private String nomeDepartamento;
		private String codigoDepartamento;

		public DepartamentoInfo(String nomeDepartamento, String codigoDepartamento) {
			this.codigoDepartamento = codigoDepartamento;
			this.nomeDepartamento = nomeDepartamento;
		}

		public String getNomeDepartamento() {
			return nomeDepartamento;
		}

		public String getCodigoDepartamento() {
			return codigoDepartamento;
		}
	}

	private List<DepartamentoInfo> departamentos = new ArrayList<>();

	private Comprovante comprovante;

	public List<DepartamentoInfo> getDepartamentos() {
		return departamentos;
	}

	public void comDepartamentos(List<Departamento> departamentos) {
		for (Departamento depto : departamentos) {
			this.departamentos.add(new DepartamentoInfo(depto.getNome(), depto.getId().toString()));
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

	public void adicionarItemRequerimento(String descritorTurma, String nomeDepartamento, int opcao) {
		String[] componentes = descritorTurma.split(";");
		String codigoTurma = componentes[0];
		String codigoDisciplina = componentes[1];
		String nomeDisciplina = componentes[2];
		if (isSolicitacaoRepetida(codigoTurma, codigoDisciplina)) {
			throw new IllegalArgumentException(
					"Erro: já existe uma solicitação para a turma/disciplina " + codigoTurma + "/" + codigoDisciplina);
		} else {
			ItemRequerimentoInfo item = new ItemRequerimentoInfo(codigoTurma, codigoDisciplina, nomeDisciplina,
					nomeDepartamento, opcao);
			itensRequerimentos.add(item);
		}
	}

	public List<ItemRequerimentoInfo> getItensRequerimentos() {
		return itensRequerimentos;
	}

	public void comSolicitacoes(final List<ItemMatriculaForaPrazo> itensSolicitacao) {
		for (ItemMatriculaForaPrazo item : itensSolicitacao) {
			String nomeDepartamento = item.getDepartamento().getNome();
			int opcao = item.getOpcao();
			String nomeDisciplina = item.getTurma().getNomeDisciplina();
			String codigoDisciplina = item.getTurma().getDisciplina().getCodigo();
			String codigoTurma = item.getTurma().getCodigo();
			ItemRequerimentoInfo itemreq = new ItemRequerimentoInfo(codigoTurma, codigoDisciplina, nomeDisciplina,
					nomeDepartamento, opcao);
			itensRequerimentos.add(itemreq);
		}
	}

	public boolean isSolicitacaoRepetida(String codigoTurma, String codigoDisciplina) {
		for (ItemRequerimentoInfo item : itensRequerimentos) {
			if (item.getCodigoTurma().equals(codigoTurma) && item.getCodigoDisciplina().equals(codigoDisciplina))
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

	public void setObservacoes(String observacoes) {
		this.observacoes = observacoes;
	}

	public String getObservacoes() {
		return this.observacoes;
	}
}
