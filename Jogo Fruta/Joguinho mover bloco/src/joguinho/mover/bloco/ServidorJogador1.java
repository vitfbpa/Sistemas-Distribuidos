package joguinho.mover.bloco;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * @author vitin
 */
public class ServidorJogador1 extends javax.swing.JFrame {
    
private volatile boolean g1Up, g1Down, g1Left, g1Right; 
    private volatile boolean g2Up, g2Down, g2Left, g2Right;

    private int pontosBranco = 0;
    private int pontosVermelho = 0;
    
    private Comunicador comunicador;
    private String frutaSecretaDoLobby;

    public ServidorJogador1() {
        initComponents();
        configurarJanela();
        configurarTeclado();
        
        escolherFrutaSecreta();
        configurarRedeEAutenticar();
    }
    
    private void configurarJanela() {
        getContentPane().setLayout(null);
        this.setFocusable(true);
        this.requestFocusInWindow();
        
        GlobalJogo.criarObstaculos(this);
        Movimenta.sorteiaPosicao(btn_frutinha, this);
    }
    
    private void escolherFrutaSecreta() {
        Random gerador = new Random();
        int indice = gerador.nextInt(GlobalJogo.FRUTAS_SECRETAS.length);
        this.frutaSecretaDoLobby = GlobalJogo.FRUTAS_SECRETAS[indice];
        
        JOptionPane.showMessageDialog(this, 
                "A fruta secreta para este lobby é: " + this.frutaSecretaDoLobby, 
                "Código do Lobby", 
                JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void configurarRedeEAutenticar() {
        comunicador = new Comunicador();
        new Thread(() -> {
            if (comunicador.aguardarCliente(GlobalJogo.PORTA)) {
                
                // --- ETAPA DE AUTENTICAÇÃO ---
                Object objRecebido = comunicador.receberObjeto();
                
                // O cliente deve enviar a fruta secreta (String) como primeiro objeto
                if (objRecebido instanceof String frutaRecebida && frutaRecebida.equalsIgnoreCase(this.frutaSecretaDoLobby)) {
                    
                    JOptionPane.showMessageDialog(this, "Cliente autenticado com sucesso!");
                    comunicador.enviarObjeto("AUTENTICADO_OK"); // Avisa o cliente
                    
                    iniciarListenerCliente();
                    iniciarLoopJogo();
                    
                } else {
                    JOptionPane.showMessageDialog(this, "Cliente falhou na autenticação (Fruta errada).");
                    comunicador.enviarObjeto("AUTENTICADO_FALHA");
                    comunicador.fechar();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Servidor falhou em iniciar.");
                System.exit(1);
            }
        }).start();
    }
    
    private void iniciarListenerCliente() {
        new Thread(() -> {
            while (true) {
                // Recebe um Objeto (que esperamos ser uma String de input)
                Object inputObj = comunicador.receberObjeto(); 
                if (inputObj == null) {
                    JOptionPane.showMessageDialog(this, "Cliente desconectado.");
                    comunicador.fechar();
                    System.exit(0); 
                    break;
                }
                
                if (inputObj instanceof String input) {
                    switch (input) {
                        case "W_PRESS"   -> g2Up = true;
                        case "W_RELEASE" -> g2Up = false;
                        case "S_PRESS"   -> g2Down = true;
                        case "S_RELEASE" -> g2Down = false;
                        case "A_PRESS"   -> g2Left = true;
                        case "A_RELEASE" -> g2Left = false;
                        case "D_PRESS"   -> g2Right = true;
                        case "D_RELEASE" -> g2Right = false;
                    }
                }
            }
        }).start();
    }

    private void configurarTeclado() {
        this.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent e) {
                setFlag(e.getKeyCode(), true);
            }
            @Override
            public void keyReleased(java.awt.event.KeyEvent e) {
                setFlag(e.getKeyCode(), false);
            }
        });
    }

