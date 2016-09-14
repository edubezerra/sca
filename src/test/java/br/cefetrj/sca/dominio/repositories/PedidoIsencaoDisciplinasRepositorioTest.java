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
import br.cefetrj.sca.dominio.isencoes.ItemPedidoIsencaoDisciplina;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AppConfig.class)
public class PedidoIsencaoDisciplinasRepositorioTest {

	@Autowired
	PedidoIsencaoDisciplinasRepositorio processoIsencaoRepositorio;

	@Autowired
	DepartamentoRepositorio departamentoRepositorio;

	@Autowired
	AlunoRepositorio alunoRepositorio;

	@Test
	public void testFindAlunosSolicitantesByDepartamento() {
		Departamento departamento = departamentoRepositorio.findDepartamentoBySigla("DEPIN");
		List<Aluno> alunosSolicitantes = processoIsencaoRepositorio.findAlunosSolicitantesByDepartamento(departamento);

		System.out.println(alunosSolicitantes.size());

		System.out.println("Solicitantes:");
		for (Aluno aluno : alunosSolicitantes) {
			System.out.println("ID do aluno: " + aluno.getId());
			System.out.println("Nome do aluno: " + aluno.getNome());
		}
	}

	@Test
	public void testFindItensByDepartamentoAndAluno() {
		Departamento departamento = departamentoRepositorio.findDepartamentoBySigla("DEPIN");
		Aluno aluno = alunoRepositorio.findAlunoByMatricula("1311057BCC");
		List<ItemPedidoIsencaoDisciplina> itens = processoIsencaoRepositorio
				.findItensByDepartamentoAndAluno(departamento, aluno);

		System.out.println(itens.size());

		System.out.println("Itens:");
		for (ItemPedidoIsencaoDisciplina item : itens) {
			System.out.println("ID do aluno: " + item.getId());
			System.out.println("Nome do aluno: " + item.getDisciplina().getNome());
		}
	}
}
