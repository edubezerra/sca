package br.cefetrj.sca.dominio.spec;

import java.lang.reflect.ParameterizedType;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import br.cefetrj.sca.dominio.LocalAula;
import br.cefetrj.sca.dominio.PeriodoLetivo;

abstract public class CompositeSpecification<T> implements Specification<T> {

	@Override
	public boolean isSatisfiedBy(T t) {
		throw new NotImplementedException();
	}

	@Override
	public Predicate toPredicate(Root<T> poll, CriteriaBuilder cb) {
		throw new NotImplementedException();
	}

	@Override
	public Specification<T> and(Specification<T> other) {
		return new AndSpecification<>(this, other);
	}

	@Override
	public Class<T> getType() {
		ParameterizedType type = (ParameterizedType) this.getClass().getGenericSuperclass();
		return (Class<T>)type.getActualTypeArguments()[0];
	}

	public boolean isSatisfiedBy(LocalAula local, PeriodoLetivo periodo) {
		// TODO Auto-generated method stub
		return false;
	}
}
