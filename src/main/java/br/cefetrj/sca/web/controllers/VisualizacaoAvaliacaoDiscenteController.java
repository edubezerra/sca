package br.cefetrj.sca.web.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import br.cefetrj.sca.dominio.Professor;
import br.cefetrj.sca.dominio.Turma;
import br.cefetrj.sca.dominio.avaliacaoturma.AvaliacaoTurma;
import br.cefetrj.sca.dominio.repositories.ProfessorRepositorio;
import br.cefetrj.sca.dominio.repositories.TurmaRepositorio;
import br.cefetrj.sca.dominio.usuarios.Usuario;
import br.cefetrj.sca.service.VisualizacaoAvaliacaoDiscenteService;

@Controller
@RequestMapping("/visualizacaoAvaliacaoDiscente")
public class VisualizacaoAvaliacaoDiscenteController {
	@Autowired
	private VisualizacaoAvaliacaoDiscenteService discenteService;

	@Autowired
	private ProfessorRepositorio repositorio;

	@Autowired
	private TurmaRepositorio turmaRepositorio;

	@RequestMapping("/turma")
	public ModelAndView ApresentarTurma() {
		ModelAndView mv = new ModelAndView("visualizarAvaliacoesDocentes/ApresentacaoTurmas");
		Usuario usr = UsuarioController.getCurrentUser();
		String matricula = usr.getMatricula();

		Professor professor = repositorio.findProfessorByMatricula(matricula);

		List<Turma> t = discenteService.listarTurmaLecionadas(professor);

		mv.addObject("turmas", t);
		return mv;

	}

	@RequestMapping("/Escolhaturma")
	public ModelAndView EscolhaTurma(@RequestParam Long cod) {
		ModelAndView mv = new ModelAndView("visualizarAvaliacoesDocentes/GraficoAvaliacoes");
		Turma t = turmaRepositorio.findTurmaById(cod);

		String titulo = t.getNomeDisciplina() + " " + t.getCodigo();

		List<AvaliacaoTurma> at = discenteService.selecionarTurma(t);

		List<String> resp = discenteService.conversaoRespospa(at);

		List<String> respPos = discenteService.obterRespostasPos(at);

		List<String> respNeg = discenteService.obterRespostasNeg(at);

		mv.addObject("turma", t);
		mv.addObject("Respostas", resp);
		mv.addObject("respPos", respPos);
		mv.addObject("respNeg", respNeg);
		mv.addObject("titulo", titulo);
		return mv;

	}

}
