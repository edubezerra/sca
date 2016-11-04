package br.cefetrj.sca.dominio;

import br.cefetrj.sca.dominio.repositories.BlacklistTagMonografiaRepositorio;
import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.Subselect;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by Alexandre Vicente on 04/11/16.
 */

@Entity
public class BlacklistTagMonografia {
    @Id
    private Long id = 1L;

    private Long getId() { return 1L; }
    private void setId(Long id) { id = 1L; }

    @Column(name="BLACKLIST", columnDefinition="TEXT")
    private String blacklist;

    public String getBlacklist() {
        return blacklist;
    }

    public void setBlacklist(String blacklist) {
        this.blacklist = blacklist;
    }

    public String[] getBlacklistItems() {
        return blacklist.split("\n");
    }

    public void setBlacklistItems(String[] items) {
        this.blacklist = StringUtils.join(items, '\n');
    }
}
