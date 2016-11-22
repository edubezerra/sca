package br.cefetrj.sca.dominio;

import br.cefetrj.sca.infra.monografia.TagExtractor;
import com.google.common.util.concurrent.SettableFuture;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.sax.BodyContentHandler;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.delete.DeleteRequestBuilder;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.*;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.springframework.web.multipart.MultipartFile;

import br.cefetrj.sca.infra.monografia.ElasticSearchClientFactory;

import javax.servlet.ServletContext;
import java.io.*;
import java.math.BigInteger;
import java.net.UnknownHostException;
import java.util.*;

/**
 * Created by Alexandre Vicente on 04/09/16.
 */
public class Monografia {
    private String id;
    private String[] autores;
    private String orientador;
    private String presidenteBanca;
    private String[] membrosBanca;
    private String titulo;
    private String resumoLinguaEstrangeira;
    private String resumoPortugues;
    private Set<Arquivo> arquivos = new HashSet<Arquivo>();
    private List<Object> cacheArquivos;

    public Monografia(String id, String[] autores, String orientador, String presidenteBanca, String[] membrosBanca, String titulo, String resumoLinguaEstrangeira, String resumoPortugues) {
        this.id = id;
        this.autores = autores;
        this.orientador = orientador;
        this.presidenteBanca = presidenteBanca;
        this.membrosBanca = membrosBanca;
        this.titulo = titulo;
        this.resumoLinguaEstrangeira = resumoLinguaEstrangeira;
        this.resumoPortugues = resumoPortugues;
    }

    public class Arquivo {
        private String nome;
        private Long tamanho;
        private Integer index;
        private MultipartFile arquivoMultipart;

        public Arquivo(String nome, Long tamanho, Integer index) {
            this.nome = nome;
            this.tamanho = tamanho;
            this.index = index;
        }

        public Arquivo(MultipartFile arquivoMultipart) {
            this.arquivoMultipart = arquivoMultipart;
            this.nome = arquivoMultipart.getOriginalFilename();
            this.tamanho = arquivoMultipart.getSize();
        }

        public String getId() {
            return id;
        }

        public String getNome() {
            return nome;
        }

        public Long getTamanho() {
            return tamanho;
        }

        public String getTamanhoLegivel(){
            return FileUtils.byteCountToDisplaySize(BigInteger.valueOf(tamanho));
        }

        public String getConteudoBase64() throws IOException {
            if (arquivoMultipart != null) {
                byte[] conteudo = new byte[Math.toIntExact(tamanho)];
                arquivoMultipart.getInputStream().read(conteudo);
                return Base64.getEncoder().encodeToString(conteudo);
            }

            carregarCacheArquivos();

            return (String) cacheArquivos.get(this.index);
        }

        public byte[] getConteudo() throws IOException {
            if (arquivoMultipart != null) {
                byte[] conteudo = new byte[Math.toIntExact(tamanho)];
                arquivoMultipart.getInputStream().read(conteudo);
                return conteudo;
            }

            carregarCacheArquivos();

            return Base64.getDecoder().decode((String) cacheArquivos.get(this.index));
        }

        public String getTags(ServletContext servletContext) throws IOException {
            InputStream inputStream = null;
            String text = "";

            if (arquivoMultipart != null) {
                inputStream = arquivoMultipart.getInputStream();
            } else {
                carregarCacheArquivos();
                byte[] buffer = Base64.getDecoder().decode((String) cacheArquivos.get(this.index));
                inputStream = new ByteArrayInputStream(buffer);
            }

            BodyContentHandler handler = new BodyContentHandler();

            AutoDetectParser parser = new AutoDetectParser();
            Metadata metadata = new Metadata();

            try {
                parser.parse(inputStream, handler, metadata);
                String rawText = handler.toString();
                text = TagExtractor.getInstance(servletContext).getTags(rawText);

            } catch (Exception e) {
                e.printStackTrace();
            }

            return text;
        }

        @Override
        public int hashCode() {
            return nome.hashCode() + 5;
        }
    }

    private void carregarCacheArquivos() throws IOException {
        if (cacheArquivos == null) {
            TransportClient client = ElasticSearchClientFactory.createClient();
            GetRequestBuilder getRequestBuilder = client.prepareGet("monografias", "monografia", id);
            getRequestBuilder.setFields(
                    "arquivos.conteudo"
            );
            GetResponse response = getRequestBuilder.get();
            cacheArquivos = response.getField("arquivos.conteudo").getValues();
        }
    }

    public String getId() {
        return id;
    }

    public String[] getAutores() {
        return autores;
    }

    public String getAutoresString() {
        return org.apache.commons.lang.StringUtils.join(autores, "; ");
    }

    public String getTitulo() {
        return titulo;
    }

    public String getAbstract() {
        return resumoLinguaEstrangeira;
    }

    public String getResumoPortugues() {
        return resumoPortugues;
    }

    public String getOrientador() {
        return orientador;
    }

