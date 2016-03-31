package br.cefetrj.sca.dominio.repositories;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.cefetrj.sca.dominio.IntervaloTemporal;
import br.cefetrj.sca.dominio.LocalAula;
import br.cefetrj.sca.dominio.PeriodoLetivo;

public interface LocalAulaRepositorio extends
		JpaRepository<LocalAula, Serializable> {

	public List<LocalAula> findByCapacidadeGreaterThan(Integer capacidade);

	// TODO: completar a especifica��o dessa consulta. Tem que retornar apenas
	// os locais de aulas que n�o est�o sendo usados no per�odo letivo e
	// intervalos fornecidos. Ali�s, deve ser considerado o dia da semana
	// tamb�m!
	@Query("select u from LocalAula u")
	public List<LocalAula> findDesalocadas(PeriodoLetivo periodo,
			IntervaloTemporal intervalo);
}
