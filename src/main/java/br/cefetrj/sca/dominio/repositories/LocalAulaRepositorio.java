package br.cefetrj.sca.dominio.repositories;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.cefetrj.sca.dominio.Intervalo;
import br.cefetrj.sca.dominio.LocalAula;
import br.cefetrj.sca.dominio.PeriodoLetivo;

public interface LocalAulaRepositorio extends
		JpaRepository<LocalAula, Serializable> {

	public List<LocalAula> findByCapacidadeGreaterThan(Integer capacidade);

	// TODO: completar a especificação dessa consulta. Tem que retornar apenas
	// os locais de aulas que não estão sendo usados no período letivo e
	// intervalos fornecidos. Aliás, deve ser considerado o dia da semana
	// também!
	@Query("select u from LocalAula u")
	public List<LocalAula> findDesalocadas(PeriodoLetivo periodo,
			Intervalo intervalo);
}
