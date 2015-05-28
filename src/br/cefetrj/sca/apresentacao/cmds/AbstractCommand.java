package br.cefetrj.sca.apresentacao.cmds;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface AbstractCommand {
	public abstract String execute(HttpServletRequest request,
			HttpServletResponse response);
}
