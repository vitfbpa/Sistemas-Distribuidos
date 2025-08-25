# Quadro Branco

## Aula 5 - Sincronização Distribuída — Relógios Físicos e Lógicos. Exclusão Mútua e Eleição.

- **Teoria básica de sistemas distribuídos**
  - O que é e para que serve → compartilhar recurso (CPU, RAM, memória secundária)
  - Diferenças entre grid (computação concomitante) e cluster (computação paralela)
  - Comunicação entre computadores ou equipamentos em sistemas distribuídos
    - Modelo TCP/IP: endereço, porta, máscara de rede, socket, camada de transporte (UDP e TCP)
  - Comunicação e leitura (consumidor) ou escrita (produtor) – **É BLOQUEANTE**

- **THREADS:** mini processos concomitantes → desbloquear a comunicação
  - Sem memória compartilhada
  - Com memória compartilhada
  - Delegar uma rotina para thread; passar parâmetros; identificação

- **SINCRONISMO:** acesso à seção crítica → memória compartilhada
  - Java: `synchronized`
  - C# e Python: `lock`
  - Via relógio: `físico e lógico`
  - Exclusão mútua: `lock, relógio ou eleição`
 
  ---
 
  Para próxima aula, refazer o Tele Jogo (GitHub do professor) com uso de threads (sem e com compartilhamento de recursos) e fazer uma pesquisa sobre Relógios físicos e lógicos.
