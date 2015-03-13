# Visualizar Avaliações (CSU02) #

Sumário: Aluno visualiza avaliação que recebeu (notas e freqüência) nas turmas em que participou em um semestre letivo.

Ator Primário: Aluno

Precondições: O Aluno está identificado pelo sistema.

Fluxo Principal
  1. O Aluno solicita a visualização das avaliações para as turmas em que participou.
  1. O sistema exibe os semestres letivos nos quais o Aluno se inscreveu em pelo menos uma turma cujo lançamento já tenha sido encerrado.
  1. O Aluno seleciona os semestres letivos para as turmas cujas avaliações deseja visualizar.
  1. O sistema exibe uma lista de avaliações, agrupadas pelos semestres letivos selecionados e por turma, e o caso de uso termina.

Fluxo de Exceção (2): Não há semestre letivo no qual o Aluno tenha participado em alguma turma.
  * a. O sistema reporta o fato e o caso de uso termina.

Pós-condições: N/A.