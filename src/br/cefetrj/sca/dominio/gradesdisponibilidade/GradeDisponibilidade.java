package br.cefetrj.sca.dominio.gradesdisponibilidade;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.springframework.beans.factory.annotation.Autowired;

import br.cefetrj.sca.dominio.Disciplina;
import br.cefetrj.sca.dominio.EnumDiaSemana;
import br.cefetrj.sca.dominio.ItemHorario;
import br.cefetrj.sca.dominio.ItensHorario;
import br.cefetrj.sca.dominio.Professor;
import br.cefetrj.sca.dominio.SemestreLetivo;
import br.cefetrj.sca.dominio.repositorio.DisciplinaRepositorio;

@Entity
public class GradeDisponibilidade implements Cloneable {

	@Id
	@GeneratedValue
	private Long id;

	@ManyToOne
	private Professor professor;

	@ManyToMany
	@JoinTable(name = "GRADEDISPONIBILIDADE_DISCIPLINA", joinColumns = { @JoinColumn(name = "GRADE_ID", referencedColumnName = "ID") }, inverseJoinColumns = { @JoinColumn(name = "DISCIPLINA_ID", referencedColumnName = "ID") })
	private Set<Disciplina> disciplinas = new HashSet<Disciplina>();

	@OneToMany(fetch=FetchType.EAGER)
	@JoinColumn(name = "GRADEDISPONIBILIDADE_ID", referencedColumnName = "ID")
	private Set<ItemHorario> horarios = new HashSet<ItemHorario>();

	@Transient
	private DisciplinaRepositorio r;

	@Embedded
	private SemestreLetivo semestre;

	@Transient
	@Autowired
	private ItensHorario itensHorario;

	@SuppressWarnings("unused")
	private GradeDisponibilidade() {
	}

	public GradeDisponibilidade(Professor professor, SemestreLetivo semestre) {
		if (professor == null) {
			throw new IllegalArgumentException("Professor deve ser fornecido.");
		}
		this.professor = professor;
		this.semestre = semestre;
	}

	public GradeDisponibilidade(Professor professor) {
		this.professor = professor;
		this.semestre = (SemestreLetivo) SemestreLetivo.SEMESTRE_LETIVO_CORRENTE;
	}

	public Long getId() {
		return id;
	}

	public Set<Disciplina> getDisciplinas() {
		return disciplinas;
	}

	public Set<ItemHorario> getItensDisponibilidade() {
		return horarios;
	}

	public SemestreLetivo getSemestre() {
		return semestre;
	}

	public Professor getProfessor() {
		return professor;
	}

	public Disciplina adicionarDisciplina(String nome) {
		Disciplina d;
		d = r.getDisciplina(nome);
		if (d == null) {
			throw new IllegalArgumentException(
					"Nome inv�lido para disciplina: " + "\"" + nome + "\".");
		}
		if (!this.professor.estaHabilitado(d)) {
			throw new IllegalArgumentException(
					"Professor n�o est� habilitado para disciplina \""
							+ d.getNome() + "\".");
		}
		adicionarDisciplina(d);
		return d;
	}

	private void adicionarDisciplina(Disciplina d) {
		this.disciplinas.add(d);
	}

	public void removerDisciplina(Disciplina disciplina) {
		disciplinas.remove(disciplina);
	}

	@Override
	public GradeDisponibilidade clone() {
		GradeDisponibilidade copia = new GradeDisponibilidade(this.professor,
				(SemestreLetivo) this.semestre);
		copia.setDisciplinas(this.disciplinas);
		for (ItemHorario item : horarios) {
			copia.horarios.add((ItemHorario) item);
		}
		return copia;
	}

	private void setDisciplinas(Set<Disciplina> disciplinas) {
		this.disciplinas = disciplinas;
	}

	public void adicionarItemHorario(String diaStr, String fim, String inicio) {
		EnumDiaSemana dia = EnumDiaSemana.findByText(diaStr);
		ItemHorario itemFornecido = itensHorario.getItem(dia, inicio, fim);
		for (ItemHorario item : horarios) {
			if (itemFornecido.equals(item)) {
				throw new IllegalArgumentException("Item já inserido.");
			} else if (itemFornecido.colide(item)) {
				throw new IllegalArgumentException("Colisão de horários");
			}
		}
		horarios.add(itemFornecido);
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("Semestre letivo: " + this.semestre);

		buffer.append("Disciplinas:\n");
		for (Disciplina disciplina : this.disciplinas) {
			buffer.append(disciplina.getNome() + "\n");
		}

		buffer.append("Hor�rios:\n");
		for (ItemHorario item : this.horarios) {
			buffer.append(item.getInicio() + " �s " + item.getFim() + "\n");
		}
		return buffer.toString();
	}

	public void adicionarItemHorario(EnumDiaSemana dia, String horaInicial,
			String horaFinal) {
		ItemHorario item;
		item = new ItemHorario(dia, horaInicial, horaFinal);
		this.horarios.add(item);
	}

	public int getQuantidadeDisciplinas() {
		return disciplinas.size();
	}

	public int getQuantidadeDisponibilidades() {
		return horarios.size();
	}
}