package br.cefetrj.sca.infra;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import br.cefetrj.sca.dominio.Aluno;
import br.cefetrj.sca.dominio.LocalAula;
import br.cefetrj.sca.dominio.spec.Specification;
import br.cefetrj.sca.infra.jpa.GenericDaoJpa;

public class LocalAulaDaoJpa extends GenericDaoJpa<Aluno>  implements LocalAulaDao {

	@Override
	public List<LocalAula> getAll(Specification<LocalAula> specification) {
		
		CriteriaBuilder criteriaBuilder = super.getEntityManager().getCriteriaBuilder();
		// use specification.getType() to create a Root<T> instance
		CriteriaQuery<LocalAula> criteriaQuery = criteriaBuilder
				.createQuery(specification.getType());
		Root<LocalAula> root = criteriaQuery.from(specification.getType());
		// get predicate from specification
		Predicate predicate = specification.toPredicate(root, criteriaBuilder);
		// set predicate and execute query
		criteriaQuery.where(predicate);
		return super.getEntityManager().createQuery(criteriaQuery).getResultList();	}
}
