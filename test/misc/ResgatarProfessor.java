package misc;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import br.cefetrj.sca.dominio.Professor;

public class ResgatarProfessor {
	public static void main(String[] args) {
		EntityManagerFactory emf = Persistence
				.createEntityManagerFactory("SCAPU");

		EntityManager em = emf.createEntityManager();

		Professor p = em.find(Professor.class, 301L);

		System.out.println("Professor criado com id = " + p.getId());
	}
}