package br.cefetrj.sca.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.cefetrj.sca.dominio.FichaAvaliacoes;
import br.cefetrj.sca.dominio.FichaAvaliacoesFabrica;
import br.cefetrj.sca.dominio.LancamentoNotasFechado;
import br.cefetrj.sca.dominio.PeriodoLetivo;
import br.cefetrj.sca.dominio.Turma;
import br.cefetrj.sca.dominio.repositories.TurmaRepositorio;

@Service
public class LancarAvaliacoesService implements ApplicationEventPublisherAware {

	@Autowired
	TurmaRepositorio turmaRepositorio;

	@Autowired
	FichaAvaliacoesFabrica fabrica;

	private ApplicationEventPublisher publisher;

	List<Turma> iniciarLancamentoNotas(String matriculaProfessor) {
		return turmaRepositorio.findTurmasLecionadasPorProfessorEmPeriodo(
				matriculaProfessor, PeriodoLetivo.PERIODO_CORRENTE);
	}

	FichaAvaliacoes solicitarLancamento(Long idTurma) {
		FichaAvaliacoes f = fabrica.criar(idTurma);

		// TODO: habilitar o lançamento apenas para alunos cujas avaliações
		// ainda
		// não foram encerradas.

		return f;
	}

	@Transactional
	void registrarLancamentos(FichaAvaliacoes ficha) {
		Turma turma = turmaRepositorio.findTurmaById(ficha.getIdTurma());

		ficha.lancarAvaliacoesEmTurma(turma);

		// TODO: habilitar o lançamento apenas para alunos cujas avaliações
		// ainda
		// não foram encerradas.

		// TODO: se algum dado de avaliação foi apagado pelo professor, refletir
		// isso no modelo.

		turmaRepositorio.save(turma);
	}

	@Transactional
	void encerrarLancamentos(FichaAvaliacoes ficha) {
		Turma turma = turmaRepositorio.findTurmaById(ficha.getIdTurma());
		publisher.publishEvent(new LancamentoNotasFechado(this, turma, null));

		// TODO: atualizar o *histórico escolar* de cada aluno. Atneção:
		// atualizar apenas os HEs dos alunos marcados para encerramento.

		// TODO: quando todos os alunos da turma tiverem sua avaliação
		// encerrada, marcar a turma como "fechada para lançamento". Usar tipo
		// enumerado SituacaoTurma definido na classe Turma.

		turmaRepositorio.save(turma);
	}

	@Override
	public void setApplicationEventPublisher(ApplicationEventPublisher publisher) {
		this.publisher = publisher;
	}
}
