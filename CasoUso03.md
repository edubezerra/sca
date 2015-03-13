# Fornecer Grade de Disponibilidades (CSU03) #

Sumário: Professor fornece a sua grade de disponibilidade (disciplinas que deseja lecionar, juntamente com dias e horários em que está disponível) para o próximo semestre letivo.

Ator Primário: Professor

Pré-condições: N/A.

Fluxo Principal
  1. Professor fornece sua matrícula para validação.
  1. Sistema apresenta a lista de disciplinas disponíveis (conforme RN04), e a lista de dias da semana e de horários do semestre letivo seguinte.
  1. Professor informa (1) cada disciplina que deseja lecionar e (2) cada disponibilidade para o próximo semestre letivo.
  1. Professor solicita o registro da grade.
  1. Sistema registra a grade e o caso de uso termina.

Fluxo Alternativo (3): Modificação na grade atual
  * a. Professor solicita que o sistema apresente a mesma grade do semestre atual.
  * b. Sistema apresenta a configuração de grade requisitada.
  * c. Professor realiza as modificações que deseja na grade e solicita o seu registro.
  * d. Sistema registra a grade alterada e o caso de uso termina.

Fluxo de Exceção (4): O Professor não forneceu disciplina alguma.
  * a. Sistema reporta o fato e o caso de uso continua a partir do passo 3.

Fluxo de Exceção (4): Professor não forneceu dias e horários.
  * a. Sistema reporta o fato e o caso de uso continua a partir do passo 3.

Pós-condições: o sistema registrou a disponibilidade do Professor para o próximo semestre letivo.

Regras de Negócio: RN04