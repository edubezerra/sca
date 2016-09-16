package br.cefetrj.sca.dominio;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Created by Alexandre Vicente on 07/09/16.
 */
public class ArquivosMultipart {
    private List<MultipartFile> arquivos;

    public List<MultipartFile> getArquivos() {
        return arquivos;
    }

    public void setArquivos(List<MultipartFile> arquivos) {
        this.arquivos = arquivos;
    }
}