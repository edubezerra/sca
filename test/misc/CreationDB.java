package misc;

import java.math.BigInteger;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import br.cefetrj.sca.dominio.Curso;
import br.cefetrj.sca.dominio.Disciplina;
import br.cefetrj.sca.dominio.Professor;
import br.cefetrj.sca.dominio.PeriodoLetivo;
import br.cefetrj.sca.dominio.PeriodoLetivo.EnumPeriodo;
import br.cefetrj.sca.dominio.Turma;
import br.cefetrj.sca.dominio.VersaoCurso;
import br.cefetrj.sca.dominio.avaliacaoturma.Alternativa;
import br.cefetrj.sca.dominio.avaliacaoturma.Quesito;

@SuppressWarnings("all")
public class CreationDB {

	public static void main(String[] args) {

		EntityManagerFactory emf = Persistence
				.createEntityManagerFactory("SCAPU");
		EntityManager em = emf.createEntityManager();

		System.out.println("Inicio");

		em.getTransaction().begin();

		Professor prof1 = new Professor("1234567", "Eduardo Bezerra",
				"edubezerra@gmail.com");

		Curso curso = new Curso("WEB", "TECNOLOGIA EM SISTEMAS PARA INTERNET");
		VersaoCurso versao = new VersaoCurso("2012", curso);
		Disciplina disc1 = new Disciplina("MICROECONOMIA", "GADM7708", "2",
				"36");
		Disciplina disc2 = new Disciplina("SIMULAÇÕES EMPRESARIAIS",
				"GADM7731", "2", "36");
		Disciplina disc3 = new Disciplina("GESTÃO ESTRATÉGICA", "GADM7741",
				"2", "36");
		Disciplina disc4 = new Disciplina("ECONOMIA BRASILEIRA", "GADM7756",
				"2", "36");
		Disciplina disc5 = new Disciplina("ARQUITETURA DE COMPUTADORES",
				"GTSI1211", "4", "72");
		Disciplina disc6 = new Disciplina("PROGRAMAÇÃO DE CLIENTE WEB",
				"GTSI1212", "4", "72");
		Disciplina disc7 = new Disciplina(
				"PROJETO DE ALGORITMOS COMPUTACIONAIS", "GTSI1213", "4", "72");
		Disciplina disc8 = new Disciplina("LÓGICA MATEMÁTICA", "GTSI1214", "4",
				"72");
		Disciplina disc9 = new Disciplina("INTRODUÇÃO À ADMINISTRAÇÃO",
				"GTSI1215", "2", "36");
		Disciplina disc10 = new Disciplina("SISTEMAS OPERACIONAIS", "GTSI1221",
				"4", "72");
		Disciplina disc11 = new Disciplina("ESTRUTURAS DE DADOS", "GTSI1222",
				"4", "72");
		Disciplina disc12 = new Disciplina("MATEMÁTICA DISCRETA", "GTSI1223",
				"4", "72");
		Disciplina disc13 = new Disciplina("METODOLOGIA CIENTÍFICA",
				"GTSI1224", "2", "36");
		Disciplina disc14 = new Disciplina("ENGENHARIA DE REQUISITOS",
				"GTSI1231", "4", "72");
		Disciplina disc15 = new Disciplina(
				"FUNDAMENTOS DE REDES DE COMPUTADORES", "GTSI1232", "4", "72");
		Disciplina disc16 = new Disciplina("PROGRAMAÇÃO ORIENTADA A OBJETOS",
				"GTSI1233", "4", "72");
		Disciplina disc17 = new Disciplina("EMPREENDEDORISMO", "GTSI1234", "2",
				"36");
		Disciplina disc18 = new Disciplina("ESTATÍSTICA E PROBABILIDADE",
				"GTSI1235", "4", "72");
		Disciplina disc19 = new Disciplina("PROJETO E CONSTRUÇÃO DE SISTEMAS",
				"GTSI1254", "4", "72");
		Disciplina disc20 = new Disciplina("EXPRESSÃO ORAL E ESCRITA",
				"GTSI1273", "2", "36");
		Disciplina disc21 = new Disciplina("RESPONSABILIDADE SOCIAL",
				"GTSI1277", "2", "36");
		Disciplina disc22 = new Disciplina(
				"ARQUITETURAS AVANÇADAS DE COMPUTADORES", "GTSI1301", "2", "36");
		Disciplina disc23 = new Disciplina("SISTEMAS DIGITAS", "GTSI1302", "4",
				"72");
		Disciplina disc24 = new Disciplina("PROJETO DE BANCO DE DADOS",
				"GTSI7311", "4", "72");
		Disciplina disc25 = new Disciplina("MODELAGEM DE PROJETO DE SISTEMAS",
				"GTSI7312", "4", "72");
		Disciplina disc26 = new Disciplina("ADMINISTRAÇÃO DE BANCO DE DADOS",
				"GTSI7415", "4", "72");
		Disciplina disc27 = new Disciplina(
				"PROJETO DE SOFTWARE ORIENTADOS A OBJETOS", "GTSI7418", "4",
				"72");
		Disciplina disc28 = new Disciplina("PROGRAMAÇÃO DE SERVIDORES WEB",
				"GTSI7518", "4", "72");
		Disciplina disc29 = new Disciplina("PROJETO DE INTERFACES", "GTSI7519",
				"4", "72");
		Disciplina disc30 = new Disciplina("PADRÕES DE SOFTWARE", "GTSI7520",
				"4", "72");
		Disciplina disc31 = new Disciplina("ENGENHARIA DE SOFTWARE",
				"GTSI7521", "4", "72");
		Disciplina disc32 = new Disciplina("PROJETO FINAL I", "GTSI7523", "4",
				"72");
		Disciplina disc33 = new Disciplina("TÓPICOS AVANÇADOS EM INFORMÁTICA",
				"GTSI7624", "2", "36");
		Disciplina disc34 = new Disciplina("NEGÓCIOS NA INTERNET", "GTSI7625",
				"2", "36");
		Disciplina disc35 = new Disciplina("SEGURANÇA DA INFORMAÇÃO",
				"GTSI7626", "2", "36");
		Disciplina disc36 = new Disciplina("INFORMÁTICA E SOCIEDADE",
				"GTSI7627", "2", "36");
		Disciplina disc37 = new Disciplina("PROJETO FINAL II", "GTSI7628", "4",
				"72");
		Disciplina disc38 = new Disciplina(
				"ESTÁGIO SUPERVISIONADO (TECNÓLOGO EM WEB)", "GTSI7629", "8",
				"300");
		Disciplina disc39 = new Disciplina(
				"APLICAÇÕES NA INTERNET PARA TV DIGITAL INTERATIVA",
				"GTSI7702", "4", "72");

		Disciplina[] disciplinas = { disc1, disc2, disc3, disc4, disc5, disc6,
				disc7, disc8, disc9, disc10, disc11, disc12, disc13, disc14,
				disc15, disc16, disc17, disc18, disc19, disc20, disc21, disc22,
				disc23, disc24, disc25, disc26, disc27, disc28, disc29, disc30,
				disc31, disc32, disc33, disc34, disc35, disc36, disc37, disc38,
				disc39 };

		PeriodoLetivo semestreLetivo = new PeriodoLetivo(2013,
				EnumPeriodo.SEGUNDO);
		PeriodoLetivo semestreLetivo01 = new PeriodoLetivo(2013,
				EnumPeriodo.PRIMEIRO);
		PeriodoLetivo semestreLetivo02 = new PeriodoLetivo(2012,
				EnumPeriodo.SEGUNDO);
		PeriodoLetivo semestreLetivo03 = new PeriodoLetivo(2012,
				EnumPeriodo.PRIMEIRO);
		PeriodoLetivo semestreLetivo04 = new PeriodoLetivo(2011,
				EnumPeriodo.SEGUNDO);

		Turma turma1 = new Turma(disc1, "600006");
		Turma turma2 = new Turma(disc2, "600007");
		Turma turma3 = new Turma(disc3, "600009");
		Turma turma4 = new Turma(disc4, "600014");
		Turma turma5 = new Turma(disc5, "600001");
		Turma turma6 = new Turma(disc6, "600004");
		Turma turma7 = new Turma(disc7, "600012");
		Turma turma8 = new Turma(disc8, "600003");
		Turma turma9 = new Turma(disc9, "600013");
		Turma turma10 = new Turma(disc10, "610002");
		Turma turma11 = new Turma(disc11, "690020");
		Turma turma12 = new Turma(disc12, "640018");
		Turma turma13 = new Turma(disc13, "640018");
		Turma turma14 = new Turma(disc14, "630010");
		Turma turma15 = new Turma(disc15, "620008");
		Turma turma16 = new Turma(disc16, "620009");
		Turma turma17 = new Turma(disc17, "630011");
		Turma turma18 = new Turma(disc18, "630013");
		Turma turma19 = new Turma(disc19, "600011");
		Turma turma20 = new Turma(disc20, "600010");
		Turma turma21 = new Turma(disc21, "600024");
		Turma turma22 = new Turma(disc22, "600002");
		Turma turma23 = new Turma(disc23, "600005");
		Turma turma24 = new Turma(disc24, "620005");
		Turma turma25 = new Turma(disc25, "620006");
		Turma turma26 = new Turma(disc26, "640020");
		Turma turma27 = new Turma(disc27, "630011");
		Turma turma28 = new Turma(disc28, "640014");
		Turma turma29 = new Turma(disc29, "650001");
		Turma turma30 = new Turma(disc30, "680002");
		Turma turma31 = new Turma(disc31, "630020");
		Turma turma32 = new Turma(disc32, "640017");
		Turma turma33 = new Turma(disc33, "600008");
		Turma turma34 = new Turma(disc34, "680003");
		Turma turma35 = new Turma(disc35, "640019");
		Turma turma36 = new Turma(disc36, "660000");
		Turma turma37 = new Turma(disc37, "690010");
		Turma turma38 = new Turma(disc38, "690011");
		Turma turma39 = new Turma(disc39, "600030");

		Turma[] turmas = { turma1, turma2, turma3, turma4, turma5, turma6,
				turma7, turma8, turma9, turma10, turma11, turma12, turma13,
				turma14, turma15, turma16, turma17, turma18, turma19, turma20,
				turma21, turma22, turma23, turma24, turma25, turma26, turma27,
				turma28, turma29, turma30, turma31, turma32, turma33, turma34,
				turma35, turma36, turma37, turma38, turma39 };

		System.out.println("Persists");

	}

