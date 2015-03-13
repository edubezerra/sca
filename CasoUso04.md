# Lançar Avaliações (CSU04) #

Sumário: Professor realiza o lançamento de avaliações (notas e frequências) para alunos de uma turma sob sua responsabilidade no semestre letivo corrente.

Ator Primário: Professor

Precondições: O Professor está identificado pelo sistema.

Fluxo Principal
  1. O Professor solicita o lançamento de notas.
  1. O sistema exibe a lista de turmas do semestre letivo corrente nas quais o Professor lecionou.
  1. O Professor seleciona a turma para a qual deseja realizar o lançamento.
  1. Para cada aluno inscrito na turma selecionada, o sistema nome e matrícula e requisita a primeira nota (A1), a segunda nota (A2) e a quantidade de faltas.
  1. O Professor fornece as notas de A1 e de A2 e a quantidade de faltas para cada aluno.
  1. O sistema exibe o resultado da avaliação de cada aluno, conforme regra de negócio [RegrasNegocio#RegraNegocio06](RegrasNegocio#RegraNegocio06.md).
  1. O Professor confirma o lançamento.
  1. O sistema registra as avaliações, e o caso de uso termina.

Fluxo Alternativo (7): Erro no lançamento (O professor detecta que lançou uma avaliação errada para algum aluno.)
  * a.	O professor corrige a informação lançada erroneamente.
  * b.	O sistema aceita a correção, e o caso de uso continua a partir do passo 7.

Fluxos de Exceção (4): Avaliação em branco ou errada
  * a. O Professor não fornece alguma nota, ou freqüência, ou fornece dados inválidos:
  * b. O sistema reporta o fato e o caso de uso retorna ao passo 4.

Pós-condições: as avaliações de uma turma lecionada pelo professor foram registradas no sistema.

Regras de Negócio: [RegrasNegocio#RegraNegocio05](RegrasNegocio#RegraNegocio05.md), [RegrasNegocio#RegraNegocio06](RegrasNegocio#RegraNegocio06.md)