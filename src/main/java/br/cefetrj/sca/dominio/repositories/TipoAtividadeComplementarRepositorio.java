package br.cefetrj.sca.dominio.repositories;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;
import br.cefetrj.sca.dominio.atividadecomplementar.TipoAtividadeComplementar;

public interface TipoAtividadeComplementarRepositorio extends JpaRepository<TipoAtividadeComplementar, Serializable> {

//	TipoAtividadeComplementar findTipoAtividadeComplementarByCodigo(String codigoAtiv);

	TipoAtividadeComplementar findTipoAtividadeComplementarById(String id);
}
