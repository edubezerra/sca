package br.cefetrj.sca.infra;

import java.util.List;

import br.cefetrj.sca.dominio.LocalAula;
import br.cefetrj.sca.dominio.spec.Specification;

public interface LocalAulaDao {

	List<LocalAula> getAll(Specification<LocalAula> specification);

}
