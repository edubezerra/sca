package br.cefetrj.sca.dominio;

import org.springframework.security.access.method.P;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.function.Consumer;

/**
 * Created by Alexandre Vicente on 04/09/16.
 */
public class TagMonografia implements Comparable {
    private String tag;
    private Long ocorrencias;

    public TagMonografia(String tag, Long ocorrencias) {
        this.tag = tag;
        this.ocorrencias = ocorrencias;
    }

    public String getTag() {
        return tag;
    }

    public Long getOcorrencias() {
        return ocorrencias;
    }

    @Override
    public int compareTo(Object o) {
        if(o instanceof TagMonografia){
            TagMonografia tag = (TagMonografia) o;
            return ocorrencias.compareTo(tag.getOcorrencias());
        } else {
            return 1;
        }
    }

    @Override
    public int hashCode() {
        return tag.hashCode() + 2;
    }

    @Override
    public String toString() {
        return tag;
    }
}