	public static List<Turma> getTurmasPreenchimento(EntityManager em,
			String matricula, PeriodoLetivo semestreLetivo) {

		String jpql = "SELECT T FROM Aluno A inner join A.turmas T where "
				+ " A.matricula =:matricula and T.semestreLetivo.ano =:semestreLetivo.ano and T.semestreLetivo.semestre =:semestreLetivo.semestre ";
		Query query = em.createQuery(jpql);

		query.setParameter("matricula", matricula);
		query.setParameter("semestreLetivo", semestreLetivo);

		List<Turma> turmas1 = query.getResultList();

		String jpql2 = "SELECT Q.turma FROM Aluno A inner join A.questionarios Q where A.matricula =:matricula and Q.semestreLetivo.semestre =:semestreLetivo.semestre and Q.semestreLetivo.ano =:semestreLetivo.ano and "
				+ " SIZE(Q.respostas) = (SELECT count(U) from Quesito U) ";
		Query query2 = em.createQuery(jpql2);
		query2.setParameter("matricula", matricula);
		query2.setParameter("semestreLetivo", semestreLetivo);

		List<Turma> turmas2 = query2.getResultList();

		turmas1.removeAll(turmas2);

		return turmas1;
	}

	public static Alternativa[] getAlternativas(EntityManager em) {
		String jpql = "SELECT A FROM Alternativa A ";
		Query query = em.createQuery(jpql);

		List<Alternativa> alternativas = query.getResultList();

		alternativas.size();

		Alternativa[] resp = new Alternativa[alternativas.size()];
		int i = 0;

		for (Iterator iterator = alternativas.iterator(); iterator.hasNext();) {
			Alternativa alternativa = (Alternativa) iterator.next();
			resp[i] = alternativa;
			i++;
		}
		return resp;
	}

	public static int[] getDadosQuestao(EntityManager em, Turma turma,
			PeriodoLetivo semestreLetivo, Quesito questao,
			Alternativa[] alternativas) {

		int[] resp = new int[alternativas.length];

		String sql = " SELECT count(id) FROM RESPOSTA R "
				+ " WHERE R.alternativa_id =:alternativaId and R.turma_id =:turmaId and R.questao_id =:questaoId ";

		Query query = em.createNativeQuery(sql);

		for (int i = 0; i < resp.length; i++) {
			query.setParameter("turmaId", turma.getId());
			query.setParameter("alternativaId", alternativas[i].getId());
			query.setParameter("questaoId", questao.getId());
			int total = ((BigInteger) query.getSingleResult()).intValue();
			resp[i] = total;
		}

		return resp;

	}

}
