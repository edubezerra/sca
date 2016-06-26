package br.cefetrj.sca.dominio;

import java.time.Duration;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import br.cefetrj.sca.dominio.atividadecomplementar.AtividadeComplementar;
import br.cefetrj.sca.dominio.atividadecomplementar.EnumEstadoAtividadeComplementar;
import br.cefetrj.sca.dominio.atividadecomplementar.EnumTipoAtividadeComplementar;
import br.cefetrj.sca.dominio.atividadecomplementar.RegistroAtividadeComplementar;

@Entity
public class Aluno {

	private static final int TAM_MIN_MATRICULA = 10;
	private static final int TAM_MAX_MATRICULA = 11;

	@Id
	@GeneratedValue
	private Long id;

	/**
	 * Matrícula do aluno, composta de <code>TAM_MATRICULA</code> carateres.
	 */
	private String matricula;

	@Embedded
	Pessoa pessoa;
	
	@OneToOne
	ProcessoIsencao processoIsencao;

	@OneToOne(cascade = CascadeType.ALL)
	private HistoricoEscolar historico;

	@ManyToOne
	private VersaoCurso versaoCurso;
	
	/**
	 * Registros de atividade complementar deste aluno.
	 */
	@OneToMany(cascade = CascadeType.ALL)
	// CascadeType.ALL necessário para persistir Alunos que fizeram registros de atividade complementar
	@JoinColumn(name = "ALUNO_ID", referencedColumnName = "ID")
	private Set<RegistroAtividadeComplementar> registrosAtiv = new HashSet<>();

	public Long getId() {
		return id;
	}

	@SuppressWarnings("unused")
	private Aluno() {
	}

	public Aluno(String nome, String cpf, String matricula,
			VersaoCurso versaoCurso) {
		if (cpf == null || cpf.equals("")) {
			throw new IllegalArgumentException("CPF deve ser fornecido.");
		}

		if (nome == null || nome.equals("")) {
			throw new IllegalArgumentException("Nome não pode ser vazio.");
		}
		if (matricula == null || matricula.equals("")) {
			throw new IllegalArgumentException("Matrícula não pode ser vazia.");
		}
		if (!(matricula.length() >= TAM_MIN_MATRICULA && matricula.length() <= TAM_MAX_MATRICULA)) {
			throw new IllegalArgumentException("Matrícula deve ter entre "
					+ TAM_MIN_MATRICULA + " e " + TAM_MAX_MATRICULA
					+ " caracteres: " + matricula);
		}
		this.pessoa = new Pessoa(nome, cpf);
		this.matricula = matricula;
		this.versaoCurso = versaoCurso;
		this.historico = new HistoricoEscolar(this.versaoCurso);
	}

	public Aluno(String nome, String cpf, String matricula,
			VersaoCurso versaoCurso, Date dataNascimento, String enderecoEmail) {
		this(nome, cpf, matricula, versaoCurso);
		this.pessoa = new Pessoa(nome, dataNascimento, enderecoEmail);
	}

	public String getNome() {
		return this.pessoa.getNome();
	}

	public Date getDataNascimento() {
		return pessoa.getDataNascimento();
	}

	public String getMatricula() {
		return matricula;
	}

	public String getEmail() {
		if (pessoa.getEmail() != null) {
			return pessoa.getEmail().toString();
		} else {
			return null;
		}
	}

	public String getCpf() {
		return this.pessoa.getCpf();
	}

	public void setVersaoCurso(VersaoCurso versaoCurso) {
		if (versaoCurso == null) {
			throw new IllegalArgumentException("Curso deve ser definido!");
		}
		this.versaoCurso = versaoCurso;
	}

	public VersaoCurso getVersaoCurso() {
		return versaoCurso;
	}

	public List<Disciplina> getDisciplinasPossiveis() {
		return historico.getDisciplinasPossiveis();
	}

	public HistoricoEscolar getHistorico() {
		return historico;
	}

	public void registrarMatricula(Turma turma) {
		this.historico.lancar(turma.getDisciplina(),
				EnumSituacaoAvaliacao.MATRICULA, turma.getPeriodo());
	}

	public void registrarNoHistoricoEscolar(Disciplina disciplina,
			EnumSituacaoAvaliacao situacaoFinal, PeriodoLetivo semestre) {
		HistoricoEscolar he = this.getHistorico();
		he.lancar(disciplina, situacaoFinal, semestre);
	}

