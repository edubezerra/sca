package br.cefetrj.sca.service.util;

import java.util.ArrayList;
import java.util.List;

import br.cefetrj.sca.dominio.Aluno;
import br.cefetrj.sca.dominio.Departamento;
import br.cefetrj.sca.dominio.inclusaodisciplina.ItemMatriculaForaPrazo;

public class FichaMatriculaForaPrazo {
	Aluno aluno;

	public class DepartamentoInfo {
		private String nomeDepartamento;
		private String codigoDepartamento;

		public DepartamentoInfo(String nomeDepartamento,
				String codigoDepartamento) {
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

	private List<ItemMatriculaForaPrazo> solicitacoes = new ArrayList<>();

	public List<DepartamentoInfo> getDepartamentos() {
		return departamentos;
	}

	public void comDepartamentos(List<Departamento> departamentos) {
		for (Departamento depto : departamentos) {
			this.departamentos.add(new DepartamentoInfo(depto.getNome(), depto
					.getId().toString()));
		}
	}

	public void comSolicitacoes(List<ItemMatriculaForaPrazo> solicitacoes) {
		this.solicitacoes.addAll(solicitacoes);
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

	public List<ItemMatriculaForaPrazo> getSolicitacoes() {
		return solicitacoes;
	}
}
