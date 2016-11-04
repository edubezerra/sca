package br.cefetrj.sca.service;

import br.cefetrj.sca.dominio.ArquivosMultipart;
import br.cefetrj.sca.dominio.BlacklistTagMonografia;
import br.cefetrj.sca.dominio.TagMonografia;
import br.cefetrj.sca.dominio.Monografia;
import br.cefetrj.sca.dominio.repositories.BlacklistTagMonografiaRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Alexandre Vicente on 04/09/16.
 */
@Service
public class MonografiaService{
    @Autowired
    BlacklistTagMonografiaRepositorio blacklistTagMonografiaRepositorio;

    public Collection<TagMonografia> obterTags() throws IOException {
        BlacklistTagMonografia blacklistTagMonografia = new BlacklistTagMonografia();
        blacklistTagMonografia.setBlacklistItems(new String[]{ "a", "e" });
        blacklistTagMonografiaRepositorio.save(blacklistTagMonografia);

        String[] blacklist = blacklistTagMonografiaRepositorio.findBlacklistTagMonografiaById(1L).getBlacklistItems();
        return TagMonografia.getTopTags(blacklist);
    }

    public List<Monografia> buscarMonografias(String query) throws IOException {
        return Monografia.buscar(query);
    }

    public Monografia obterMonografia(String id, Long idAutor, String autor, String orientador, String presidenteBanca, String[] membrosBanca, String titulo, String resumoLinguaEstrangeira, String resumoPortugues) throws IOException {
        Monografia monografia = new Monografia(
                id,
                idAutor,
                autor,
                orientador,
                presidenteBanca,
                membrosBanca,
                titulo,
                resumoLinguaEstrangeira,
                resumoPortugues
        );
        return monografia;
    }

    public void adicionarArquivos(String id, ArquivosMultipart arquivos) throws IOException {
        Monografia monografia = Monografia.get(id);
        monografia.adicionarArquivos(arquivos);
        monografia.salvar();
    }

}