	@Override
	public String toString() {
		return "Aluno [matricula=" + matricula + ", pessoa=" + pessoa + "]";
	}
	
	// Métodos relacionados às atividades complementares do aluno.
	/**
	 * Retorna todos os registros de atividade complementar deste aluno.
	 */
	public Set<RegistroAtividadeComplementar> getRegistrosAtiv() {
		return Collections.unmodifiableSet(this.registrosAtiv);
	}
	
	/**
	 * Retorna os registros deste aluno da atividade complementar passada como parâmetro.
	 */
	public Set<RegistroAtividadeComplementar> getRegistrosAtiv(AtividadeComplementar atividade) {
		Set<RegistroAtividadeComplementar> regsAtiv = new HashSet<>();
		for (RegistroAtividadeComplementar reg : registrosAtiv) {
			if(reg.getAtividade().equals(atividade))
				regsAtiv.add(reg);
		}
		return Collections.unmodifiableSet(regsAtiv);
	}
	
	/**
	 * Retorna os registros deste aluno das atividades complementares da categoria passada como parâmetro.
	 */
	public Set<RegistroAtividadeComplementar> getRegistrosAtiv(EnumTipoAtividadeComplementar categoria) {
		Set<RegistroAtividadeComplementar> regsAtiv = new HashSet<>();
		for (RegistroAtividadeComplementar reg : registrosAtiv) {
			if( reg.getAtividade().getTipo().getCategoria().equals(categoria) )
				regsAtiv.add(reg);
		}
		return Collections.unmodifiableSet(regsAtiv);
	}
	
	/**
	 * Retorna os registros deste aluno que se encontram no estado passado como parâmetro.
	 */
	public Set<RegistroAtividadeComplementar> getRegistrosAtiv(EnumEstadoAtividadeComplementar status) {
		Set<RegistroAtividadeComplementar> regsAtiv = new HashSet<>();
		for (RegistroAtividadeComplementar reg : registrosAtiv) {
			if( reg.getEstado().equals(status) )
				regsAtiv.add(reg);
		}
		return Collections.unmodifiableSet(regsAtiv);
	}
	
	/**
	 * Retorna o registro de atividade complementar especificado pelo id passado como parâmetro.
	 */
	public RegistroAtividadeComplementar getRegistroAtiv(Long idReg) {
		for (RegistroAtividadeComplementar reg : registrosAtiv) {
			if (reg.getId().equals(idReg)) {
				return reg;
			}
		}
		return null;
	}
	
	/**
	 * Retorna o conjunto de atividades complementares cumpridas por este aluno.
	 */
	public Set<AtividadeComplementar> getAtividades(){
		Set<AtividadeComplementar> atividades = new HashSet<>();
		for (RegistroAtividadeComplementar reg : registrosAtiv) {
			atividades.add(reg.getAtividade());
		}
		return Collections.unmodifiableSet(atividades);
	}
	
	/**
	 * Registra uma atividade complementar do aluno.
	 * 
	 *  RN09: Não deve ser possível o registro de uma atividade complementar se a sua carga horária máxima for excedida.
	 */
	public void registraAtividade(RegistroAtividadeComplementar reg) {
		
		if (registrosAtiv.contains(reg)) {
			throw new IllegalArgumentException(
					"Registro de atividade complementar duplicado.");
		}
		if ( this.getCargaHorariaCumpridaAtiv(reg.getAtividade()).plus(reg.getCargaHoraria()).toHours()
				> reg.getAtividade().getCargaHorariaMax().toHours() ) {
			throw new IllegalArgumentException(
					"A carga horária máxima da atividade complementar foi ultrapassada.");
		}
		
		this.registrosAtiv.add(reg);
	}
	
	/**
	 * Remove registro de atividade complementar passado como parâmetro.
	 * 
	 * RN08: O registro só pode ser removido se estiver com o estado "SUBMETIDO"
	 */
	public void removeRegAtividade(RegistroAtividadeComplementar reg) {
		if(reg == null) 
			throw new IllegalArgumentException(
					"O registro de atividade complementar não foi encontrado no conjunto de registros do aluno.");
		if(reg.podeSerCancelado())
			this.registrosAtiv.remove(reg);
		else
			throw new IllegalStateException(
					"O registro de atividade complementar não pode ser removido, pois já não se encontra no estado \"SUBMETIDO\".");		
	}
	
