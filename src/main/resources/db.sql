
delete from ROLES;
delete from USERS;
delete from USER_PROFILE;

INSERT INTO USERS (id,dob,login,nome,password, email) VALUES 
 (1,NULL,'admin','Administrador','admin', 'admin@email.com'),
 (2,NULL,'1506449','EDUARDO BEZERRA','1506449', 'edubezerra@gmail.com'),
 (3,NULL,'1311038BCC','CHRISTOFER MARINHO RAQUEL DANTAS','1311038BCC', 'christofer@gmail.com'),
 (4,NULL,'1223216BCC','REBECCA PONTES SALLES','1223216BCC', 'rebeccapsalles@gmail.com'),
 (5,NULL,'1024600WEB','EDUARDO AUGUSTO NOVO MACHADO','1024600WEB', 'eduardoanm@gmail.com');

/* Popula a tabela USER_PROFILE */
INSERT INTO USER_PROFILE(type) VALUES ('ROLE_USER');
INSERT INTO USER_PROFILE(type) VALUES ('ROLE_ADMIN');
INSERT INTO USER_PROFILE(type) VALUES ('ROLE_PROFESSOR');
--INSERT INTO USER_PROFILE(type) VALUES ('ROLE_COORDENADOR_CURSO');
INSERT INTO USER_PROFILE(type) VALUES ('ROLE_ALUNO');

--delete from roles;

INSERT INTO roles (role_id,role_name,user_id) VALUES 
 (1,'ROLE_ADMIN',1),
 (2,'ROLE_USER',1),
 (3,'ROLE_PROFESSOR',2),
 (4,'ROLE_ALUNO',3),
 (5,'ROLE_ALUNO',4),
 (6,'ROLE_ALUNO',5);