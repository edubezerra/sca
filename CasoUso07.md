# Atualizar Informações sobre Professor (CSU07) #

---


Sumário: DRE do sistema usa o sistema para atualizar as informações cadastrais sobre professores a partir do SRH.

Ator Primário: DRE

Ator Secundário: Sistema de Recursos Humanos (SRH)

Precondições: O DRE está identificado pelo sistema.

Fluxo Principal
  1. O DRE solicita ao sistema que obtenha os dados atualizados sobre professores.
  1. O sistema se comunica com o SRH e obtém os dados a partir deste.
  1. O sistema apresenta os dados obtidos e solicita a confirmação do DRE para realizar a atualização.
  1. O DRE confirma a atualização.
  1. O sistema atualiza os dados cadastrais dos professores e o caso de uso termina.

Fluxo de Exceção (2): Houve uma falha na obtenção de dados
  * a. O sistema não consegue obter os dados a partir do SRH.
  * b. O sistema reporta o fato e o caso de uso termina.

Fluxo Alternativo (4): Desistência de atualização
  * O DRE declina da atualização e o caso de uso termina.