package br.cefetrj.sca.dominio.repositories;

import br.cefetrj.sca.dominio.BlacklistTagMonografia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;

/**
 * Created by Alexandre Vicente on 04/11/16.
 */
public interface BlacklistTagMonografiaRepositorio extends JpaRepository<BlacklistTagMonografia, Serializable> {
    public BlacklistTagMonografia findBlacklistTagMonografiaById(Long id);
}
