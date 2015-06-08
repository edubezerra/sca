package br.cefetrj.sca.dominio;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import br.cefetrj.sca.dominio.spec.Specification;
import br.cefetrj.sca.infra.LocalAulaDao;

public class LocalAulaRepositorio {
	@Autowired
	private LocalAulaDao dao = new br.cefetrj.sca.infra.LocalAulaDaoJpa();

	public LocalAulaRepositorio() {
	}

	public List<LocalAula> findAllBySpecification(Specification<LocalAula> specification) {
		return dao.getAll(specification);
	}
}