    private void setFlag(int codigoTecla, boolean pressionada) {
        switch (codigoTecla) {
            case java.awt.event.KeyEvent.VK_W -> g1Up = pressionada;
            case java.awt.event.KeyEvent.VK_S -> g1Down = pressionada;
            case java.awt.event.KeyEvent.VK_A -> g1Left = pressionada;
            case java.awt.event.KeyEvent.VK_D -> g1Right = pressionada;
        }
    }

    private void iniciarLoopJogo() {
        new Thread(() -> {
            while (true) {
                if (g1Up) Movimenta.cima(btn_ghost1, this);
                if (g1Down) Movimenta.baixo(btn_ghost1, this);
                if (g1Left) Movimenta.esquerda(btn_ghost1, this);
                if (g1Right) Movimenta.direita(btn_ghost1, this);

                if (g2Up) Movimenta.cima(btn_ghost2, this);
                if (g2Down) Movimenta.baixo(btn_ghost2, this);
                if (g2Left) Movimenta.esquerda(btn_ghost2, this);
                if (g2Right) Movimenta.direita(btn_ghost2, this);

                verificarColisaoFruta(btn_ghost1, 1);
                verificarColisaoFruta(btn_ghost2, 2);

                // Formata o estado como Objeto e envia
                EstadoJogo estadoJogo = formatarEstado();
                comunicador.enviarObjeto(estadoJogo);

                try {
                    Thread.sleep(GlobalJogo.PAUSA_LOOP);
                } catch (InterruptedException ex) {
                    break;
                }
            }
        }).start();
    }
    
    private void verificarColisaoFruta(JButton ghost, int jogador) {
        // Usei 'getBounds().intersects' em vez do 'Movimenta.pegou'
        if (ghost.getBounds().intersects(btn_frutinha.getBounds())) {
            if (jogador == 1) {
                pontosBranco++;
                lbl_pontosBranco.setText("Branco: " + pontosBranco);
            } else {
                pontosVermelho++;
                lbl_pontosVermelho.setText("Vermelho: " + pontosVermelho);
            }
            Movimenta.sorteiaPosicao(btn_frutinha, this);
            GlobalJogo.randomizarObstaculos(this);
            this.repaint();
        }
    }
    
    /**
     * Cria um Objeto 'EstadoJogo' com todo o estado para enviar ao cliente.
     */
    private EstadoJogo formatarEstado() {
        // usei List<GlobalJogo>
        List<GlobalJogo> componentes = new ArrayList<>();
        
        // Adiciona Jogador 1
        componentes.add(new GlobalJogo(
                btn_ghost1.getX(), btn_ghost1.getY(), 
                btn_ghost1.getWidth(), btn_ghost1.getHeight(), 
                GlobalJogo.JOGADOR1 // <- constante em uso
        ));
        
        // Adiciona Jogador 2
        componentes.add(new GlobalJogo(
                btn_ghost2.getX(), btn_ghost2.getY(), 
                btn_ghost2.getWidth(), btn_ghost2.getHeight(), 
                GlobalJogo.JOGADOR2 // <- constante em uso
        ));
        
        // Adiciona Fruta
        componentes.add(new GlobalJogo(
                btn_frutinha.getX(), btn_frutinha.getY(), 
                btn_frutinha.getWidth(), btn_frutinha.getHeight(), 
                GlobalJogo.FRUTA // <- constante em uso
        ));
        
        // Adiciona todos os Obstáculos
        for (JButton obs : GlobalJogo.getObstaculos()) {
             componentes.add(new GlobalJogo(
                obs.getX(), obs.getY(), 
                obs.getWidth(), obs.getHeight(), 
                GlobalJogo.OBSTACULO // <- constante em uso
            ));
        }

        // Cria e retorna o objeto de estado com os pontos
        return new EstadoJogo(
                componentes, 
                String.valueOf(pontosBranco), 
                String.valueOf(pontosVermelho)
        );
    }
   
    
    
