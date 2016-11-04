package br.cefetrj.sca.web.controllers;

import br.cefetrj.sca.dominio.ArquivosMultipart;
import br.cefetrj.sca.dominio.Monografia;
import br.cefetrj.sca.service.MonografiaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by Alexandre Vicente on 07/09/16.
 */
@Controller
@RequestMapping("/monografias")
public class MonografiaController {

    @Autowired
    private MonografiaService service;

    protected Logger logger = Logger.getLogger(MonografiaController.class.getName());

    @RequestMapping(value = "/{*}", method = RequestMethod.GET)
    public String get(Model model) {
        model.addAttribute("error", "Erro: página não encontrada.");
        return "/homeView";
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(HttpServletRequest request, Model model) {
        try {
            String query = request.getParameter("q");
            if(query != null && query != ""){
                List<Monografia> results = service.buscarMonografias(query);
                model.addAttribute("queryResults", results);
                model.addAttribute("q", query);
            } else {
                model.addAttribute("tags", service.obterTags());
            }
            return "/monografias/index";

        } catch (Exception exc) {
            exc.printStackTrace();
            model.addAttribute("error", exc.getMessage());
            return "/homeView";

        }
    }

    @RequestMapping(value = "/minhas/", method = RequestMethod.GET)
    public String formMinhasMonografias(HttpServletRequest request, Model model) {
        try {

            List<Monografia> monografias = service.buscarMonografias("idAutor:\"-1\"");

            if(monografias.size() != 0){
                request.setAttribute("monografias", monografias);
            }

            return "/monografias/minhas";

        } catch (Exception exc) {

            model.addAttribute("error", exc.getMessage());
            return "/homeView";

        }
    }

    @RequestMapping(value = "/nova/", method = RequestMethod.GET)
    public String formNovaMonografia(HttpServletRequest request, Model model) {
        try {
            return "/monografias/nova";

        } catch (Exception exc) {

            model.addAttribute("error", exc.getMessage());
            return "/homeView";

        }
    }

    @RequestMapping(value = "/editar/", method = RequestMethod.GET)
    public String formEditarMonografia(HttpServletRequest request, Model model) {
        try {

            Monografia monografia = Monografia.get(request.getParameter("id"));
            request.setAttribute("monografia", monografia);

            return "/monografias/nova";

        } catch (Exception exc) {

            model.addAttribute("error", exc.getMessage());
            return "/homeView";

        }
    }

    @RequestMapping(value = "/visualizar/", method = RequestMethod.GET)
    public String formVisualizarMonografia(HttpServletRequest request, Model model) {
        try {

            String id = request.getParameter("id");
            Monografia monografia = Monografia.get(id);
            request.setAttribute("monografia", monografia);
            return "/monografias/visualizar";

        } catch (Exception exc) {

            model.addAttribute("error", exc.getMessage());
            return "/homeView";

        }
    }

    @RequestMapping(value = "/download/", method = RequestMethod.GET)
    public void downloadArquivo(HttpServletRequest request,
                           HttpServletResponse response) throws IOException {

        Monografia monografia = Monografia.get(request.getParameter("id"));
        String nomeArquivo = request.getParameter("arquivo");
        Monografia.Arquivo arquivoDownload = null;

        for(Monografia.Arquivo arquivo : monografia.getArquivos()){
            if (arquivo.getNome().equals(nomeArquivo)) {
                arquivoDownload = arquivo;
                break;
            }
        }

        byte[] conteudo = arquivoDownload.getConteudo();

        String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=\"%s\"",
                nomeArquivo);
        response.setHeader(headerKey, headerValue);

        // get output stream of the response
        OutputStream outStream = response.getOutputStream();

        outStream.write(conteudo);

        outStream.close();

    }

    @RequestMapping(value = "/salvar/", method = RequestMethod.POST)
    public String salvarMonograia(HttpServletRequest request, String id, String titulo,
                                  String orientador, String presidenteBanca,
                                  String membrosBanca, String resumoLinguaEstrangeira,
                                  String resumoPortugues, String arquivosRemovidos,
                                  ArquivosMultipart arquivos, Model model, HttpServletResponse response) {
        try {
            Monografia monografia = service.obterMonografia(
                    id,
                    -1L,
                    "Autor de teste",
                    orientador,
                    presidenteBanca,
                    membrosBanca.split("\n"),
                    titulo,
                    resumoLinguaEstrangeira,
                    resumoPortugues
            );
            monografia.removerArquivos(Arrays.asList(arquivosRemovidos.split("\n")));
            monografia.adicionarArquivos(arquivos);
            monografia.salvar();

            request.setAttribute("monografia", monografia);
            response.sendRedirect("/monografias/visualizar/?id="+monografia.getId());

            return "/monografias/visualizar";

        } catch (Exception exc) {

            model.addAttribute("error", exc.getMessage());
            return "/homeView";

        }
    }


}
