# SCA
Sistema de Controle Acadêmico

Este projeto é um implementação de algumas funcionalidades do Sistema de Controle Acadêmico (SCA). Seu objetivo é consolidar os principais conceitos teóricos descritos no livro *Princípios de Análise e Projeto de Sistemas orientados a Objetos com UML* (http://eic.cefet-rj.br/papsuml3ed/) e oferecer uma visão prática sobre como os modelos apresentados neste livro são desenvolvidos.

O estudo de caso utilizado no livro, o *Sistema de Controle Acadêmico* (SCA), é um sistema para uma instituição de ensino fictícia que precisa controlar alguns processos acadêmicos, como inscrições de discentes, lançamentos de notas, alocação de recursos para turmas etc. 

 A linguagem de programação escolhida para esse desenvolvimento foi Java. Essa implementação faz uso intenso do framework Spring (Spring Secutry, Spring DataJPA, etc).

Esse projeto está configurado para fazer acesso a um banco de dados em HyperSQL (http://hsqldb.org/). Para testar a aplicação, dados devem inicialmente ser carregados. Para fazer essa carga de dados, execute a classe br.cefetrj.sca.infra.cargadados.ImportadorTudo.

Para testar os diferentes perfis de usuário, veja o arquivo src/main/resources/db.sql.
