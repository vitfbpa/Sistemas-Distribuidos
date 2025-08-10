# Jogo da Cobrinha - Sistemas Distribuídos

## Estrutura de Arquivos
- **JFrame_Principal.java**:  
  - Classe principal da interface gráfica (herda de `JFrame`).
  - Define a janela do jogo, adiciona listeners para capturar eventos de teclado e gerencia o loop de atualização da tela.
  - Possui componentes como painel de desenho e métodos para renderizar o jogo.
  - Interage com a classe `Movimenta` para processar a movimentação do jogador.
  
- **Movimenta.java**:  
  - Classe responsável por gerenciar a lógica de movimentação.
  - Armazena coordenadas do jogador (x, y) e implementa métodos para mover para cima, baixo, esquerda e direita.
  - Utiliza valores constantes para velocidade e mantém controle do estado da movimentação.

## Funcionamento do Jogo
1. **Inicialização**:  
   - A janela é configurada com tamanho fixo, título e listener de teclado.
   
2. **Entrada do Usuário**:  
   - As teclas direcionais (`↑`, `↓`, `←`, `→`) são capturadas para alterar a posição do personagem.
   
3. **Atualização da Tela**:  
   - O painel é redesenhado constantemente, mostrando a nova posição do personagem.

4. **Movimentação**:  
   - A classe `Movimenta` calcula o deslocamento com base na tecla pressionada e atualiza as coordenadas.

## Tecnologias Utilizadas
- **Java SE**
- **Swing** para interface gráfica
- **Eventos de Teclado** (`KeyListener`)

---
