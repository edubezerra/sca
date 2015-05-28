package br.cefetrj.sca.apresentacao.cmds;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.cefetrj.sca.dominio.Professor;
import br.cefetrj.sca.service.FornecerGradeService;

@Service
public class ValidarMatriculaProfessorCommand implements AbstractCommand {

	@Autowired
	FornecerGradeService fgService;

	public String execute(HttpServletRequest request,
			HttpServletResponse response) {
		Professor professor;
		String matriculaProfessor = request.getParameter("matriculaProfessor");
		professor = fgService.validarProfessor(matriculaProfessor);
		request.setAttribute("professor", professor);
		String pagina = "/apresentarFormGradeDisponibilidade.jsp";
		return pagina;
	}
}
