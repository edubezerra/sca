package br.cefetrj.sca.infra.cargadados;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.cefetrj.sca.dominio.Departamento;
import br.cefetrj.sca.dominio.repositories.DepartamentoRepositorio;

@Component
public class ImportadorDepartamentos {

	@Autowired
	DepartamentoRepositorio departamentoRepositorio;

	public void run() {

		Departamento dep01 = criar("DEMAT", "Departamento de Matemática");
		Departamento dep02 = criar("DEPEA", "Departamento de Administração");
		Departamento dep03 = criar("DEMEC", "Departamento de Engenharia Mecânica");
		Departamento dep04 = criar("DEPRO", "Departamento de Engenharia de Produção");
		Departamento dep05 = criar("DEPEC", "Departamento de Engenharia Civil");
		Departamento dep06 = criar("DEELE", "Departamento de Engenharia Elétrica");
		Departamento dep07 = criar("DEPIN", "Departamento de Informática");
		Departamento dep08 = criar("DEAMB", "Departamento de Engenharia Ambiental");
		Departamento dep09 = criar("DEFIS", "Departamento de Física");
		Departamento dep10 = criar("DECAP Química", "Departamento de Ciência Aplicadas - Química");
		Departamento dep11 = criar("DECAP Desenho", "Departamento de Ciência Aplicadas - Desenho");
		Departamento dep12 = criar("DEELT", "Departamento de Engenharia Eletrônica");
		Departamento dep13 = criar("DETEL", "Departamento de Engenharia de Telecomunicações");
		Departamento dep14 = criar("DECAU", "Departamento de Engenharia de Automação");

		Departamento[] departamentos = new Departamento[] { dep01, dep02, dep03, dep04, dep05, dep06, dep07, dep08,
				dep09, dep10, dep11, dep12, dep13, dep14 };

		int numDeptosImportados = 0;
		for (Departamento departamento : departamentos) {
			System.out.println("Importando departamento " + departamento.getSigla());
			Departamento depto = departamentoRepositorio.findDepartamentoBySigla(departamento.getSigla());
			if (depto == null) {
				departamentoRepositorio.save(departamento);
				numDeptosImportados++;
			}
		}

		System.out.println(numDeptosImportados + "foram departamentos importados com sucesso!");
	}

	private static Departamento criar(String sigla, String nome) {
		return new Departamento(sigla, nome);
	}
}
