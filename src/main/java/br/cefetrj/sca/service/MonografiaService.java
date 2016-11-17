package br.cefetrj.sca.service;

import br.cefetrj.sca.dominio.*;
import br.cefetrj.sca.dominio.repositories.AlunoRepositorio;
import br.cefetrj.sca.dominio.usuarios.PerfilUsuario;
import br.cefetrj.sca.dominio.usuarios.TipoPerfilUsuario;
import br.cefetrj.sca.dominio.usuarios.Usuario;
import br.cefetrj.sca.infra.monografia.ElasticSearchClientFactory;
import com.google.common.util.concurrent.SettableFuture;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.springframework.beans.NullValueInNestedPathException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

/**
 * Created by Alexandre Vicente on 04/09/16.
 */
@Service
public class MonografiaService{
    static Collection<TagMonografia> tagCache = null;

    @Autowired
    AlunoRepositorio alunoRepositorio;

    public Aluno obterAluno(String matricula){
        return alunoRepositorio.findAlunoByMatricula(matricula);
    }

    public Collection<TagMonografia> obterTags() throws IOException {
        if(tagCache != null){
            return tagCache;
        }

        String[] blacklist = getBlacklist().split("\n");
        tagCache = TagMonografia.getTopTags(blacklist);
        return tagCache;
    }

    public List<Monografia> minhasMonografias(String matricula) throws IOException {
        Aluno aluno = obterAluno(matricula);

        return Monografia.buscar("autor:\"" + aluno.getNome() + "\"");
    }

    public List<Monografia> buscarMonografias(String query) throws IOException {
        return Monografia.buscar(query);
    }

    public Monografia obterMonografia(String id, String[] autores, String orientador, String presidenteBanca, String[] membrosBanca, String titulo, String resumoLinguaEstrangeira, String resumoPortugues) throws IOException {
        Monografia monografia = new Monografia(
                id,
                autores,
                orientador,
                presidenteBanca,
                membrosBanca,
                titulo,
                resumoLinguaEstrangeira,
                resumoPortugues
        );
        tagCache = null;
        return monografia;
    }

    public void adicionarArquivos(String id, ArquivosMultipart arquivos) throws IOException {
        Monografia monografia = Monografia.get(id);
        monografia.adicionarArquivos(arquivos);
        tagCache = null;
    }


    public String getBlacklist() throws IOException {
        TransportClient client = ElasticSearchClientFactory.createClient();
        GetResponse getResponse = client
                .prepareGet("monografias", "blacklist", "blacklist")
                .setFields("blacklist")
                .get();

        String blacklist = null;

        try {
            blacklist = getResponse.getField("blacklist").getValue().toString();
        } catch (Exception e){
            blacklist = "a\nan\nthe";
            setBlacklist(blacklist);
        }

        return blacklist;
    }

    public void adminOnly(Usuario usuario) {
        for(PerfilUsuario perfil : usuario.getUserProfiles()){
            if(perfil.getType().equals(TipoPerfilUsuario.ROLE_ADMIN.getUserProfileType())){
                return;
            }
        }
        throw new AuthorizationServiceException("Acesso apenas para administradores");
    }

    public void setBlacklist(String blacklist) throws IOException {
        blacklist = blacklist.replaceAll("\r", "");

        TransportClient client = ElasticSearchClientFactory.createClient();
        IndexRequestBuilder indexRequestBuilder = client.prepareIndex("monografias", "blacklist", "blacklist");
        XContentBuilder contentBuilder = XContentFactory.jsonBuilder().startObject();

        contentBuilder.field("blacklist", blacklist);

        indexRequestBuilder.setSource(contentBuilder);

        SettableFuture<IndexResponse> future = SettableFuture.create();

        indexRequestBuilder.execute(new ActionListener<IndexResponse>() {
            @Override
            public void onResponse(IndexResponse indexResponse) {
                synchronized (future) {
                    future.set(indexResponse);
                    future.notifyAll();
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                synchronized (future) {
                    future.setException(throwable);
                    future.notifyAll();
                }
            }
        });

        IndexResponse response;

        try {
            synchronized (future) {
                future.wait();
                response = future.get();
            }
        } catch (Exception e) {
            throw new IOException(e);
        }
        tagCache = null;
    }

    public List<Aluno> alunoAutocomplete(String q){
        List<Aluno> resultados = alunoRepositorio.findAlunosByNome(q);
        return resultados.subList(0, Math.min(resultados.size(), 5));
    }
}
