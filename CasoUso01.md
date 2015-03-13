# Realizar Inscrições (CSU01) #

Sumário: Aluno usa o sistema para realizar inscrição em disciplinas.

Ator Primário: Aluno

Atores Secundários: Sistema de Faturamento

Precondições: O Aluno está identificado pelo sistema.

Fluxo Principal
  1. Aluno solicita a realização de inscrições.
  1. Sistema apresenta as disciplinas (e respectivos códigos das turmas) em que o aluno pode se inscrever (conforme a  [RegrasNegocio#RegraNegocio03](RegrasNegocio#RegraNegocio03.md)).
  1. Aluno escolhe a lista de turmas que deseja cursar no próximo semestre letivo e as submete para inscrição.
  1. Para cada turma, o sistema informa o professor, os horários e os respectivos locais das aulas.
  1. Aluno confirma as inscrições.
  1. Sistema registra as inscrições do Aluno, envia os dados sobre as mesmas para o Sistema de Faturamento, e o caso de uso termina.

Fluxo Alternativo (4): Não há vaga disponível para alguma turma selecionada pelo aluno (conforme a  [RegrasNegocio#RegraNegocio02](RegrasNegocio#RegraNegocio02.md)),
  * a. Sistema fornece a possibilidade de inserir o Aluno em uma lista de espera.
  * b. Se o Aluno aceitar, o sistema o insere na lista de espera e apresenta a posição na qual o aluno foi inserido na lista. O caso de uso retorna ao passo 4.
  * c. Se o Aluno não aceitar, o caso de uso prossegue a partir do passo 4.

Fluxo Alternativo (5): Revisão das inscrições
  * Aqui, é possível que o caso de uso retorne ao passo 3, conforme o Aluno queira revisar (inserir ou remover itens) a lista de disciplinas selecionadas.

Fluxo de Exceção (4): Violação de [RegrasNegocio#RegraNegocio00](RegrasNegocio#RegraNegocio00.md) - Aluno selecionou turmas para as quais há choque de horários.
  * a. Sistema informa as turmas em que houve choque de horários, juntamente com os respectivos horários de cada uma, e o caso de uso retorna ao passo 2.

Fluxo de Exceção (4): Violação de [RegrasNegocio#RegraNegocio01](RegrasNegocio#RegraNegocio01.md) - Aluno atingiu a quantidade máxima de inscrições possíveis.
  * a. Sistema informa a quantidade de créditos que o aluno pode selecionar, e o caso de uso retorna ao passo 2.

Pós-condições: O aluno foi inscrito em uma turma de cada uma das disciplinas desejadas, ou foi adicionado a uma ou mais listas de espera.

Regras de Negócio: [RegrasNegocio#RegraNegocio00](RegrasNegocio#RegraNegocio00.md),  [RegrasNegocio#RegraNegocio01](RegrasNegocio#RegraNegocio01.md),  [RegrasNegocio#RegraNegocio02](RegrasNegocio#RegraNegocio02.md),  [RegrasNegocio#RegraNegocio03](RegrasNegocio#RegraNegocio03.md)