package br.cefetrj.sca.infra.cargadados;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.cefetrj.sca.dominio.Aluno;
import br.cefetrj.sca.dominio.repositories.AlunoRepositorio;
import br.cefetrj.sca.dominio.repositories.PerfilUsuarioRepositorio;
import br.cefetrj.sca.dominio.repositories.UsuarioRepositorio;
import br.cefetrj.sca.dominio.usuarios.PerfilUsuario;
import br.cefetrj.sca.dominio.usuarios.Usuario;

@Component
public class ImportadorUsuariosAlunosUsandoCpfComoLogin {
	@Autowired
	private AlunoRepositorio alunoRepositorio;

	@Autowired
	private UsuarioRepositorio usuarioRepositorio;

	@Autowired
	private PerfilUsuarioRepositorio perfilUsuarioRepositorio;

	public String run() {

		StringBuilder response = new StringBuilder();

		int qtdCriados = 0;
		List<Aluno> alunos = alunoRepositorio.findAll();
		PerfilUsuario perfil = perfilUsuarioRepositorio.getPerfilUsuarioByNome("ROLE_ALUNO");
		if (perfil == null) {
			perfil = new PerfilUsuario("ROLE_ALUNO");
			perfilUsuarioRepositorio.save(perfil);
		}
		for (Aluno aluno : alunos) {
			String login = converterFormatosCPF(aluno.getCpf());
			Usuario usuario = usuarioRepositorio.findUsuarioByLogin(login);
			if (usuario == null) {
				usuario = new Usuario(aluno.getNome(), login, aluno.getMatricula(), aluno.getEmail(), new Date());
				usuario.getUserProfiles().add(perfil);
				usuarioRepositorio.save(usuario);
				qtdCriados++;
			}
		}

		response.append("Foram criados " + qtdCriados + " usuário(s) com perfil de aluno.");

		return response.toString();
	}

	/**
	 * No Moodle, o CPF do aluno (usado como login) é armazenado sem os pontos
	 * separadores, enquanto que os valores de CPFs provenientes do SIE possuem
	 * pontos. A instrução a seguir tem o propósito de uniformizar a
	 * representação.
	 */
	private String converterFormatosCPF(String cpf) {
		return cpf.replace(".", "");
	}
}
