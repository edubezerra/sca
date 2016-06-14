package br.cefetrj.sca.infra.cargadados;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.cefetrj.sca.dominio.Departamento;
import br.cefetrj.sca.dominio.Professor;
import br.cefetrj.sca.dominio.repositories.DepartamentoRepositorio;
import br.cefetrj.sca.dominio.repositories.ProfessorRepositorio;

@Component
public class ImportadorAlocacoesProfessoresEmDepartamentos {
	@Autowired
	private DepartamentoRepositorio departamentoRepositorio;

	@Autowired
	ProfessorRepositorio professorRepositorio;

	public void run() {

		Professor professor = professorRepositorio
				.findProfessorByMatricula("1506449");
		Departamento departamento = departamentoRepositorio
				.findDepartamentoBySigla("DEPIN");
		departamento.addProfessor(professor);

		System.out.println("Foram alocados "
				+ departamento.getProfessores().size()
				+ " professor(es) no departamento " + departamento.getNome());
	}
}
