package br.cefetrj.sca.infra.cargadados;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import br.cefetrj.sca.dominio.Departamento;
import br.cefetrj.sca.dominio.PeriodoLetivo;
import br.cefetrj.sca.dominio.Turma;
import br.cefetrj.sca.dominio.matriculaforaprazo.AlocacacaoDisciplinasEmDepartamento;

public class ImportadorAlocacoesDisciplinasEmDepartamentos {
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

		EntityManager em = ImportadorTudo.entityManager;

		em.getTransaction().begin();

		List<Turma> turmas = findTurmasByPeriodoLetivo(em,
				PeriodoLetivo.PERIODO_CORRENTE);

		if (turmas != null) {
			Set<String> chaves = mapa.keySet();
			for (String siglaDepto : chaves) {
				Departamento departamento = obterDepartamentoPorSigla(em,
						siglaDepto);
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
				em.persist(alocacao);

				System.out.println("Foram alocadas "
						+ alocacao.getDisciplinas().size()
						+ " disciplina(s) no " + departamento.getNome());
			}
		}
		em.getTransaction().commit();
	}

	private List<Turma> findTurmasByPeriodoLetivo(EntityManager em,
			PeriodoLetivo periodo) {
		TypedQuery<Turma> query = em
				.createQuery(
						"SELECT t from Turma t WHERE t.periodo = :periodo",
						Turma.class);
		query.setParameter("periodo", periodo);

		try {
			return query.getResultList();
		} catch (NoResultException ex) {
			return null;
		}
	}

	private Departamento obterDepartamentoPorSigla(EntityManager em,
			String siglaDepartamento) {
		Query query = em
				.createQuery("from Departamento a where a.sigla = :siglaDepartamento");
		query.setParameter("siglaDepartamento", siglaDepartamento);

		try {
			return (Departamento) query.getSingleResult();
		} catch (NoResultException ex) {
			return null;
		}
	}
}
