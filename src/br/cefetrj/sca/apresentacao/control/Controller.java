package br.cefetrj.sca.apresentacao.control;

import java.io.IOException;
import java.util.Hashtable;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.cefetrj.sca.apresentacao.cmds.AbstractCommand;
import br.cefetrj.sca.apresentacao.cmds.AdicionarDisciplinaCommand;
import br.cefetrj.sca.apresentacao.cmds.ObterDisciplinasCommand;
import br.cefetrj.sca.apresentacao.cmds.ValidarMatriculaProfessorCommand;

public class Controller extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static Hashtable<String, AbstractCommand> comandos;

	public void init(ServletConfig config) throws ServletException {

		super.init();

		comandos = new Hashtable<String, AbstractCommand>();
		comandos.put("ValidarMatriculaProfessor",
				new ValidarMatriculaProfessorCommand());
		comandos.put("ObterDisciplinas", new ObterDisciplinasCommand());
		comandos.put("AdicionarDisciplina", new AdicionarDisciplinaCommand());
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		execute(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		execute(request, response);
	}

	public void execute(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String cmd = (String) request.getAttribute("comando");
		if (cmd == null) {
			cmd = request.getParameter("comando");
		}
		AbstractCommand comando = (AbstractCommand) comandos.get(cmd);
		String pagina = comando.execute(request, response);
		request.getRequestDispatcher(pagina).forward(request, response);
	}
}
