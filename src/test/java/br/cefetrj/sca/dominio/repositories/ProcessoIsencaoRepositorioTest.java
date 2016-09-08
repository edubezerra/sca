package br.cefetrj.sca.dominio.repositories;

import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import br.cefetrj.sca.config.AppConfig;
import br.cefetrj.sca.dominio.Departamento;
import br.cefetrj.sca.dominio.isencoes.ItemIsencaoDisciplina;
import br.cefetrj.sca.dominio.isencoes.ProcessoIsencaoDisciplinas;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AppConfig.class)
@Transactional
public class ProcessoIsencaoRepositorioTest {

	@Autowired
	ProcessoIsencaoRepositorio processoIsencaoRepositorio;
	
	@Autowired
	DepartamentoRepositorio departamentoRepositorio;
	
	@Test
	public void teste() {
		Departamento departamento = departamentoRepositorio.findDepartamentoBySigla("DEPIN");
		Set<ProcessoIsencaoDisciplinas> processos = processoIsencaoRepositorio.findByDepartamento(departamento);

		System.out.println(processos.size());

		for (ProcessoIsencaoDisciplinas processo : processos) {
			System.out.println("ID do processo: " + processo.getId());
			List<ItemIsencaoDisciplina> itens = processo.getItens();
			for (ItemIsencaoDisciplina item : itens) {
				System.out.println(item.getDisciplina().getCodigo() + " " + item.getDisciplina().getNome());
			}
		}
	}
}
