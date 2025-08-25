# Relógios Físicos e Lógicos

## Relógios Físicos
- **Definição:** Baseados em hardware (osciladores de quartzo, relógios atômicos) para medir tempo real.  
- **Problemas:** Sofrem *drift* (desvios) ao longo do tempo.  
- **Sincronização:** Necessária em sistemas distribuídos. Algoritmos comuns:
  - **Cristian:** Cliente consulta servidor de tempo.
  - **Berkeley:** Média dos tempos dos nós.
  - **NTP/PTP:** Protocolos modernos para internet e alta precisão.  
- **Aplicações:** Logs, transações, auditoria.

## Relógios Lógicos
- **Motivação:** Ordem dos eventos (causalidade) é mais importante que o tempo real.  
- **Lamport:** Cada processo incrementa contador; mensagens carregam timestamp.  
- **Limitações:** Não diferencia eventos concorrentes.  
- **Relógios Vetoriais:** Vetores por processo, detectam causalidade e concorrência.  

## Comparativo

| Aspecto         | Físico                   | Lógico                  |
|-----------------|--------------------------|-------------------------|
| Base            | Tempo real               | Ordem causal            |
| Sincronização   | Protocolos de rede       | Troca de mensagens      |
| Precisão        | Alta precisão temporal   | Consistência lógica     |
| Aplicações      | Logs, auditoria          | Exclusão mútua, ordenação |
