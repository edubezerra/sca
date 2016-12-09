package br.cefetrj.sca.service;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.cefetrj.sca.dominio.Aluno;
import br.cefetrj.sca.dominio.Curso;
import br.cefetrj.sca.dominio.Professor;
import br.cefetrj.sca.dominio.VersaoCurso;
import br.cefetrj.sca.dominio.atividadecomplementar.RegistroAtividadeComplementar;
import br.cefetrj.sca.dominio.matriculaforaprazo.Comprovante;
import br.cefetrj.sca.dominio.repositories.AlunoRepositorio;
import br.cefetrj.sca.dominio.repositories.CursoRepositorio;
import br.cefetrj.sca.dominio.repositories.ProfessorRepositorio;
import br.cefetrj.sca.dominio.repositories.RegistroAtividadeComplementarRepositorio;
import br.cefetrj.sca.service.util.FormPesquisaSolicitacaoDiscente;
import br.cefetrj.sca.service.util.EnumEstadoSolicitacao;
import br.cefetrj.sca.service.util.SolicitaRegistroAtividadesResponse;

@Component
public class AnalisePedidoIsencaoService {

	@Autowired
	private AlunoRepositorio alunoRepo;

	@Autowired
	private RegistroAtividadeComplementarRepositorio regRepo;

	@Autowired
	private ProfessorRepositorio professorRepositorio;

	@Autowired
	private CursoRepositorio cursoRepo;

	public List<String> obterVersoesCurso(String siglaCurso) {

		List<String> versoesCursos = new ArrayList<>();
		for (VersaoCurso versaoCurso : cursoRepo.findAllVersaoCursoByCurso(siglaCurso)) {
			versoesCursos.add(versaoCurso.getNumero());
		}

		return versoesCursos;
	}

	public FormPesquisaSolicitacaoDiscente homeAnaliseAtividades(String matricula) {
		Professor professor = getProfessorByMatricula(matricula);

		Set<String> siglaCursos = new HashSet<String>();

		for (Curso curso : cursoRepo.findAllCursoByCoordenadorAtividades(professor.getMatricula())) {
			siglaCursos.add(curso.getSigla());
		}

		FormPesquisaSolicitacaoDiscente dadosAnaliseAtividades = new FormPesquisaSolicitacaoDiscente(professor.getNome(),
				professor.getMatricula(), siglaCursos, EnumEstadoSolicitacao.values());

		return dadosAnaliseAtividades;
	}

