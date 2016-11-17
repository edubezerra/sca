package br.cefetrj.sca.infra.cargadados;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.cefetrj.sca.dominio.Professor;
import br.cefetrj.sca.dominio.repositories.PerfilUsuarioRepositorio;
import br.cefetrj.sca.dominio.repositories.ProfessorRepositorio;
import br.cefetrj.sca.dominio.repositories.UsuarioRepositorio;
import br.cefetrj.sca.dominio.usuarios.PerfilUsuario;
import br.cefetrj.sca.dominio.usuarios.Usuario;

@Component
public class ImportadorUsuariosProfessoresUsandoMatriculaComoLogin {
	@Autowired
	private ProfessorRepositorio professorRepositorio;

	@Autowired
	private UsuarioRepositorio usuarioRepositorio;

	@Autowired
	private PerfilUsuarioRepositorio perfilUsuarioRepositorio;

	public String run() {

		StringBuilder response = new StringBuilder();

		int qtdCriados = 0;
		List<Professor> professores = professorRepositorio.findAll();
		PerfilUsuario perfil = perfilUsuarioRepositorio.getPerfilUsuarioByNome("ROLE_PROFESSOR");
		if (perfil == null) {
			perfil = new PerfilUsuario("ROLE_PROFESSOR");
			perfilUsuarioRepositorio.save(perfil);
		}
		for (Professor professor : professores) {
			String login = professor.getMatricula();
			Usuario usuario = usuarioRepositorio.findUsuarioByLogin(login);
			if (usuario == null) {
				usuario = new Usuario(professor.getNome(), login, professor.getMatricula(), professor.getEmail(), new Date());
				usuario.getUserProfiles().add(perfil);
				usuarioRepositorio.save(usuario);
				qtdCriados++;
			}
		}

		response.append("Foram criados " + qtdCriados + " usuário(s) com perfil de professor.");

		return response.toString();
	}
}