    private void reiniciarJogo() {
        pontosBranco = 0;
        pontosVermelho = 0;
        
        javax.swing.SwingUtilities.invokeLater(() -> {
            lbl_pontosBranco.setText("Branco: 0");
            lbl_pontosVermelho.setText("Vermelho: 0");

            // Posições iniciais (ajuste se necessário)
            btn_ghost1.setLocation(61, 81); 
            btn_ghost2.setLocation(450, 81); 

            Movimenta.sorteiaPosicao(btn_frutinha, this);
            GlobalJogo.randomizarObstaculos(this);
            
            g1Up = g1Down = g1Left = g1Right = false;
            g2Up = g2Down = g2Left = g2Right = false;
            
            this.repaint();
            
            // Devolve o foco para a janela principal
            requestFocusInWindow(); 
        });
        
        System.out.println("--- JOGO REINICIADO PELO SERVIDOR ---");
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btn_ghost1 = new javax.swing.JButton();
        btn_ghost2 = new javax.swing.JButton();
        btn_frutinha = new javax.swing.JButton();
        lbl_pontosBranco = new javax.swing.JLabel();
        lbl_pontosVermelho = new javax.swing.JLabel();
        btn_reiniciar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        btn_ghost1.setBackground(new java.awt.Color(102, 102, 102));
        btn_ghost1.setForeground(new java.awt.Color(255, 51, 51));
        btn_ghost1.setText("👻");
        btn_ghost1.setFocusable(false);

        btn_ghost2.setBackground(new java.awt.Color(102, 102, 102));
        btn_ghost2.setForeground(new java.awt.Color(0, 153, 255));
        btn_ghost2.setText("👻");
        btn_ghost2.setFocusable(false);

        btn_frutinha.setBackground(new java.awt.Color(153, 0, 255));
        btn_frutinha.setForeground(new java.awt.Color(255, 255, 255));
        btn_frutinha.setText("🍇");
        btn_frutinha.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btn_frutinhaKeyPressed(evt);
            }
        });

        lbl_pontosBranco.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lbl_pontosBranco.setForeground(new java.awt.Color(255, 51, 51));
        lbl_pontosBranco.setText("Time 01");

        lbl_pontosVermelho.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lbl_pontosVermelho.setForeground(new java.awt.Color(0, 153, 255));
        lbl_pontosVermelho.setText(" Time 02");

        btn_reiniciar.setBackground(new java.awt.Color(255, 255, 255));
        btn_reiniciar.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btn_reiniciar.setForeground(new java.awt.Color(0, 0, 0));
        btn_reiniciar.setText("Jogar Novamente");
        btn_reiniciar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_reiniciarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(61, 61, 61)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(118, 118, 118)
                        .addComponent(btn_frutinha, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 207, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(layout.createSequentialGroup()
                            .addGap(8, 8, 8)
                            .addComponent(lbl_pontosBranco, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lbl_pontosVermelho, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(btn_ghost1, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btn_ghost2, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(170, 170, 170))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btn_reiniciar)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btn_reiniciar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_pontosBranco, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_pontosVermelho, javax.swing.GroupLayout.DEFAULT_SIZE, 44, Short.MAX_VALUE))
                .addGap(12, 12, 12)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_ghost2, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_ghost1, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 281, Short.MAX_VALUE)
                .addComponent(btn_frutinha, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(32, 32, 32))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

 
    private void btn_frutinhaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btn_frutinhaKeyPressed

    }//GEN-LAST:event_btn_frutinhaKeyPressed

    private void btn_reiniciarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_reiniciarActionPerformed
        int confirm = JOptionPane.showConfirmDialog(this, 
                "Tem certeza que deseja reiniciar a partida para todos?", 
                "Reiniciar Jogo", 
                JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            reiniciarJogo();
        }    }//GEN-LAST:event_btn_reiniciarActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
    } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
        ex.printStackTrace(); // Simples e funciona
    }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new ServidorJogador1().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_frutinha;
    private javax.swing.JButton btn_ghost1;
    private javax.swing.JButton btn_ghost2;
    private javax.swing.JButton btn_reiniciar;
    private javax.swing.JLabel lbl_pontosBranco;
    private javax.swing.JLabel lbl_pontosVermelho;
    // End of variables declaration//GEN-END:variables
}

