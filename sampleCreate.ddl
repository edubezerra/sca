create sequence hibernate_sequence start with 1 increment by 1
create table Alternativa (id bigint not null, descritor varchar(255) not null, primary key (id))
create table Aluno (id bigint not null, matricula varchar(255), cpf varchar(255), dataNascimento timestamp, endereco varchar(255), UF varchar(255), bairro varchar(255), cidade varchar(255), numero varchar(255), rua varchar(255), nome varchar(255), curso_id bigint, historico_id bigint, primary key (id))
create table Aula (id bigint not null, dia integer, fim timestamp, inicio timestamp, local_id bigint, TURMA_ID bigint, primary key (id))
create table AvaliacaoEgresso (id bigint not null, aspectosNegativos clob(8192), aspectosPositivos clob(8192), alunoAvaliador_id bigint not null, primary key (id))
create table AvaliacaoTurma (id bigint not null, aspectosNegativos clob(8192), aspectosPositivos clob(8192), alunoAvaliador_id bigint not null, turmaAvaliada_id bigint not null, primary key (id))
create table Curso (id bigint not null, nome varchar(255), sigla varchar(255), primary key (id))
create table Departamento (id bigint not null, nome varchar(255), primary key (id))
create table Disciplina (id bigint not null, cargaHoraria integer not null, codigo varchar(255), nome varchar(255), periodoIdeal varchar(255), quantidadeCreditos integer, versaoCurso_id bigint, primary key (id))
create table DISCIPLINA_PREREQS (GRADE_ID bigint not null, DISCIPLINA_ID bigint not null, primary key (GRADE_ID, DISCIPLINA_ID))
create table GradeDisponibilidade (id bigint not null, ano integer, periodo integer, professor_id bigint, primary key (id))
create table GRADEDISPONIBILIDADE_DISCIPLINA (GRADE_ID bigint not null, DISCIPLINA_ID bigint not null, primary key (GRADE_ID, DISCIPLINA_ID))
create table HistoricoEscolar (id bigint not null, primary key (id))
create table HistoricoEscolar_ItemHistoricoEscolar (HistoricoEscolar_id bigint not null, itens_id bigint not null, primary key (HistoricoEscolar_id, itens_id))
create table Inscricao (id bigint not null, aluno_id bigint, avaliacao_id bigint, TURMA_ID bigint, primary key (id))
create table ItemHistoricoEscolar (id bigint not null, ano integer, periodo integer, situacao integer, disciplina_id bigint, primary key (id))
create table ItemHorario (id bigint not null, dia integer, fim timestamp, inicio timestamp, GRADEDISPONIBILIDADE_ID bigint, primary key (id))
create table ListaEspera (id bigint not null, disciplina_id bigint, primary key (id))
create table LISTAESPERA_ALUNO (LISTAESPERA_ID bigint not null, ALUNO_ID bigint not null)
create table LocalAula (id bigint not null, capacidade integer, descricao varchar(255), primary key (id))
create table NotaFinal (id bigint not null, frequencia numeric(19,2), notaP1 numeric(19,2), notaP2 numeric(19,2), notaP3 numeric(19,2), primary key (id))
create table Professor (id bigint not null, matricula varchar(255), cpf varchar(255), dataNascimento timestamp, endereco varchar(255), UF varchar(255), bairro varchar(255), cidade varchar(255), numero varchar(255), rua varchar(255), nome varchar(255), departamento_id bigint, primary key (id))
create table PROFESSOR_DISCIPLINA (PROFESSOR_ID bigint not null, DISCIPLINA_ID bigint not null, primary key (PROFESSOR_ID, DISCIPLINA_ID))
create table Professor_GradeDisponibilidade (Professor_id bigint not null, grades_id bigint not null, primary key (Professor_id, grades_id))
create table Quesito (id bigint not null, enunciado varchar(255) not null, primary key (id))
create table QUESITO_ALTERNATIVA (QUESITO_ID bigint not null, ALTERNATIVA_ID bigint not null)
create table RESPOSTA (AVALIACAOTURMA_ID bigint not null, ALTERNATIVA_ID bigint not null)
create table Turma (id bigint not null, capacidadeMaxima integer not null, codigo varchar(255), ano integer, periodo integer, disciplina_id bigint, professor_id bigint, primary key (id))
create table VersaoGradeCurso (id bigint not null, numero varchar(255), CURSO_ID bigint, primary key (id))
alter table Aluno add constraint FKhd33lesnacp3tp8sx85r6ygbu foreign key (curso_id) references Curso
alter table Aluno add constraint FKqf7pd9vdsyfs7m3p3y7fx7m47 foreign key (historico_id) references HistoricoEscolar
alter table Aula add constraint FKf1ij85f1vdd98hxalv12b8g3x foreign key (local_id) references LocalAula
alter table Aula add constraint FKa2q9opqd9vnoknur5ylyd3qpj foreign key (TURMA_ID) references Turma
alter table AvaliacaoEgresso add constraint FKak3mqu86t4r69h8pv6jjsnrh1 foreign key (alunoAvaliador_id) references Aluno
alter table AvaliacaoTurma add constraint FKsqab16vp3s27b3fpfj7wjk6st foreign key (alunoAvaliador_id) references Aluno
alter table AvaliacaoTurma add constraint FKmwes5emkw3assvoxno9slhdg6 foreign key (turmaAvaliada_id) references Turma
alter table Disciplina add constraint FKrq33fqktam3o6c0pv01dn0arm foreign key (versaoCurso_id) references VersaoGradeCurso
alter table DISCIPLINA_PREREQS add constraint FK3xfhcqnjmm3n5p4t7p3icyu8r foreign key (DISCIPLINA_ID) references Disciplina
alter table DISCIPLINA_PREREQS add constraint FKbur1syi2tfkygsvvlq4e7iodb foreign key (GRADE_ID) references Disciplina
alter table GradeDisponibilidade add constraint FKae34ayhd2v3xyaal8t3g177pd foreign key (professor_id) references Professor
alter table GRADEDISPONIBILIDADE_DISCIPLINA add constraint FKajtgsu6s7kpekrwhbgqcjqlys foreign key (DISCIPLINA_ID) references Disciplina
alter table GRADEDISPONIBILIDADE_DISCIPLINA add constraint FKp5pu733wrosybsafu4kp7an5 foreign key (GRADE_ID) references GradeDisponibilidade
alter table HistoricoEscolar_ItemHistoricoEscolar add constraint UK_b7cawbnpwhq9a820i7neaha5g unique (itens_id)
alter table HistoricoEscolar_ItemHistoricoEscolar add constraint FKgrhdv9cgwje3pvxa5jgynsndf foreign key (itens_id) references ItemHistoricoEscolar
alter table HistoricoEscolar_ItemHistoricoEscolar add constraint FKn5306iatrkp97xic01re1db5v foreign key (HistoricoEscolar_id) references HistoricoEscolar
alter table Inscricao add constraint FKt48bstymoi0yfjv4ojapf99yl foreign key (aluno_id) references Aluno
alter table Inscricao add constraint FKevvoksc9f9ok93h6sxc77h7s7 foreign key (avaliacao_id) references NotaFinal
alter table Inscricao add constraint FKet251hfwdo80odjr2wdsi18ut foreign key (TURMA_ID) references Turma
alter table ItemHistoricoEscolar add constraint FKsl0c24298w89jnxdeq49jifsd foreign key (disciplina_id) references Disciplina
alter table ItemHorario add constraint FKserxiyutox38lfjy0f1bfq6r8 foreign key (GRADEDISPONIBILIDADE_ID) references GradeDisponibilidade
alter table ListaEspera add constraint FK3tjpnrsal5am5ucwfntumacp3 foreign key (disciplina_id) references Disciplina
alter table LISTAESPERA_ALUNO add constraint FK827vwg1cnvwldd3slpkq8ove foreign key (ALUNO_ID) references Aluno
alter table LISTAESPERA_ALUNO add constraint FKgsdumxaqr6612yperl8km1cxd foreign key (LISTAESPERA_ID) references ListaEspera
alter table Professor add constraint FKj98gljve5cwy75xrmh24kd5ia foreign key (departamento_id) references Departamento
alter table PROFESSOR_DISCIPLINA add constraint FKhd3veyetedyxhs9mn6nasxivu foreign key (DISCIPLINA_ID) references Disciplina
alter table PROFESSOR_DISCIPLINA add constraint FKlbwfm410vn467dgp5h94n98d7 foreign key (PROFESSOR_ID) references Professor
alter table Professor_GradeDisponibilidade add constraint UK_pwp5qurj7rkin57rv22s9mgyb unique (grades_id)
alter table Professor_GradeDisponibilidade add constraint FKlsr1aukvv6qp22r9ku4xi5kte foreign key (grades_id) references GradeDisponibilidade
alter table Professor_GradeDisponibilidade add constraint FKqaalulrj53ua4wjelpdfnekev foreign key (Professor_id) references Professor
alter table QUESITO_ALTERNATIVA add constraint FKn0ew6khxp4cqsvmw25mn2asi0 foreign key (ALTERNATIVA_ID) references Alternativa
alter table QUESITO_ALTERNATIVA add constraint FKffl1d198jjjvp2cr755tqpex4 foreign key (QUESITO_ID) references Quesito
alter table RESPOSTA add constraint FK9hepanirl5p2k04idvc0hllvk foreign key (ALTERNATIVA_ID) references Alternativa
alter table RESPOSTA add constraint FKjsb0xnh3kw4uifxw1et4bi0j3 foreign key (AVALIACAOTURMA_ID) references AvaliacaoTurma
alter table RESPOSTA add constraint FKqvsqlaewe3vu9nglieguc0nxl foreign key (AVALIACAOTURMA_ID) references AvaliacaoEgresso
alter table Turma add constraint FK8046jn01khei22nwsvj86kmfr foreign key (disciplina_id) references Disciplina
alter table Turma add constraint FKq31csamjp0hsa4qnkqr0n4l3r foreign key (professor_id) references Professor
alter table VersaoGradeCurso add constraint FK14wwiqm535m2slvj8d9294wh0 foreign key (CURSO_ID) references Curso
