package br.cefetrj.sca.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.cefetrj.sca.dominio.Aluno;
import br.cefetrj.sca.dominio.Disciplina;
import br.cefetrj.sca.dominio.Professor;
import br.cefetrj.sca.dominio.VersaoCurso;
import br.cefetrj.sca.dominio.isencoes.FichaIsencaoDisciplinas;
import br.cefetrj.sca.dominio.isencoes.ItemPedidoIsencaoDisciplina;
import br.cefetrj.sca.dominio.isencoes.PedidoIsencaoDisciplinas;
import br.cefetrj.sca.dominio.repositories.AlunoRepositorio;
import br.cefetrj.sca.dominio.repositories.DisciplinaRepositorio;
import br.cefetrj.sca.dominio.repositories.PedidoIsencaoDisciplinasRepositorio;
import br.cefetrj.sca.dominio.repositories.ProfessorRepositorio;

@Service
public class PedidoIsencaoDisciplinasServiceDEPRECATED {

	@Autowired
	private AlunoRepositorio alunoRepo;

	@Autowired
	private ProfessorRepositorio professorRepo;

	@Autowired
	private DisciplinaRepositorio disciplinaRepo;

	@Autowired
	private PedidoIsencaoDisciplinasRepositorio pedidoIsencaoDisciplinasRepo;

	public Aluno findAlunoByMatricula(String matricula) {
		return alunoRepo.findAlunoByMatricula(matricula);
	}

	public Professor findProfessorByMatricula(String matricula) {
		return professorRepo.findProfessorByMatricula(matricula);
	}

	public Disciplina getDisciplinaPorId(Long idDisciplina) {
		return disciplinaRepo.findDisciplinaById(idDisciplina);
	}

	public List<Disciplina> findDisciplinas(VersaoCurso versaoCurso) {
		return disciplinaRepo.findAllEmVersaoCurso(versaoCurso);
	}

	public List<PedidoIsencaoDisciplinas> findProcessosIsencao() {
		return pedidoIsencaoDisciplinasRepo.findAll();
	}

	public List<Aluno> getTodosOsAlunos() {
		return alunoRepo.findAll();
	}

	public ItemPedidoIsencaoDisciplina findItemIsencaoById(Long solicitacaoId) {
		return pedidoIsencaoDisciplinasRepo.findItemIsencaoById(solicitacaoId);
	}

	public List<ItemPedidoIsencaoDisciplina> findItensIsencaoByDepartamentoAndAluno(Long idDepartamento,
			String matriculaAluno) {

		Aluno aluno = alunoRepo.findAlunoByMatricula(matriculaAluno);

		List<ItemPedidoIsencaoDisciplina> itensIsencao = new ArrayList<>();

		PedidoIsencaoDisciplinas processo = pedidoIsencaoDisciplinasRepo.findByAluno(aluno);

		// DivisorIsencoesDisciplinasService
		itensIsencao.addAll(processo.getItens());

		return itensIsencao;
	}

	public void registrarRespostaParaItem(String idItemPedidoIsencao, String matriculaAluno, String matriculaProfessor,
			String novaSituacao, String observacao) {
		Aluno alunoSolicitante = this.findAlunoByMatricula(matriculaAluno);
		Professor professorResponsavel = this.findProfessorByMatricula(matriculaProfessor);

		PedidoIsencaoDisciplinas pedido = pedidoIsencaoDisciplinasRepo.findByAluno(alunoSolicitante);

		pedido.registrarRespostaParaItem(idItemPedidoIsencao, professorResponsavel, novaSituacao, observacao);

		pedidoIsencaoDisciplinasRepo.save(pedido);
	}

	public FichaIsencaoDisciplinas findFichaIsencao(String matricula) {
		PedidoIsencaoDisciplinas pedido = pedidoIsencaoDisciplinasRepo.findByMatriculaAluno(matricula);
		Aluno aluno = alunoRepo.findAlunoByMatricula(matricula);
		return new FichaIsencaoDisciplinas(aluno, pedido);
	}
}
