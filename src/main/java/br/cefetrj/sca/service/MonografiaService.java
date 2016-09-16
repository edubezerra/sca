package br.cefetrj.sca.service;

import br.cefetrj.sca.dominio.ArquivosMultipart;
import br.cefetrj.sca.dominio.TagMonografia;
import br.cefetrj.sca.dominio.Monografia;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Alexandre Vicente on 04/09/16.
 */
@Service
public class MonografiaService{
    public Collection<TagMonografia> obterTags(){
        throw new NotImplementedException();
    }

    public List<Monografia> buscarMonografias(String query){
        return buscarMonografias(query, 1);
    }

    public List<Monografia> buscarMonografias(String query, Integer pagina){
        throw new NotImplementedException();
    }

    public void enviarMonografia(Monografia monografia){
        throw new NotImplementedException();
    }

}
