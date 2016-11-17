package br.cefetrj.sca.dominio.repositories;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;

import br.cefetrj.sca.dominio.atividadecomplementar.EnumTipoAtividadeComplementar;
import br.cefetrj.sca.dominio.atividadecomplementar.TipoAtividadeComplementar;

public interface TipoAtividadeComplementarRepositorio extends JpaRepository<TipoAtividadeComplementar, Serializable> {

	TipoAtividadeComplementar findById(String id);

	TipoAtividadeComplementar findByDescricaoAndCategoria(String descricao, EnumTipoAtividadeComplementar categoria);
}
