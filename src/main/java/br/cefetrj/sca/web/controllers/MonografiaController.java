package br.cefetrj.sca.web.controllers;

import br.cefetrj.sca.dominio.ArquivosMultipart;
import br.cefetrj.sca.dominio.Monografia;
import br.cefetrj.sca.service.MonografiaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
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

            return "/monografias/index";

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


}
