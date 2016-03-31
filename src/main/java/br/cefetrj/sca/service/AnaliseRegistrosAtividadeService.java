package br.cefetrj.sca.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import br.cefetrj.sca.dominio.Aluno;
import br.cefetrj.sca.dominio.Professor;
import br.cefetrj.sca.dominio.VersaoCurso;
import br.cefetrj.sca.dominio.atividadecomplementar.EnumEstadoAtividadeComplementar;
import br.cefetrj.sca.dominio.atividadecomplementar.RegistroAtividadeComplementar;
import br.cefetrj.sca.dominio.inclusaodisciplina.Comprovante;
import br.cefetrj.sca.dominio.repositories.AlunoRepositorio;
import br.cefetrj.sca.dominio.repositories.CursoRepositorio;
import br.cefetrj.sca.dominio.repositories.ProfessorRepositorio;
import br.cefetrj.sca.dominio.repositories.RegistroAtividadeComplementarRepositorio;
import br.cefetrj.sca.service.util.SolicitaRegistroAtividadesResponse;

@Component
public class AnaliseRegistrosAtividadeService {
	
	@Autowired
	private AlunoRepositorio alunoRepo;
	
	@Autowired
	private RegistroAtividadeComplementarRepositorio regRepo;
	
	@Autowired
	private ProfessorRepositorio professorRepositorio;
	
	@Autowired
	private CursoRepositorio cursoRepo;
	
	public void homeAnaliseAtividades(String matricula, Model model){
		Professor professor = getProfessorByMatricula(matricula);
				
		model.addAttribute("nomeProfessor", professor.getNome());
		model.addAttribute("matriculaProfessor", professor.getMatricula());
		
		HashMap<String, List<String>> versoesCursos = new HashMap<>();
		for(VersaoCurso versaoCurso: cursoRepo.obterTodosVersaoCurso()){
			if (versoesCursos.get(versaoCurso.getCurso().getSigla()) == null) {
				List<String> numerosVersoes = new ArrayList<>();
				numerosVersoes.add(versaoCurso.getNumero());
				versoesCursos.put(versaoCurso.getCurso().getSigla(),numerosVersoes);
			}
			else{
				versoesCursos.get(versaoCurso.getCurso().getSigla()).add(versaoCurso.getNumero());
			}
		}
		
		model.addAttribute("versoesCursos", versoesCursos);						
		model.addAttribute("status", EnumEstadoAtividadeComplementar.values());
	}
	
	public SolicitaRegistroAtividadesResponse listarRegistrosAtividade(String siglaCurso,
			String numeroVersao, String status){
		
		VersaoCurso versaoCurso = cursoRepo.getVersaoCurso(siglaCurso, numeroVersao);
		List<Aluno> alunos = alunoRepo.findAllByVersaoCurso(versaoCurso);
		
		SolicitaRegistroAtividadesResponse response = new SolicitaRegistroAtividadesResponse();
		for (Aluno aluno : alunos) {
			for (RegistroAtividadeComplementar reg : 
				aluno.getRegistrosAtiv(EnumEstadoAtividadeComplementar.findByText(status))) {
				response.add(reg, aluno);
			}
		}
		return response;
	}
	
	public void atualizaStatusRegistro(String matriculaAluno, Long idRegistro, String status){
		
		Aluno aluno = getAlunoPorMatricula(matriculaAluno);
		RegistroAtividadeComplementar registroAtiv = regRepo.findRegistroAtividadeComplementarById(idRegistro);
		
		if(aluno.podeTerRegistroDeferido(registroAtiv)){
			registroAtiv.setEstado(EnumEstadoAtividadeComplementar.findByText(status));
		}

		regRepo.save(registroAtiv);
	}

	public Comprovante getComprovante(String matriculaAluno, Long idReg) {
		
		Aluno aluno = getAlunoPorMatricula(matriculaAluno);
		return aluno.getRegistroAtiv(idReg).getDocumento();
	}
	
	private Professor getProfessorByMatricula(String matricula){
		return professorRepositorio.findProfessorByMatricula(matricula);
	}
	
	private Aluno getAlunoPorMatricula(String matriculaAluno) {
		if (matriculaAluno == null || matriculaAluno.trim().equals("")) {
			throw new IllegalArgumentException("Matrícula deve ser fornecida!");
		}

		Aluno aluno = null;

		try {
			aluno = alunoRepo.findAlunoByMatricula(matriculaAluno);
		} catch (Exception e) {
			throw new IllegalArgumentException("Aluno não encontrado ("
					+ matriculaAluno + ")", e);
		}

		return aluno;
	}	
}
