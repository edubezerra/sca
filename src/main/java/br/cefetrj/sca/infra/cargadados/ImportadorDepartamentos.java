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

		Departamento dep1 = criar("DEMAT", "Departamento de Matemática");
		Departamento dep2 = criar("DEPEA", "Departamento de Administração");
		Departamento dep3 = criar("DEMEC", "Departamento de Engenharia Mecânica");
		Departamento dep4 = criar("DEPRO", "Departamento de Engenharia de Produção");
		Departamento dep5 = criar("DEPEC", "Departamento de Engenharia Civil");
		Departamento dep6 = criar("DEELE", "Departamento de Engenharia Elétrica");
		Departamento dep7 = criar("DEPIN", "Departamento de Informática");
		Departamento dep8 = criar("DEAMB", "Departamento de Engenharia Ambiental");
		Departamento dep9 = criar("DEFIS", "Departamento de Física");
		Departamento dep10 = criar("DECAP Química", "Departamento de Ciência Aplicadas - Química");
		Departamento dep11 = criar("DECAP Desenho", "Departamento de Ciência Aplicadas - Desenho");
		Departamento dep12 = criar("DEELT", "Departamento de Engenharia Eletrônica");
		Departamento dep13 = criar("DETEL", "Departamento de Engenharia de Telecomunicações");
		Departamento dep14 = criar("DECAU", "Departamento de Engenharia de Automação");

		Departamento[] departamentos = new Departamento[] { dep1, dep2, dep3,
				dep4, dep5, dep6, dep7, dep8, dep9, dep10, dep11, dep12, dep13,
				dep14 };

		for (Departamento departamento : departamentos) {
			System.out.println("Importando departamento "
					+ departamento.getSigla());
			departamentoRepositorio.save(departamento);
		}

		System.out.println("Departamentos importados com sucesso!");
	}

	private static Departamento criar(String sigla, String nome) {
		return new Departamento(sigla, nome);
	}
}