	public SolicitaRegistroAtividadesResponse listarRegistrosAtividade(String matricula, String siglaCurso,
			String numeroVersao, String status, String startDate, String endDate) {

		Professor professor = getProfessorByMatricula(matricula);
		List<Aluno> alunos = new ArrayList<>();

		if (!siglaCurso.equals("") && !numeroVersao.equals("")) {
			VersaoCurso versaoCurso = cursoRepo.getVersaoCurso(siglaCurso, numeroVersao);
			alunos = alunoRepo.findAllByVersaoCurso(versaoCurso);
		} else if (!siglaCurso.equals("") && numeroVersao.equals("")) {
			for (VersaoCurso versaoCurso : cursoRepo.findAllVersaoCursoByCurso(siglaCurso)) {
				alunos.addAll(alunoRepo.findAllByVersaoCurso(versaoCurso));
			}
		} else if (siglaCurso.equals("") && numeroVersao.equals("")) {
			for (VersaoCurso versaoCurso : cursoRepo
					.findAllVersaoCursoByCoordenadorAtividades(professor.getMatricula())) {
				alunos.addAll(alunoRepo.findAllByVersaoCurso(versaoCurso));
			}
		}

		SolicitaRegistroAtividadesResponse response = new SolicitaRegistroAtividadesResponse();
		if (!status.equals("") && startDate.equals("") && endDate.equals("")) {
			for (Aluno aluno : alunos) {
				for (RegistroAtividadeComplementar reg : aluno
						.getRegistrosAtiv(EnumEstadoSolicitacao.findByText(status))) {
					response.add(reg, aluno);
				}
			}
		} else if (!status.equals("") && !startDate.equals("") && !endDate.equals("")) {
			DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT);
			Date dataInicio, dataFim;
			try {
				dataInicio = df.parse(startDate);
				Calendar c = Calendar.getInstance();
				c.setTime(df.parse(endDate));
				c.add(Calendar.DATE, 1); // number of days to add
				dataFim = c.getTime();
				// dataFim = df.parse(endDate);
			} catch (ParseException e) {
				throw new IllegalArgumentException("Intervalo dos registros buscados mal definido!", e);
			}
			for (Aluno aluno : alunos) {
				for (RegistroAtividadeComplementar reg : aluno
						.getRegistrosAtiv(EnumEstadoSolicitacao.findByText(status))) {
					if (reg.getDataSolicitacao().compareTo(dataInicio) >= 0
							&& reg.getDataSolicitacao().compareTo(dataFim) <= 0)
						response.add(reg, aluno);
				}
			}
		} else if (status.equals("") && !startDate.equals("") && !endDate.equals("")) {
			DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT);
			Date dataInicio, dataFim;
			try {
				dataInicio = df.parse(startDate);
				Calendar c = Calendar.getInstance();
				c.setTime(df.parse(endDate));
				c.add(Calendar.DATE, 1); // number of days to add
				dataFim = c.getTime();
				// dataFim = df.parse(endDate);
			} catch (ParseException e) {
				throw new IllegalArgumentException("Intervalo dos registros pesquisados mal definido!", e);
			}
			for (Aluno aluno : alunos) {
				for (RegistroAtividadeComplementar reg : aluno.getRegistrosAtiv()) {
					if (reg.getDataSolicitacao().compareTo(dataInicio) >= 0
							&& reg.getDataSolicitacao().compareTo(dataFim) <= 0)
						response.add(reg, aluno);
				}
			}
		} else if (status.equals("") && startDate.equals("") && endDate.equals("")) {
			for (Aluno aluno : alunos) {
				for (RegistroAtividadeComplementar reg : aluno.getRegistrosAtiv()) {
					response.add(reg, aluno);
				}
			}
		}

		return response;
	}

	public void atualizaStatusRegistro(String matriculaProfessor, String matriculaAluno, Long idRegistro, String status,
			String justificativa) {

		Professor professor = getProfessorByMatricula(matriculaProfessor);
		Aluno aluno = getAlunoPorMatricula(matriculaAluno);
		RegistroAtividadeComplementar registroAtiv = regRepo.findRegistroAtividadeComplementarById(idRegistro);

		if (EnumEstadoSolicitacao.findByText(status).equals(EnumEstadoSolicitacao.DEFERIDO)) {
			if (aluno.podeTerRegistroDeferido(registroAtiv)) {
				registroAtiv.setEstado(EnumEstadoSolicitacao.findByText(status));
				registroAtiv.setDataAnalise(Calendar.getInstance().getTime());
				registroAtiv.setAvaliador(professor);
				registroAtiv.setJustificativa(justificativa);
			} else {
				throw new IllegalArgumentException(
						"Registro de atividade complementar não pode ser deferido! Carga horária máxima atingida!");
			}
		} else {
			registroAtiv.setEstado(EnumEstadoSolicitacao.findByText(status));
			registroAtiv.setDataAnalise(Calendar.getInstance().getTime());
			registroAtiv.setAvaliador(professor);
			registroAtiv.setJustificativa(justificativa);
		}

		regRepo.save(registroAtiv);
	}

	public Comprovante getComprovante(String matriculaAluno, Long idReg) {

		Aluno aluno = getAlunoPorMatricula(matriculaAluno);
		return aluno.getRegistroAtiv(idReg).getDocumento();
	}

	private Professor getProfessorByMatricula(String matricula) {
		return professorRepositorio.findProfessorByMatricula(matricula);
	}

	private Aluno getAlunoPorMatricula(String matriculaAluno) {
		if (matriculaAluno == null || matriculaAluno.trim().equals("")) {
			throw new IllegalArgumentException("Matrícula do aluno deve ser fornecida!");
		}

		Aluno aluno = null;

		try {
			aluno = alunoRepo.findAlunoByMatricula(matriculaAluno);
		} catch (Exception e) {
			throw new IllegalArgumentException("Aluno não encontrado (" + matriculaAluno + ")", e);
		}

		return aluno;
	}
}
