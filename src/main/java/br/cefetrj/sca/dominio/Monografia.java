package br.cefetrj.sca.dominio;

import com.google.common.util.concurrent.SettableFuture;
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

import br.cefetrj.sca.infra.ElasticSearchClientFactory;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.*;

/**
 * Created by Alexandre Vicente on 04/09/16.
 */
public class Monografia {
    private String id;
    private Long idAutor;
    private String autor;
    private String orientador;
    private String presidenteBanca;
    private String[] membrosBanca;
    private String titulo;
    private String resumoLinguaEstrangeira;
    private String resumoPortugues;
    private Set<Arquivo> arquivos = new HashSet<Arquivo>();
    private List<Object> cacheArquivos;

    public Monografia(String id, Long idAutor, String autor, String orientador, String presidenteBanca, String[] membrosBanca, String titulo, String resumoLinguaEstrangeira, String resumoPortugues) {
        this.id = id;
        this.idAutor = idAutor;
        this.autor = autor;
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
            this.nome = arquivoMultipart.getName();
            this.tamanho = arquivoMultipart.getSize();
        }

        public String getId() {
            return id;
        }

        public String getNome() {
            return nome;
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

        @Override
        public int hashCode() {
            return nome.hashCode() + 5;
        }
    }

    private void carregarCacheArquivos() throws UnknownHostException {
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

    public String getAutor() {
        return autor;
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

    public void setAutor(String autor) {
        this.autor = autor;
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
            if (!this.arquivos.add(new Arquivo(arquivo))) {
                throw new IllegalArgumentException("Nome de arquivo duplicado (" + arquivo.getName() + ")");
            }
            ;
        }
    }

    public static Monografia get(String id) throws UnknownHostException {
        TransportClient client = ElasticSearchClientFactory.createClient();
        GetRequestBuilder getRequestBuilder = client.prepareGet("monografias", "monografia", id);
        getRequestBuilder.setFields(
                "titulo",
                "idAutor",
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

        Monografia monografia = new Monografia(
                response.getId(),
                (Long) (response.getField("idAutor").getValue()),
                (String) (response.getField("autor").getValue()),
                (String) (response.getField("orientador").getValue()),
                (String) (response.getField("presidenteBanca").getValue()),
                membrosBanca,
                (String) (response.getField("titulo").getValue()),
                (String) (response.getField("resumoLinguaEstrangeira").getValue()),
                (String) (response.getField("resumoPortugues").getValue())
        );

        List<Object> nomesArquivos = response.getField("arquivos.nome").getValues();
        List<Object> tamanhosArquivos = response.getField("arquivos.tamanho").getValues();

        for (int i = 0; i < nomesArquivos.size(); i++) {
            monografia.adicionarArquivo(
                    (String) (nomesArquivos.get(i)),
                    (Long) (tamanhosArquivos.get(i)),
                    i
            );
        }

        client.close();

        return monografia;
    }

    public IndexResponse salvar() throws IOException {
        TransportClient client = ElasticSearchClientFactory.createClient();

        IndexRequestBuilder indexRequestBuilder = client.prepareIndex("monografias", "monografia", id);

        XContentBuilder contentBuilder = XContentFactory.jsonBuilder().startObject();

        contentBuilder.field("idAutor", idAutor);
        contentBuilder.field("titulo", titulo);
        contentBuilder.field("autor", autor);
        contentBuilder.field("orientador", orientador);
        contentBuilder.field("presidenteBanca", presidenteBanca);
        contentBuilder.array("membrosBanca", membrosBanca);
        contentBuilder.field("resumoLinguaEstrangeira", resumoLinguaEstrangeira);
        contentBuilder.field("resumoPortugues", resumoPortugues);

        List<Map<String, Object>> listaArquivos = new ArrayList<>(arquivos.size());

        for (Arquivo arquivo : arquivos) {
            Map<String, Object> map = new HashMap<>();
            map.put("nome", arquivo.nome);
            map.put("tamanho", arquivo.tamanho);
            map.put("conteudo", arquivo.getConteudoBase64());
            listaArquivos.add(map);
        }

        contentBuilder.array("arquivos", listaArquivos.toArray());

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
                "idAutor",
                "autor",
                "orientador",
                "presidenteBanca",
                "membrosBanca",
                "resumoLinguaEstrangeira",
                "resumoPortugues",
                "arquivos.nome",
                "arquivos.tamanho"
        );

        searchRequestBuilder.execute(new ActionListener<SearchResponse>() {
            @Override
            public void onResponse(SearchResponse searchResponse) {
                SearchHits hits = searchResponse.getHits();
                List<Monografia> results = new ArrayList<Monografia>(Math.toIntExact(hits.getTotalHits()));
                
                for(SearchHit hit : hits){
                    List<Object> membrosBancaObject = hit.field("membrosBanca").getValues();
                    String[] membrosBanca = new String[membrosBancaObject.size()];

                    for (int i = 0; i < membrosBanca.length; i++) {
                        membrosBanca[i] = (String) (membrosBancaObject.get(i));
                    }

                    Monografia monografia = new Monografia(
                            hit.getId(),
                            Long.parseLong(hit.field("idAutor").getValue().toString()),
                            (String) (hit.field("autor").getValue()),
                            (String) (hit.field("orientador").getValue()),
                            (String) (hit.field("presidenteBanca").getValue()),
                            membrosBanca,
                            (String) (hit.field("titulo").getValue()),
                            (String) (hit.field("resumoLinguaEstrangeira").getValue()),
                            (String) (hit.field("resumoPortugues").getValue())
                    );

                    List<Object> nomesArquivos = hit.field("arquivos.nome").getValues();
                    List<Object> tamanhosArquivos = hit.field("arquivos.tamanho").getValues();

                    for (int i = 0; i < nomesArquivos.size(); i++) {
                        monografia.adicionarArquivo(
                                (String) (nomesArquivos.get(i)),
                                Long.parseLong(tamanhosArquivos.get(i).toString()),
                                i
                        );
                    }

                    results.add(monografia);
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
            throw new IOException(e);
        }

        client.close();
        return response;
    }

    public static void main(String args[]) throws Throwable {
        Monografia monografia = new Monografia(null, -1L, "Autor", "Orientador", "Presidente", new String[]{"M1", "M2"}, "Title", "Abstract", "Resumo");
        monografia.salvar();

        List<Monografia> monografias = Monografia.buscar("BNF");
        for(Monografia m : monografias){
            System.out.println(m.getId());
        }

        monografia.deletar();
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
        "idAutor": {
          "type": "long"
        },
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

POST /monografias/monografia
{
  "titulo": "Titulo",
  "idAutor": -1,
  "autor": "Fulano da Silva",
  "orientador": "Orientador",
  "presidenteBanca": "Presidente",
  "membrosBanca": [
    "Membro 1",
    "Membro 2"
  ],
  "resumoLinguaEstrangeira": "Abstract",
  "resumoPortugues": "Resumo",
  "arquivos": [
    {
      "tamanho": 31366,
      "conteudo": "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MiA0OTEgNjQ3IDYxMiA0OTMgMzA5IDQ5MyA0OTMgNDkzIF0KXQo+PgplbmRvYmoKNDEgMCBvYmoKPDwgL0xlbmd0aCA4MjYgPj4Kc3RyZWFtCi9DSURJbml0IC9Qcm9jU2V0IGZpbmRyZXNvdXJjZSBiZWdpbgoxMiBkaWN0IGJlZ2luCmJlZ2luY21hcAovQ0lEU3lzdGVtSW5mbyA8PCAvUmVnaXN0cnkgKEFkb2JlKSAvT3JkZXJpbmcgKFVDUykgL1N1cHBsZW1lbnQgMCA+PiBkZWYKL0NNYXBOYW1lIC9BZG9iZS1JZGVudGl0eS1VQ1MgZGVmCi9DTWFwVHlwZSAyIGRlZgoxIGJlZ2luY29kZXNwYWNlcmFuZ2UKPDAwMDA+IDxGRkZGPgplbmRjb2Rlc3BhY2VyYW5nZQoyIGJlZ2luYmZyYW5nZQo8MDAwMD4gPDAwMDA+IDwwMDAwPgo8MDAwMT4gPDAwNDI+IFs8MDA0Mj4gPDAwNEU+IDwwMDQ2PiA8MDAyMD4gPDAwNjQ+IDwwMDY1PiA8MDA0Qz4gPDAwNjk+IDwwMDczPiA8MDA3MD4gPDAwMjg+IDwwMDZFPiA8MDBFMz4gPDAwNkY+IDwwMDYzPiA8MDBFRD4gPDAwNjY+IDwwMDYxPiA8MDA3Mj4gPDAwNDM+IDwwMDZEPiA8MDAyOT4gPDAwNTA+IDwwMEY1PiA8MDA3ND4gPDAwNzY+IDwwMDJDPiA8MDA3NT4gPDAwNzk+IDwwMDcxPiA8MDAyRT4gPDAwNDk+IDwwMDUyPiA8MDA2Mj4gPDAwNkM+IDwwMEU5PiA8MDA0NT4gPDAwNjg+IDwyMDFDPiA8MjAxRD4gPDAwNkE+IDwwMDQ0PiA8MDA3QT4gPDAwRTE+IDwwMEU3PiA8MDA0MT4gPDAwNjc+IDwwMEYzPiA8MDAzQT4gPDAwMkE+IDwwMDUzPiA8MDBFMj4gPDAwNTQ+IDwwMDc4PiA8MDA1NT4gPDAwRUE+IDwwMDJGPiA8MDA3Nz4gPDAwNkI+IDwwMDQ4PiA8MDA0Nz4gPDAwMzE+IDwwMDJEPiA8MDAzNz4gPDAwMzM+IDwwMDM1PiBdCmVuZGJmcmFuZ2UKZW5kY21hcApDTWFwTmFtZSBjdXJyZW50ZGljdCAvQ01hcCBkZWZpbmVyZXNvdXJjZSBwb3AK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IDwwMDc4PiA8MDA3MD4gPDAwNzI+IDwwMDY5PiA8MDA2Rj4gPDAwNkU+IDwwMDIwPiA8MDAzRD4gPDAwNjE+IDwwMDc0PiA8MDA2RD4gPDAwNjM+IDwwMDc5PiA8MDA2Mj4gPDAwNkM+IDwwMDVDPiA8MDAyRj4gPDAwMjI+IDwwMDI4PiA8MDAyRT4gPDAwMjk+IDwwMDNDPiA8MDAzRT4gPDAwNzU+IDwwMDdBPiA8MDAzMT4gPDAwMzI+IDwwMDM5PiA8MDA2ND4gPDAwNjY+IDwwMDc2PiA8MDAyQT4gPDAwMzU+IDwwMDUzPiA8MDA1MD4gPDAwNDU+IDwwMDQzPiA8MDA0OT4gPDAwNDE+IDwwMDRDPiBdCmVuZGJmcmFuZ2UKZW5kY21hcApDTWFwTmFtZSBjdXJyZW50ZGljdCAvQ01hcCBkZWZpbmVyZXNvdXJjZSBwb3AKZW5kCmVuZAoKZW5kc3RyZWFtCmVuZG9iago3IDAgb2JqCjw8IC9UeXBlIC9Gb250Ci9TdWJ0eXBlIC9UeXBlMAovQmFzZUZvbnQgL1NvdXJjZUNvZGVQcm8tUmVndWxhcgovRW5jb2RpbmcgL0lkZW50aXR5LUgKL0Rlc2NlbmRhbnRGb250cyBbNDUgMCBSXQovVG9Vbmljb2RlIDQ2IDAgUj4+CmVuZG9iagoyIDAgb2JqCjw8Ci9UeXBlIC9QYWdlcwovS2lkcyAKWwo1IDAgUgoxNSAwIFIKXQovQ291bnQgMgovUHJvY1NldCBbL1BERiAvVGV4dCAvSW1hZ2VCIC9JbWFnZUNdCj4+CmVuZG9iagp4cmVmCjAgNDgKMDAwMDAwMDAwMCA2NTUzNSBmIAowMDAwMDAwMDA5IDAwMDAwIG4gCjAwMDAwMzAyMjEgMDAwMDAgbiAKMDAwMDAwMDE5MyAwMDAwMCBuIAowMDAwMDAwMjg4IDAwMDAwIG4gCjAwMDAwMDA0NzkgMDAwMDAgbiAKMDAwMDAxOTgyNiAwMDAwMCBuIAowMDAwMDMwMDc0IDAwMDAwIG4gCjAwMDAwMDAzMjUgMDAwMDAgbiAKMDAwMDAwMDM3NiAwMDAwMCBuIAowMDAwMDAwNDI3IDAwMDAwIG4gCjAwMDAwMDA3OTYgMDAwMDAgbiAKMDAwMDAwMzA4NCAwMDAwMCBuIAowMDAwMDAwNjAwIDAwMDAwIG4gCjAwMDAwMDA3NzYgMDAwMDAgbiAKMDAwMDAwNTU5MCAwMDAwMCBuIAowMDAwMDAzMTA1IDAwMDAwIG4gCjAwMDAwMDMxNTcgMDAwMDAgbiAKMDAwMDAwMzIwOSAwMDAwMCBuIAowMDAwMDAzMjYxIDAwMDAwIG4gCjAwMDAwMDMzMTMgMDAwMDAgbiAKMDAwMDAwMzU0MSAwMDAwMCBuIAowMDAwMDAzNzU4IDAwMDAwIG4gCjAwMDAwMDM5OTMgMDAwMDAgbiAKMDAwMDAwNDIzMiAwMDAwMCBuIAowMDAwMDA1NDIzIDAwMDAwIG4gCjAwMDAwMDQzOTkgMDAwMDAgbiAKMDAwMDAwNDc0OCAwMDAwMCBuIAowMDAwMDA0OTAyIDAwMDAwIG4gCjAwMDAwMDUwMzcgMDAwMDAgbiAKMDAwMDAwNTE2MiAwMDAwMCBuIAowMDAwMDA1MzAxIDAwMDAwIG4gCjAwMDAwMDQ1ODkgMDAwMDAgbiAKMDAwMDAwNTQ4NiAwMDAwMCBuIAowMDAwMDA1OTI2IDAwMDAwIG4gCjAwMDAwMDc0MjMgMDAwMDAgbiAKMDAwMDAwNTcxMiAwMDAwMCBuIAowMDAwMDA1ODc4IDAwMDAwIG4gCjAwMDAwMDc0NDQgMDAwMDAgbiAKMDAwMDAwNzY1OSAwMDAwMCBuIAowMDAwMDE4NDU5IDAwMDAwIG4gCjAwMDAwMTg5NDggMDAwMDAgbiAKMDAwMDAxODQzNyAwMDAwMCBuIAowMDAwMDE5OTczIDAwMDAwIG4gCjAwMDAwMjAxODYgMDAwMDAgbiAKMDAwMDAyOTEyMiAwMDAwMCBuIAowMDAwMDI5MzU3IDAwMDAwIG4gCjAwMDAwMjkxMDEgMDAwMDAgbiAKdHJhaWxlcgo8PAovU2l6ZSA0OAovSW5mbyAxIDAgUgovUm9vdCAzMyAwIFIKPj4Kc3RhcnR4cmVmCjMwMzI2CiUlRU9GCg==",
      "nome": "texto.pdf"
    }
  ]
}

     */
}