    public String getPresidenteBanca() {
        return presidenteBanca;
    }

    public String[] getMembrosBanca() {
        return membrosBanca;
    }

    public String getNomesMembrosBanca() {
        return StringUtils.join(membrosBanca, ", ");
    }

    public String getResumoLinguaEstrangeira() {
        return resumoLinguaEstrangeira;
    }

    public Set<Arquivo> getArquivos() {
        return arquivos;
    }

    public void setAutores(String[] autores) {
        this.autores = autores;
    }

    public void setOrientador(String orientador) {
        this.orientador = orientador;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setResumoLinguaEstrangeira(String resumoLinguaEstrangeira) {
        this.resumoLinguaEstrangeira = resumoLinguaEstrangeira;
    }

    public void setResumoPortugues(String resumoPortugues) {
        this.resumoPortugues = resumoPortugues;
    }

    private void adicionarArquivo(String nome, Long tamanho, Integer index) {
        Arquivo arquivo = new Arquivo(nome, tamanho, index);
        if (!this.arquivos.add(arquivo)) {
            throw new IllegalArgumentException("Nome de arquivo duplicado (" + arquivo.getNome() + ")");
        }
    }

    public void adicionarArquivos(ArquivosMultipart arquivos) {
        for (MultipartFile arquivo : arquivos.getArquivos()) {
            if(arquivo.getSize() == 0){
                continue;
            }
            if (!this.arquivos.add(new Arquivo(arquivo))) {
                throw new IllegalArgumentException("Nome de arquivo duplicado (" + arquivo.getName() + ")");
            }
            ;
        }
    }

    public void removerArquivos(Collection<String> nomes){
        for(Arquivo arquivo: arquivos){
            if(nomes.contains(arquivo.nome)){
                arquivos.remove(arquivo);
            }
        }
    }

    public static Monografia get(String id) throws IOException {
        TransportClient client = ElasticSearchClientFactory.createClient();
        GetRequestBuilder getRequestBuilder = client.prepareGet("monografias", "monografia", id);
        getRequestBuilder.setFields(
                "titulo",
                "autor",
                "orientador",
                "presidenteBanca",
                "membrosBanca",
                "resumoLinguaEstrangeira",
                "resumoPortugues",
                "arquivos.nome",
                "arquivos.tamanho"
        );
        GetResponse response = getRequestBuilder.get();

        if (response == null) {
            return null;
        }

        List<Object> membrosBancaObject = response.getField("membrosBanca").getValues();
        String[] membrosBanca = new String[membrosBancaObject.size()];

        for (int i = 0; i < membrosBanca.length; i++) {
            membrosBanca[i] = (String) (membrosBancaObject.get(i));
        }

        List<Object> autores = response.getField("autor").getValues();
        String[] autoresArray = new String[autores.size()];

        for (int i = 0; i < autoresArray.length; i++) {
            autoresArray[i] = (String) (autores.get(i));
        }

        Monografia monografia = new Monografia(
                response.getId(),
                autoresArray,
                (String) (response.getField("orientador").getValue()),
                (String) (response.getField("presidenteBanca").getValue()),
                membrosBanca,
                (String) (response.getField("titulo").getValue()),
                (String) (response.getField("resumoLinguaEstrangeira").getValue()),
                (String) (response.getField("resumoPortugues").getValue())
        );

        try {
            List<Object> nomesArquivos = response.getField("arquivos.nome").getValues();
            List<Object> tamanhosArquivos = response.getField("arquivos.tamanho").getValues();

            for (int i = 0; i < nomesArquivos.size(); i++) {
                monografia.adicionarArquivo(
                        (String) (nomesArquivos.get(i)),
                        (Long) (tamanhosArquivos.get(i)),
                        i
                );
            }
        } catch (Exception e){
            // Nenhum arquivo
            e.printStackTrace();
        }

        client.close();

        return monografia;
    }

    public IndexResponse salvar(ServletContext servletContext) throws IOException {
        TransportClient client = ElasticSearchClientFactory.createClient();

        if(id.isEmpty()){
            id = null;
        }

        IndexRequestBuilder indexRequestBuilder = client.prepareIndex("monografias", "monografia", id);

        XContentBuilder contentBuilder = XContentFactory.jsonBuilder().startObject();

        contentBuilder.field("titulo", titulo);
        contentBuilder.array("autor", autores);
        contentBuilder.field("orientador", orientador);
        contentBuilder.field("presidenteBanca", presidenteBanca);
        contentBuilder.array("membrosBanca", membrosBanca);
        contentBuilder.field("resumoLinguaEstrangeira", resumoLinguaEstrangeira);
        contentBuilder.field("resumoPortugues", resumoPortugues);

        List<Map<String, Object>> listaArquivos = new ArrayList<>(arquivos.size());
        String textoPuro = "";

        for (Arquivo arquivo : arquivos) {
            Map<String, Object> map = new HashMap<>();
            map.put("nome", arquivo.nome);
            map.put("tamanho", arquivo.tamanho);
            map.put("conteudo", arquivo.getConteudoBase64());
            textoPuro += arquivo.getTags(servletContext);

            listaArquivos.add(map);
        }

        contentBuilder.array("arquivos", listaArquivos.toArray());
        contentBuilder.field("textoPuro", textoPuro);

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

        id = response.getId();

        client.close();
        return response;
    }

    public DeleteResponse deletar() throws IOException {
        TransportClient client = ElasticSearchClientFactory.createClient();

        DeleteRequestBuilder requestBuilder = client.prepareDelete("monografias", "monografia", id);

        SettableFuture<DeleteResponse> future = SettableFuture.create();

        requestBuilder.execute(new ActionListener<DeleteResponse>() {
                @Override
                public void onResponse(DeleteResponse response) {
                synchronized (future) {
                    future.set(response);
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

        DeleteResponse response;

        try {
            synchronized (future) {
                future.wait();
                response = future.get();
            }
        } catch (Exception e) {
            throw new IOException(e);
        }

        client.close();
        return response;
    }

    public static List<Monografia> buscar(String query) throws IOException {
        TransportClient client = ElasticSearchClientFactory.createClient();

        QueryStringQueryBuilder queryBuilder = QueryBuilders.queryStringQuery(query);

        SettableFuture<List<Monografia>> future = SettableFuture.create();

        SearchRequestBuilder searchRequestBuilder = client.prepareSearch("monografias");

        searchRequestBuilder.setQuery(queryBuilder);
        searchRequestBuilder.addFields(
                "titulo",
                "autor",
                "orientador",
                "presidenteBanca",
                "membrosBanca",
                "resumoLinguaEstrangeira",
                "resumoPortugues"
        );

        searchRequestBuilder.execute(new ActionListener<SearchResponse>() {
            @Override
            public void onResponse(SearchResponse searchResponse) {
                SearchHits hits = searchResponse.getHits();
                List<Monografia> results = new ArrayList<Monografia>(Math.toIntExact(hits.getTotalHits()));

                for(SearchHit hit : hits){
                    try{
                        List<Object> membrosBancaObject = hit.field("membrosBanca").getValues();
                        String[] membrosBanca;
                        if(membrosBancaObject != null){
                            membrosBanca = new String[membrosBancaObject.size()];

                            for (int i = 0; i < membrosBanca.length; i++) {
                                membrosBanca[i] = (String) (membrosBancaObject.get(i));
                            }
                        } else {
                            membrosBanca = new String[0];
                        }

                        List<Object> autores = hit.field("autor").getValues();
                        String[] autoresArray;
                        if(autores != null){
                            autoresArray = new String[autores.size()];

                            for (int i = 0; i < autoresArray.length; i++) {
                                autoresArray[i] = (String) (autores.get(i));
                            }
                        } else {
                            autoresArray = new String[0];
                        }

                        Monografia monografia = new Monografia(
                                hit.getId(),
                                autoresArray,
                                (String) (hit.field("orientador").getValue()),
                                (String) (hit.field("presidenteBanca").getValue()),
                                membrosBanca,
                                (String) (hit.field("titulo").getValue()),
                                (String) (hit.field("resumoLinguaEstrangeira").getValue()),
                                (String) (hit.field("resumoPortugues").getValue())
                        );

                        results.add(monografia);
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }

                synchronized (future){
                    future.set(results);
                    future.notifyAll();
                }
            }

            @Override
            public void onFailure(Throwable e) {
                synchronized (future) {
                    future.setException(e);
                    future.notifyAll();
                }
            }
        });

        List<Monografia> response;

        try {
            synchronized (future) {
                future.wait();
                response = future.get();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new IOException(e);
        }

        client.close();
        return response;
    }

}

    /*
PUT /monografias
{
  "settings": {
    "number_of_shards": 1
  },
  "mappings": {
    "monografia": {
      "properties": {
        "autor": {
          "type": "string"
        },
        "titulo": {
          "type": "string"
        },
        "orientador": {
          "type": "string"
        },
        "presidenteBanca": {
          "type": "string"
        },
        "membrosBanca": {
          "type": "string"
        },
        "resumoLinguaEstrangeira": {
          "type": "string"
        },
        "resumoPortugues": {
          "type": "string"
        },
        "tags": {
            "type": "string",
            "term_vector": "with_positions_offsets"
        },
        "arquivos": {
          "type": "nested",
          "properties": {
            "nome": {
              "type": "string"
            },
            "tamanho": {
              "type": "long"
            },
            "conteudo": {
              "type": "attachment",
              "fields": {
                "content": {
                  "store": "yes"
                },
                "author": {
                  "store": "yes"
                },
                "title": {
                  "store": "yes",
                  "analyzer": "english"
                },
                "date": {
                  "store": "yes"
                },
                "keywords": {
                  "store": "yes",
                  "analyzer": "keyword"
                }
              }
            }
          }
        }
      }
    }
  }
}
 */

