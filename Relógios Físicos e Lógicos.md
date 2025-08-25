# Sistemas Distribuídos – Tipos de Relógios

## Relógios Físicos
São baseados no tempo real do hardware, seguindo o padrão **UTC**.  
Podem apresentar desvios de sincronização (clock drift) e, por isso, precisam ser ajustados por protocolos como o **NTP (Network Time Protocol)**.  
**Aplicações:** coordenação de tarefas em tempo real, registro de logs e comunicação precisa.

## Relógios Lógicos
Não medem a hora real, mas organizam a **ordem dos eventos**.  
São usados para manter a relação causal entre processos distribuídos.  
Exemplos:  
- **Relógio de Lamport:** fornece uma sequência parcial.  
- **Relógio Vetorial:** garante ordem causal mais detalhada.  

**Aplicações:** consistência na troca de mensagens e ordenação de eventos em sistemas distribuídos.

---

## Exclusão Mútua
Mecanismo para garantir que apenas um processo acesse uma **região crítica** de cada vez.  

**Características principais:**  
- **Mutual Exclusion:** somente um processo utiliza o recurso por vez.  
- **Progress:** se o recurso está disponível, algum processo deve ser atendido.  
- **Bounded Waiting:** nenhum processo espera indefinidamente.  

**Formas de implementação:**  
- **Centralizada:** um coordenador controla o acesso.  
- **Distribuída:** uso de algoritmos como **Ricart-Agrawala** ou **Token Ring**.  

**Aplicações:** controle de recursos compartilhados como impressoras, arquivos ou bancos de dados.

---

## Eleição
Algoritmo para definir um coordenador ou líder quando o sistema precisa de uma entidade central ou quando o líder atual falha.  

**Principais métodos:**  
- **Bully:** o processo com maior ID se torna o líder.  
- **Ring:** os processos trocam mensagens em anel até o maior ID vencer.  

**Aplicações:** coordenação e recuperação de falhas em sistemas distribuídos.

---

## Exemplo do Dia a Dia – Aeroportos
- **Relógios Físicos:** todos os sistemas e painéis seguem horários globais (UTC) para voos e comunicações.  
- **Relógios Lógicos:** organizam a sequência de eventos (check-in → bagagem → embarque → decolagem).  
- **Exclusão Mútua:** a pista de pouso/decolagem é um recurso crítico, usado por um avião de cada vez, controlado pela torre de comando.  
- **Eleição:** em caso de falha do sistema principal, outro controlador ou torre pode assumir a coordenação, garantindo a continuidade do serviço.
