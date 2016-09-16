package br.cefetrj.sca.dominio;

import org.springframework.security.access.method.P;
import org.springframework.web.multipart.MultipartFile;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.*;

/**
 * Created by Alexandre Vicente on 04/09/16.
 */
public class Monografia {
    private Long id;
    private List<String> autores;
    private String titulo;
    private String _abstract;
    private String resumo;
    private Set<Arquivo> arquivos = new HashSet<Arquivo>();

    public Monografia(Long id) {
        this.id = id;
    }

    public Monografia(List<String> autores, String titulo, String _abstract, String resumo) {
        this.autores = autores;
        this.titulo = titulo;
        this._abstract = _abstract;
        this.resumo = resumo;
    }

    public class Arquivo {
        private String nome;
        private String conteudo;
        private MultipartFile arquivoMultipart;

        public Arquivo(String nome) {
            this.nome = nome;
        }

        public Arquivo(String nome, String conteudo) {
            this.nome = nome;
            this.conteudo = conteudo;
        }

        public Arquivo(MultipartFile arquivoMultipart) {
            this.arquivoMultipart = arquivoMultipart;
            this.nome = arquivoMultipart.getName();
        }

        public String getNome() {
            return nome;
        }

        public String getConteudo() {
            return conteudo;
        }

        @Override
        public int hashCode() {
            return nome.hashCode() + 5;
        }
    }

    public Long getId() {
        return id;
    }

    public List<String> getAutores() {
        return autores;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getAbstract() {
        return _abstract;
    }

    public String getResumo() {
        return resumo;
    }

    public void adicionarArquivos(ArquivosMultipart arquivos){
        for(MultipartFile arquivo : arquivos.getArquivos()){
            if(!this.arquivos.add(new Arquivo(arquivo))){
                throw new IllegalArgumentException("Nome de arquivo duplicado (" + arquivo.getName() + ")");
            };
        }
    }

    public void salvar(){
       throw new NotImplementedException();
    }

}
