package br.cefetrj.sca.infra.cargadados;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import br.cefetrj.sca.dominio.Departamento;

public class ImportadorDepartamentos {

	public static void run() {

		EntityManagerFactory emf = Persistence
				.createEntityManagerFactory("SCAPU");

		EntityManager em = emf.createEntityManager();

		em.getTransaction().begin();

		Departamento dep1 = criaDepartamento("DEMAT");
		Departamento dep2 = criaDepartamento("DEPEA");
		Departamento dep3 = criaDepartamento("DEMEC");
		Departamento dep4 = criaDepartamento("DEPRO");
		Departamento dep5 = criaDepartamento("DEPEC");
		Departamento dep6 = criaDepartamento("DEELE");
		Departamento dep7 = criaDepartamento("DEPIN");
		Departamento dep8 = criaDepartamento("DEAMB");
		Departamento dep9 = criaDepartamento("DEFIS");
		Departamento dep10 = criaDepartamento("DECAP Química");
		Departamento dep11 = criaDepartamento("DECAP Desenho");
		Departamento dep12 = criaDepartamento("DEELT");
		Departamento dep13 = criaDepartamento("DETEL");
		Departamento dep14 = criaDepartamento("DECAU");

		Departamento[] departamentos = new Departamento[] { dep1, dep2, dep3,
				dep4, dep5, dep6, dep7, dep8, dep9, dep10, dep11, dep12, dep13,
				dep14 };

		for (Departamento departamento: departamentos) {
			System.out.println("Importando departamento:" + departamento.getNome());
			em.persist(departamento);
		}

		em.getTransaction().commit();

		em.close();
		
		System.out.println("Departamentos Importados com sucesso");
	}

	private static Departamento criaDepartamento(String nome) {
		return new Departamento(nome);
	}
	
	public static void main(String[] args) {
		run();
	}
}
