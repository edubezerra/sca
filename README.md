# SCA - Sistema de Controle Acadêmico

Este projeto corresponde à implementação de algumas funcionalidades do Sistema de Controle Acadêmico (SCA), estudo de caso apresentado no livro *Princípios de Análise e Projeto de Sistemas orientados a Objetos com UML* (http://eic.cefet-rj.br/papsuml3ed/). O objetivo desse projeto é consolidar os principais conceitos teóricos descritos no livro e oferecer uma visão prática sobre como os modelos apresentados são desenvolvidos.

O SCA é um sistema para uma instituição de ensino fictícia que precisa controlar alguns processos acadêmicos, como inscrições de discentes, lançamentos de notas, alocação de recursos para turmas etc. 

 A linguagem de programação escolhida para esse desenvolvimento foi Java. Essa implementação faz uso intenso do framework Spring (https://spring.io/). Em particular, são usados: Spring Security (http://projects.spring.io/spring-security/), Spring DataJPA (http://projects.spring.io/spring-data-jpa/). Note que aspectos de implementação nesse nível *não* são abordados no livro supra-mencionado.

Esse projeto está configurado com o Maven (https://maven.apache.org/). Também está configurado para fazer acesso a um banco de dados em HyperSQL (http://hsqldb.org/). Para testar a aplicação, dados devem inicialmente ser carregados. Para fazer essa carga de dados, execute a classe br.cefetrj.sca.infra.cargadados.ImportadorTudo.

Para testar os diferentes perfis de usuário, veja o arquivo src/main/resources/db.sql.
