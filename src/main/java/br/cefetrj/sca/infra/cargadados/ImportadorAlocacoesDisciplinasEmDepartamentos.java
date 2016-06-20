package br.cefetrj.sca.infra.cargadados;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.cefetrj.sca.dominio.Departamento;
import br.cefetrj.sca.dominio.PeriodoLetivo;
import br.cefetrj.sca.dominio.Turma;
import br.cefetrj.sca.dominio.matriculaforaprazo.AlocacacaoDisciplinasEmDepartamento;
import br.cefetrj.sca.dominio.repositories.AlocacacaoDisciplinasEmDepartamentoRepositorio;
import br.cefetrj.sca.dominio.repositories.DepartamentoRepositorio;
import br.cefetrj.sca.dominio.repositories.TurmaRepositorio;

@Component
public class ImportadorAlocacoesDisciplinasEmDepartamentos {
	@Autowired
	private TurmaRepositorio turmaRepositorio;

	@Autowired
	private DepartamentoRepositorio departamentoRepositorio;

	@Autowired
	AlocacacaoDisciplinasEmDepartamentoRepositorio alocacacaoDisciplinasEmDepartamentoRepositorio;

	public void run() {

		Map<String, Set<String>> mapa = new HashMap<>();

		Set<String> lista = new HashSet<>();
		lista.add("GCC");
		lista.add("GTSI");
		mapa.put("DEPIN", lista);

		lista = new HashSet<>();
		lista.add("GCIV");
		mapa.put("DEPEC", lista);

		lista = new HashSet<>();
		lista.add("GPRO");
		mapa.put("DEPRO", lista);

		lista = new HashSet<>();
		lista.add("GADM");
		mapa.put("DEPEA", lista);

		List<Turma> turmas = turmaRepositorio
				.findTurmasAbertasNoPeriodo(PeriodoLetivo.PERIODO_CORRENTE);

		if (turmas != null) {
			Set<String> chaves = mapa.keySet();
			for (String siglaDepto : chaves) {
				Departamento departamento = departamentoRepositorio
						.findDepartamentoBySigla(siglaDepto);
				AlocacacaoDisciplinasEmDepartamento alocacao = new AlocacacaoDisciplinasEmDepartamento(
						departamento);
				lista = mapa.get(siglaDepto);
				for (String prefixoCodigoDisciplina : lista) {
					for (Turma turma : turmas) {
						if (turma.getCodigoDisciplina().startsWith(
								prefixoCodigoDisciplina)) {
							alocacao.alocarDisciplina(turma.getDisciplina());
						}
					}
				}
				alocacacaoDisciplinasEmDepartamentoRepositorio.save(alocacao);

				System.out.println("Foram alocadas "
						+ alocacao.getDisciplinas().size()
						+ " disciplina(s) no " + departamento.getNome());
			}
		}
	}
}
