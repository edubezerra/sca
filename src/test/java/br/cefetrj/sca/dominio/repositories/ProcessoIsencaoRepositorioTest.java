package br.cefetrj.sca.dominio.repositories;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import br.cefetrj.sca.config.AppConfig;
import br.cefetrj.sca.dominio.Aluno;
import br.cefetrj.sca.dominio.Departamento;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AppConfig.class)
public class ProcessoIsencaoRepositorioTest {

	@Autowired
	PedidoIsencaoDisciplinasRepositorio processoIsencaoRepositorio;

	@Autowired
	DepartamentoRepositorio departamentoRepositorio;

	@Test
	public void teste() {
		Departamento departamento = departamentoRepositorio
				.findDepartamentoBySigla("DEPIN");
		List<Aluno> alunosSolicitantes = processoIsencaoRepositorio
				.findAlunosSolicitantesByDepartamento(departamento);

		System.out.println(alunosSolicitantes.size());

		for (Aluno aluno : alunosSolicitantes) {
			System.out.println("ID do aluno: " + aluno.getId());
			System.out.println("Nome do aluno: " + aluno.getNome());
		}
	}
}