	/**
	 * Remove registro de atividade complementar especificado pelo id passado como parâmetro.
	 * 
	 * RN08: O registro só pode ser removido se estiver com o estado "SUBMETIDO"
	 */
	public void removeRegAtividade(Long idReg) {
		RegistroAtividadeComplementar reg = getRegistroAtiv(idReg);		
		removeRegAtividade(reg);
	}
	
	/**
	 * Checa se o aluno tem registros de atividade complementar.
	 */
	public boolean temRegistrosAtiv() {
		return !registrosAtiv.isEmpty();
	}
	
	/**
	 * Retorna a quantidade de registros de atividade complementar do aluno.
	 */
	public int getQtdRegistrosAtiv() {
		return this.registrosAtiv.size();
	}
	
	/**
	 * Retorna a carga horária de atividades complementares cumprida pelo aluno.
	 */
	public Duration getCargaHorariaCumpridaAtiv() {
		Duration sumCargaHoraria = Duration.ofHours(0);
		for (RegistroAtividadeComplementar reg : registrosAtiv) {
			if(reg.getEstado() == EnumEstadoAtividadeComplementar.DEFERIDO){
				sumCargaHoraria = sumCargaHoraria.plus(reg.getCargaHoraria());
			}
		}
		return sumCargaHoraria;
	}
	
	/**
	 * Retorna a carga horária cumprida pelo aluno da atividade complementar passada como parâmetro.
	 */
	public Duration getCargaHorariaCumpridaAtiv(AtividadeComplementar atividade) {
		Duration sumCargaHoraria = Duration.ofHours(0);
		for (RegistroAtividadeComplementar reg : registrosAtiv) {
			if(reg.getAtividade().equals(atividade) &&
					reg.getEstado() == EnumEstadoAtividadeComplementar.DEFERIDO){
				sumCargaHoraria = sumCargaHoraria.plus(reg.getCargaHoraria());
			}
		}
		return sumCargaHoraria;
	}
	
	/**
	 * Checa se o aluno cumpriu a carga horária mínima de atividades complementares do curso.
	 */
	public boolean temCargaHorariaMinimaAtividades(){
		return (getCargaHorariaCumpridaAtiv().toHours() >= versaoCurso.getCargaHorariaMinAitvComp().toHours());
	}
	
	/**
	 * Checa se o aluno cumpriu a carga horária mínima da atividade complementar passada como parâmetro.
	 */
	public boolean temCargaHorariaMinimaAtividade(AtividadeComplementar ativ){
		return (getCargaHorariaCumpridaAtiv(ativ).toHours() >= ativ.getCargaHorariaMin().toHours());
	}
	
	/**
	 * Checa se o aluno já cumpriu a carga horária máxima da atividade complementar passada como parâmetro.
	 */
	public boolean temCargaHorariaMaximaAtividade(AtividadeComplementar ativ){
		return !(getCargaHorariaCumpridaAtiv(ativ).toHours() < ativ.getCargaHorariaMax().toHours());
	}
		
	/**
	 * Checa se o aluno cumpriu carga horária de atividades complementares suficiente para se formar.
	 */
	public boolean temAtividadesSuficientes(){
		if(!temCargaHorariaMinimaAtividades()){
			return false;
		}
		for(AtividadeComplementar ativ : versaoCurso.getTabelaAtividades().getAtividades()){
			if(!temCargaHorariaMinimaAtividade(ativ)){
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Checa se o registro de atividade complementar passado como parâmetro pode ser DEFERIDO, 
	 * de maneira que a carga horária cumprida pelo aluno não ultrapasse a carga horária máxima
	 * da atividade complementar correspondente.
	 */
	public boolean podeTerRegistroDeferido(RegistroAtividadeComplementar reg) {
		
		if ( this.getCargaHorariaCumpridaAtiv(reg.getAtividade()).plus(reg.getCargaHoraria()).toHours()
				> reg.getAtividade().getCargaHorariaMax().toHours() ) {
			return false;
		}
		
		return true;
	}

	public ProcessoIsencao getProcessoIsencao() {
		return processoIsencao;
	}

	public void setProcessoIsencao(ProcessoIsencao processoIsencao) {
		this.processoIsencao = processoIsencao;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((matricula == null) ? 0 : matricula.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Aluno other = (Aluno) obj;
		if (matricula == null) {
			if (other.matricula != null)
				return false;
		} else if (!matricula.equals(other.matricula))
			return false;
		return true;
	}
}
